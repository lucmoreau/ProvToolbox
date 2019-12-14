package org.openprovenance.prov.template.compiler;

import javax.lang.model.element.Modifier;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.template.expander.ExpandAction;
import org.openprovenance.prov.template.expander.ExpandUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TemplateCompiler {
    
    final private ProvFactory pFactory;

    static ObjectMapper mapper = new ObjectMapper();



    public TemplateCompiler(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
    
    static ProvUtilities u= new ProvUtilities();
    
    final CompilerUtil gu=new CompilerUtil();
    
    boolean withMain=true; // TODO need to be updatable via command line
   
    public boolean generate(Document doc, String templateName, String packge, String cli_src_dir, String l2p_src_dir, String resource,  JsonNode bindings_schema) {
        try {
            String bn=templateNameClass(templateName);
            String destinationDir=l2p_src_dir + "/" + packge.replace('.', '/') + "/";
            String destinationDir2=cli_src_dir + "/" + packge.replace('.', '/') + "/" + "client" + "/";
            
            String destination=destinationDir + bn + ".java";
            String destination2=destinationDir2 + bn + ".java";
            JavaFile spec=generateBuilderSpecification(doc,bn,templateName,packge, resource, bindings_schema);
            
            boolean val1=saveToFile(destinationDir, destination, spec);
            JavaFile spec2=generateClientLib(doc,bn,templateName,packge+ ".client", resource, bindings_schema);

            boolean val2=saveToFile(destinationDir2, destination2, spec2);
            
            
            return val1 & val2;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }


    public boolean saveToFile(String destinationDir, String destination, JavaFile spec) {
        PrintWriter out;
        try {
            File dir=new File(destinationDir);
            if (!dir.exists() && !dir.mkdirs()) {
                System.err.println("failed to create directory " + destinationDir);
                return false;
            };
            out = new PrintWriter(destination);
            out.print(spec);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public String templateNameClass(String templateName) {
        return gu.capitalize(templateName)+"Builder";
    }
    
    

   
    public JavaFile generateBuilderSpecification(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun=u.getBundle(doc).get(0);
        
        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();
        
        gu.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);
        
        return generateBuilderSpecification_aux(doc, allVars,allAtts,name, templateName, packge, resource, bindings_schema);
        
    }
    
    public JavaFile generateClientLib(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun=u.getBundle(doc).get(0);
        
        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();
        
        gu.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);
        
        
        
        IndexedDocument indexed=new IndexedDocument(pFactory,pFactory.newDocument(),true);
        u.forAllStatement(bun.getStatement(), indexed);
        
        
        return generateClientLib_aux(doc, allVars,allAtts,name, templateName, packge, resource, bindings_schema, indexed);
        
    }
    
    private JavaFile generateBuilderSpecification_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {
        
        
        Builder builder = gu.generateClassBuilder2(name);
        
        Hashtable<QualifiedName, String> vmap=generateQualifiedNames(doc,builder);

        
        builder.addMethod(gu.generateConstructor2(vmap));
        
        builder.addMethod(generateTemplateGenerator(allVars, allAtts, doc,vmap, bindings_schema));
        
        builder.addMethod(nameAccessorGenerator(templateName));
        
        if (withMain) builder.addMethod(generateMain(allVars, allAtts, name, bindings_schema));

        if (bindings_schema!=null) {
            builder.addMethod(generateFactoryMethod(allVars, allAtts, name, bindings_schema));
            builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));
        }
        

       // System.out.println(allVars);
        
        TypeSpec bean=builder.build();
        
        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox for template $S",templateName)
                .build();

        return myfile;
    }
    
    private JavaFile generateClientLib_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema, IndexedDocument indexed) {
        
        
        Builder builder = gu.generateClassInit(name,ConfigProcessor.CLIENT_PACKAGE,"Builder");
             
        
        
 
        

        if (bindings_schema!=null) {
            builder.addMethod(generateClientMethod(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod2(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod3static(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod3(allVars, allAtts, name, templateName, bindings_schema));
            builder.addMethod(generateClientMethod4static(allVars, allAtts, name, templateName, bindings_schema, indexed));
            
            
            builder.addField(FieldSpec.builder(hashmapType, "__successors")
                      .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                      .initializer("__getSuccessors()")
                      .build());
            
            builder.addField(FieldSpec.builder(int[].class, "__nodes")
                             .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                             .initializer("__getNodes()")
                             .build());
                   
            builder.addMethod(generateClientMethod4(allVars, allAtts, name, templateName, bindings_schema, indexed));
            builder.addMethod(nameAccessorGenerator(templateName));

    //      builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));
        }
        

       // System.out.println(allVars);
        
        TypeSpec bean=builder.build();
        
        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox for template $S",templateName)
                .build();

        return myfile;
    }
    
   
   public MethodSpec nameAccessorGenerator(String templateName) {


       MethodSpec.Builder builder = MethodSpec.methodBuilder("getName")
               .addModifiers(Modifier.PUBLIC)
               .addAnnotation(Override.class)

               .returns(String.class)
               .addStatement("return $S", templateName);
       return builder.build();
   }

 
       
   
   public MethodSpec generateTemplateGenerator(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Document doc, Hashtable<QualifiedName, String> vmap, JsonNode bindings_schema) {
              

       MethodSpec.Builder builder = MethodSpec.methodBuilder("generator")
               .addModifiers(Modifier.PUBLIC)
               .returns(Document.class)
               .addStatement("$T nullqn = null", QualifiedName.class)
               .addStatement("$T attrs=null", StatementCompilerAction.cl_collectionOfAttributes)
               .addStatement("$T document = pf.newDocument()", Document.class)
 
       ;
       for (QualifiedName q: allVars) {
           builder.addParameter(QualifiedName.class, q.getLocalPart());
       }
       for (QualifiedName q: allAtts) {
           if (allVars.contains(q)) {
               // no need to redeclare
           } else {
               builder.addParameter(Object.class, q.getLocalPart()); // without type declaration, any object may be accepted, assuming this is not a q also in allVars.
           }
       }
       for (QualifiedName q: allVars) {
           if (ExpandUtil.isGensymVariable(q)) {
               final String vgen = q.getLocalPart();
               builder.addStatement("if ($N==null) $N=$T.getUUIDQualifiedName2(pf)",vgen,vgen,ExpandAction.class);
           }
       }

       
       StatementCompilerAction action=new StatementCompilerAction(pFactory, allVars, allAtts, vmap, builder, "document.getStatementOrBundle()", bindings_schema);
       for (StatementOrBundle s: doc.getStatementOrBundle()) {
           u.doAction(s, action);
           
       }
       builder.addStatement("new $T().updateNamespaces(document)", ProvUtilities.class);

       builder.addStatement("return document");

       MethodSpec method=builder.build();
       
       return method;
   }
   
   public Hashtable<QualifiedName, String> generateQualifiedNames(Document doc, Builder builder) {
       Bundle bun=u.getBundle(doc).get(0);
       Set<QualifiedName> set=new HashSet<QualifiedName>();
       gu.allQualifiedNames(bun,set,pFactory);
       set.remove(pFactory.newQualifiedName(ExpandUtil.TMPL_NS,ExpandUtil.LABEL,ExpandUtil.TMPL_PREFIX));
       set.add(pFactory.getName().PROV_LABEL);
       Hashtable<QualifiedName,String> qnVariables=new Hashtable<QualifiedName, String>();
       for (QualifiedName qn: set) {
           if (!(ExpandUtil.isVariable(qn))) {
               final String v = variableForQualifiedName(qn);
               qnVariables.put(qn,v);

               builder.addField(QualifiedName.class,v, Modifier.PUBLIC, Modifier.FINAL);
           }

           
       }
       return qnVariables;

   }


   public String variableForQualifiedName(QualifiedName qn) {
       return "_Q_" + qn.getPrefix() + "_" + qn.getLocalPart();
   }

   public MethodSpec generateFactoryMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, JsonNode bindings_schema) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
               .addModifiers(Modifier.PUBLIC)
               .returns(Document.class)
               .addStatement("$T document = null", Document.class)
               .addStatement("$T ns = new Namespace()", Namespace.class)
  
               ;
       
       JsonNode the_var=bindings_schema.get("var");
       JsonNode the_context=bindings_schema.get("context");

       generateSpecializedParameters(builder, the_var);
       
       Iterator<String> iter2=the_context.fieldNames();
       while(iter2.hasNext()){
           String prefix=iter2.next();
           String uri=the_context.get(prefix).textValue();
           builder.addStatement("ns.register($S,$S)", prefix, uri);  // TODO: needs substitution here, to expand the URI potentially containing * 
       }
           
       
       String args="";
       boolean first=true;
       for (QualifiedName q: allVars) {
           final String key = q.getLocalPart();
           final String newName = "__"+key;
           final JsonNode entry = the_var.path(key);
           if (entry!=null && !(entry instanceof MissingNode)) {
               String s=entry.get(0).get("@id").textValue();
               String s2="\"" + s.replace("*","\" + $N + \"") + "\"";
               builder.addStatement("$T $N=($N==null)?null:ns.stringToQualifiedName(" + s2 + ",pf)", QualifiedName.class, newName, key,key);
           } else {
               // TODO: check if it was a gensym, because then i can generate it!
               builder.addStatement("$T $N=null", QualifiedName.class, newName);
           }
           if (first) {
               first=false;
               args=newName;
           } else {
               args=args + ", " + newName; 
           }
       }

       for (QualifiedName q: allAtts) {
           final String key = q.getLocalPart();
           String newName = key;
           final JsonNode entry = the_var.path(key);
           JsonNode jentry;
           if (entry!=null && !(entry instanceof MissingNode)  && ((jentry=entry.get(0).get("@id"))!=null)) {
               String s=jentry.textValue();
               String s2="\"" + s.replace("*","\" + $N + \"") + "\"";
               newName="__"+key;
               builder.addStatement("$T $N=($N==null)?null:ns.stringToQualifiedName(" + s2 + ",pf)", QualifiedName.class, newName, key,key);
           }
           if (first) {
               first=false;
               args=newName;
           } else {
               args=args + ", " + newName;
           }
       }
       
       builder.addStatement("document = generator(" + args + ")");

                      
       builder.addStatement("return document");

 
       
       MethodSpec method=builder.build();
       
       return method;
   }


   public void generateSpecializedParameters(MethodSpec.Builder builder, JsonNode the_var) {
       Iterator<String> iter=the_var.fieldNames();
       while(iter.hasNext()){
           String key=iter.next();
           builder.addParameter(getJavaTypeForDeclaredType(the_var, key), key); 
       }
   }


    public String getConverterForDeclaredType(Class cl) {
       if  (cl!=null) {
            String keyType=cl.getName();
            switch (keyType) {
                case "java.lang.Integer":
                    return "toInt";
                case "java.lang.Long":
                    return "toLong";
                case "java.lang.String":
                    return null;
                case "java.lang.Boolean":
                    return "toBoolean";
                case "java.lang.Float":
                    return "toFloat";
                case "java.lang.Double":
                    return "toDouble";
                default:
                    throw new UnsupportedOperationException();
            }
        } else {
            return null;
        }
    }




    public void generateSpecializedParametersJavadoc(MethodSpec.Builder builder, JsonNode the_var) {
       Iterator<String> iter=the_var.fieldNames();
       while(iter.hasNext()){
           String key=iter.next();
           
           final JsonNode entry = the_var.path(key);
           if (entry!=null && !(entry instanceof MissingNode)) {
               JsonNode firstNode = entry.get(0);
               if (firstNode instanceof ArrayNode) {
                   firstNode=((ArrayNode)firstNode).get(0);
               }
               final JsonNode jsonNode = firstNode.get("@documentation");
               String documentation=noNode(jsonNode)? "-- no @documentation" : jsonNode.textValue();
               final JsonNode jsonNode2 = firstNode.get("@type");
               String type=noNode(jsonNode2)? "xsd:string" : jsonNode2.textValue();
               builder.addJavadoc("@param $N $L (expected type: $L)\n", key, documentation, type); 
           } else {           
               builder.addJavadoc("@param $N -- no bindings schemas \n", key); 
           }
       }
   }


public boolean noNode(final JsonNode jsonNode2) {
    return jsonNode2==null || jsonNode2 instanceof MissingNode || jsonNode2 instanceof NullNode;
}

   public MethodSpec generateClientMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
       final String loggerName = loggerName(template);
       MethodSpec.Builder builder = MethodSpec.methodBuilder(loggerName)
               .addModifiers(Modifier.PUBLIC)
               .returns(String.class)
      
               ;
       
       JsonNode the_var=bindings_schema.get("var");
       JsonNode the_context=bindings_schema.get("context");
       String var="sb"; 
       builder.addStatement("$T $N=new $T()", StringBuffer.class, var , StringBuffer.class);

       String args="" + var;
       
       Iterator<String> iter=the_var.fieldNames();
       while(iter.hasNext()){
           String key=iter.next();
           String newkey="__"+key;
           builder.addParameter(getJavaTypeForDeclaredType(the_var, key), newkey); 
           args=args + ", " + newkey; 
       }
       
       builder.addStatement("$N(" + args + ")",loggerName);
       builder.addStatement("return $N.toString()", var);

       MethodSpec method=builder.build();

       return method;
   }

   public MethodSpec generateClientMethod2(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder(loggerName(template))
               .addModifiers(Modifier.PUBLIC)
               .returns(void.class)
      
               ;
       String var="sb"; 
     
       JsonNode the_var=bindings_schema.get("var");
       JsonNode the_context=bindings_schema.get("context");

       builder.addParameter(StringBuffer.class, var);
       Iterator<String> iter=the_var.fieldNames();
       while(iter.hasNext()){
           String key=iter.next();
           String newkey="__"+key;
           builder.addParameter(getJavaTypeForDeclaredType(the_var, key), newkey); 
       }
       

                     
       iter=the_var.fieldNames();
       
       String constant="[\"" + template + "\"";
       while(iter.hasNext()){
           String key=iter.next();
           final String newName = "__"+key;
           final Class<?> clazz=getJavaTypeForDeclaredType(the_var, key);
           

           constant=constant+',';
           builder.addStatement("$N.append($S)",var,constant);
           constant="";

           if (String.class.equals(clazz)) {
               builder.beginControlFlow("if ($N==null)",newName)
                          .addStatement("$N.append($N)", var, newName)
                      .nextControlFlow("else")
                          .addStatement("$N.append($S)",var,"\"")
                          .addStatement("$N.append($N)", var, newName)
                          .addStatement("$N.append($S)",var,"\"")
                      .endControlFlow();
           } else {
               builder.addStatement("$N.append($S)",var,constant);
               builder.addStatement("$N.append($N)", var, newName);  
           }
       }
       constant=constant+']';
       builder.addStatement("$N.append($S)",var,constant);


       MethodSpec method=builder.build();

       return method;
   }
   
   public MethodSpec generateClientMethod3static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder("__getNodes")
               .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
               .returns(int[].class)
      
               ;
       String var="sb"; 
     
       JsonNode the_var=bindings_schema.get("var");
       JsonNode the_context=bindings_schema.get("context");

       Iterator<String> iter=the_var.fieldNames();
       
       int count=0;
       List<Integer> ll=new LinkedList<Integer>();
       while(iter.hasNext()){
           count++;
           String key=iter.next();
           if (the_var.get(key).get(0).get("@id")!=null) {
               ll.add(count);
           }
  
       }
       
       String nodes="";
       boolean first=true;
       for (int elem: ll) {
           if (first) {
               first=false;
           } else {
               nodes=nodes+", ";
           }
           nodes=nodes + elem;
       }

       builder.addStatement("return new int[] {" + nodes + "}");  

       
       
       
       MethodSpec method=builder.build();

       return method;
   }
     
   public MethodSpec generateClientMethod3(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder("getNodes")
               .addModifiers(Modifier.PUBLIC)
               .returns(int[].class)
      
               ;
       

       builder.addStatement("return __nodes");  

       
       
       
       MethodSpec method=builder.build();

       return method;
   }
   
   static final ParameterizedTypeName hashmapType = ParameterizedTypeName.get(ClassName.get(HashMap.class), TypeName.get(Integer.class), TypeName.get(int[].class));

   public MethodSpec generateClientMethod4(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder("getSuccessors")
               .addModifiers(Modifier.PUBLIC)
               .returns(hashmapType);
       
       builder.addStatement("return __successors");  
   
       MethodSpec method=builder.build();

       return method;
   }
   
   public MethodSpec generateClientMethod4static(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, JsonNode bindings_schema, IndexedDocument indexed) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder("__getSuccessors")
               .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
               .returns(hashmapType)
      
               ;
       String var="sb"; 
     
       JsonNode the_var=bindings_schema.get("var");
       JsonNode the_context=bindings_schema.get("context");
       
       
       Iterator<String> iter2=the_var.fieldNames();
       int count2=0;
       HashMap<QualifiedName,Integer> index=new HashMap<QualifiedName,Integer>();
       while(iter2.hasNext()){
           count2++;
           String key=iter2.next();
           for (QualifiedName qn: allVars) {
               if (key.equals(qn.getLocalPart())) {
                   index.put(qn,count2);
               }
           }
       }
       
       builder.addStatement("$T table = new $T()", hashmapType, hashmapType);  

       Iterator<String> iter=the_var.fieldNames();
       
       int count=0;
       
       while(iter.hasNext()){
           count++;
           String key=iter.next();
           if (the_var.get(key).get(0).get("@id")!=null) {


               Set<QualifiedName> successors=new HashSet<>();
               for (QualifiedName qn: allVars) {
                   if (key.equals(qn.getLocalPart())) {
                       successors=indexed.traverseDerivations(qn);
                       break;
                   }
               }
               String initializer="";
               boolean first=true;
               for (QualifiedName successor:successors) {
                   int i=index.get(successor);
                   if (first) {
                       first=false;
                   } else {
                       initializer=initializer + ", ";
                   }
                   initializer=initializer + i;
               }

               builder.addStatement("table.put($L,new int[] { " + initializer + "})", count);

           }
  
       }
       
       

       
       builder.addStatement("return table");  

       
       MethodSpec method=builder.build();

       return method;
   }

   public String loggerName(String template) {
       return "log" + gu.capitalize(template);

   }


public MethodSpec generateFactoryMethodWithArray(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, JsonNode bindings_schema) {
       MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
               .addModifiers(Modifier.PUBLIC)
               .returns(Document.class)
  
               ;
       
       JsonNode the_var=bindings_schema.get("var");
       JsonNode the_context=bindings_schema.get("context");
       
       builder.addParameter(Object[].class,"record");

       int count=1;
       Iterator<String> iter=the_var.fieldNames();
       String args="";
       while(iter.hasNext()){
           String key=iter.next();
           final Class<?> atype = getJavaTypeForDeclaredType(the_var, key);
           final String converter=getConverterForDeclaredType(atype);
           if (converter==null) {
               String statement = "$T $N=($T) record[" + count + "]";
               builder.addStatement(statement, atype,  key, atype);
           } else {
               String statement = "$T $N=$N(record[" + count + "])";
               builder.addStatement(statement, atype, key, converter);
           }
           if (count > 1) args=args + ", ";
           args=args+key;
           count++;
       }
       builder.addStatement("return make(" + args + ")");  

      
                      

 
       
       MethodSpec method=builder.build();
       
       return method;
   }

   public Class<?> getJavaTypeForDeclaredType(JsonNode the_var, String key) {
       if (the_var.get(key).get(0).get("@id")!=null) {
           return String.class;
       } else {
           if (the_var.get(key).get(0).get(0)==null) {
               System.out.println("key is " + key);
               System.out.println("decl is " + the_var);

               throw new UnsupportedOperationException();
           }
           JsonNode hasType=the_var.get(key).get(0).get(0).get("@type");
           if (hasType!=null) {
               String keyType=hasType.textValue();
               switch (keyType) {
                 case "xsd:int":
                   return Integer.class;
                 case "xsd:long":
                     return Long.class; 
                 case "xsd:string":
                   return String.class;
                 case "xsd:boolean":
                     return Boolean.class;
                 case "xsd:float":
                     return Float.class;
                 case "xsd:double":
                     return Double.class;
                 case "xsd:dateTime":
                     return String.class;
                 default:
                   throw new UnsupportedOperationException();
               }
           } else {
               System.out.println("key is " + key);
               System.out.println("decl is " + the_var);

               throw new UnsupportedOperationException();
           }
       }
   }
   
   public String getDeclaredType(JsonNode the_var, String key) {
       if (the_var.get(key).get(0).get("@id")!=null) {
           return "prov:QualifiedName";
       } else {
           if (the_var.get(key).get(0).get(0)==null) {
               System.out.println("key is " + key);
               System.out.println("decl is " + the_var);

               throw new UnsupportedOperationException();
           }
           JsonNode hasType=the_var.get(key).get(0).get(0).get("@type");
           if (hasType!=null) {
               String keyType=hasType.textValue();
               return keyType;
           } else {
               System.out.println("key is " + key);
               System.out.println("decl is " + the_var);

               throw new UnsupportedOperationException();
           }
       }
   }
   
   public MethodSpec generateMain(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, JsonNode bindings_schema) {

       MethodSpec.Builder builder = MethodSpec.methodBuilder("main")
               .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
               .returns(void.class)
               .addParameter(String[].class, "args")
               .addStatement("$T pf=org.openprovenance.prov.xml.ProvFactory.getFactory()",ProvFactory.class)
               .addStatement("$N me=new $N(pf)",name,name);

       ;
       for (QualifiedName q: allVars) {
           builder.addStatement("$T $N=pf.newQualifiedName($S,$S,$S)", QualifiedName.class, q.getLocalPart(), "http://example.org/",q.getLocalPart(), "ex");
       }
       
       JsonNode the_var2=(bindings_schema==null)?null:bindings_schema.get("var");
       
       for (QualifiedName q: allAtts) {
    	   String declaredType=null;
    	   if (the_var2!=null) {
    	       Iterator<String> iter=the_var2.fieldNames();
    	       
    	       while(iter.hasNext()){
    	           String key=iter.next();
	        	   if (q.getLocalPart().equals(key)) {
    	        	   declaredType=getDeclaredType(the_var2,key);
    	           }
    	       }
    	   }
	       String example=generateExampleForType(declaredType,q.getLocalPart());

           builder.addStatement("$T $N=$S", String.class, q.getLocalPart(), example);
       }
       
       String args="";
       boolean first=true;
       for (QualifiedName q: allVars) {
           if (first) {
               first=false;
               args=q.getLocalPart();
           } else {
               args=args + ", " + q.getLocalPart();
           } 
       }
       for (QualifiedName q: allAtts) {
           final String key = q.getLocalPart();
           if (first) {
               first=false;
               args=key;
           } else {
               args=args + ", " + key; 
           }
       }


       
       builder.addStatement("$T document=me.generator(" + args + ")", Document.class);
       builder.addStatement("new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document)"); //TODO make it load dynamically
            

       if (bindings_schema!=null) {
           JsonNode the_var=bindings_schema.get("var");

           Iterator<String> iter=the_var.fieldNames();
           args="";
           first=true;
           int count=0;
           while(iter.hasNext()){
               String key=iter.next();
               if (first) {
                   first=false;
                   args=createExamplar(the_var,key,count++);
               } else {
                   args=args + ", " +  createExamplar(the_var,key,count++);
               }
           }
           
           builder.addStatement("document=me.make(" + args + ")");
           builder.addStatement("new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document)");

           
       }
       
       
       MethodSpec method=builder.build();
       
       return method;
   }

   public  String generateExampleForType(String declaredType, String localPart) {
	   if (declaredType==null) {
		   return "test_" + localPart;
	   } else {
		   switch (declaredType) {
		     case "xsd:dateTime": return pFactory.newTimeNow().toXMLFormat();
		     case "xsd:float": return "123.00f";
		     case "xsd:int": return "12345";	
		     default: return "test_" + localPart;
		   }
	   }
   }


   public String createExamplar(JsonNode the_var, String key, int num) {
       if (the_var.get(key).get(0).get("@id")!=null) {
           return "\"v" + num + "\"";
       } else {
           if (the_var.get(key).get(0).get(0)==null) {
               System.out.println("key is " + key);
               System.out.println("decl is " + the_var);
               throw new UnsupportedOperationException();
           }
           JsonNode hasType=the_var.get(key).get(0).get(0).get("@type");
           if (hasType!=null) {
               String keyType=hasType.textValue();
               switch (keyType) {
                 case "xsd:int":
                   return "" + num;
                 case "xsd:long":
                     return "" + num + "L";
                 case "xsd:string":
                   return "\"v" + num + "\"";
                 case "xsd:boolean":
                   return "true";
                 case "xsd:float":
                     return "" + num + ".01f";
                 case "xsd:double":
                     return "" + num + ".01d";
                 case "xsd:dateTime":
                     return "\"" + pFactory.newTimeNow().toXMLFormat() + "\"";
                 default:
                   throw new UnsupportedOperationException();
               }
           } else {
               System.out.println("key is " + key);
               System.out.println("decl is " + the_var);

               throw new UnsupportedOperationException();
           }
       }
   }

    public JsonNode readTree(File file) throws IOException {
       return mapper.readTree(file);
    }
}
