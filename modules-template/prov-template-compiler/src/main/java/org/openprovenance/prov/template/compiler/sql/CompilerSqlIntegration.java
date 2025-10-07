package org.openprovenance.prov.template.compiler.sql;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerSqlIntegration {
    private final CompilerUtil compilerUtil;
    static final ParameterizedTypeName SupplierOfString= ParameterizedTypeName.get(ClassName.get(java.util.function.Supplier.class), ClassName.get(String.class));

    public CompilerSqlIntegration(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
    }

    public SpecificationFile generateSqlIntegration_BeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* class with following code
        import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;

            import java.sql.ResultSet;
            import java.sql.SQLException;

            public class SqlBeanCompleter extends BeanCompleter {

                public SqlBeanCompleter(ResultSet rs) {
                    super(new Getter() {
                        @Override
                        public <T> T get(Class<T> cl, String col) {
                            try {
                                return rs.getObject(col,cl);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }

         */
        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_BEAN_COMPLETER);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER), Constants.BEAN_COMPLETER));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs");

        constructor.addStatement("super(new Getter() {\n" +
                "            @Override\n" +
                "            public <T> T get(Class<T> cl, String col) {\n" +
                "                try {\n" +
                "                    return rs.getObject(col,cl);\n" +
                "                } catch ($T e) {\n" +
                "                    e.printStackTrace();\n" +
                "                    throw new RuntimeException(e);\n" +
                "                }\n" +
                "            }\n" +
                "        })", SQLException.class);

        builder.addMethod(constructor.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* class with following code
              class SqlCompositeBeanCompleter extends org.openprovenance.prov.template.library.plead.sql.SqlBeanCompleter {

            private final ResultSet rs;

            public SqlCompositeBeanCompleter(ResultSet rs) {
                super(rs);
                this.rs=rs;
            }

            @Override
            public boolean next() {
                try {
                    return rs.next();  // ResultSet next() has already been called in generic_enact
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }



         */
        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_BEAN_COMPLETER);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER), Constants.SQL_BEAN_COMPLETER));

        // field
        FieldSpec.Builder field = FieldSpec.builder(ResultSet.class, "rs", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field.build());

        // constructor

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs");
        constructor.addStatement("super(rs)");
        constructor.addStatement("this.rs=rs");
        builder.addMethod(constructor.build());

        // method
        MethodSpec.Builder mspec = MethodSpec.methodBuilder("next")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(boolean.class);
        mspec.beginControlFlow("try");
        mspec.addStatement("return rs.next()");
        mspec.nextControlFlow("catch ($T e)", SQLException.class);
        mspec.addStatement("throw new RuntimeException(e)");
        mspec.endControlFlow();


        builder.addMethod(mspec.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the backend
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_BeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* class with following code
                    public class SqlBeanCompleter3 extends BeanCompleter2 {

                public SqlBeanCompleter3(ResultSet rs) {
                    super(newGetter(rs));
                }

                public static Getter newGetter(ResultSet rs) {
                    return new Getter() {
                        @Override
                        public <T> T get(Class<T> cl, String col) {
                            try {
                                return rs.getObject(col, cl);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    };
                }
            }
         */
        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_BEAN_COMPLETER3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addStatement("super(newGetter(rs))");
        builder.addMethod(constructor.build());

        // static method
        MethodSpec.Builder staticMethod = MethodSpec.methodBuilder("newGetter")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2, Constants.GETTER))
                .addParameter(ResultSet.class, "rs");


        staticMethod.addStatement("return new Getter() {\n" +
                "            @Override\n" +
                "            public <T> T get(Class<T> cl, String col) {\n" +
                "                try {\n" +
                "                    return rs.getObject(col,cl);\n" +
                "                } catch ($T e) {\n" +
                "                    e.printStackTrace();\n" +
                "                    throw new RuntimeException(e);\n" +
                "                }\n" +
                "            }\n" +
                "        }", SQLException.class);

        builder.addMethod(staticMethod.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }
    public SpecificationFile generateSqlIntegration_BeanCompleter4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* class with following code
                    public class SqlBeanCompleter4 extends SqlBeanCompleter3 {

                public SqlBeanCompleter4(ResultSet rs) {
                    super(rs);
                }
                public void postEnactmentProcessing(Integer id, String template) {
                    if (postProcessing != null) {
                        postProcessing.apply(id, template);
                    }
                }
                }
            }
         */
        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_BEAN_COMPLETER4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));

        // field
        FieldSpec.Builder field = FieldSpec.builder(BIFUN, CompilerCommon.POST_PROCESSING_VAR, Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addStatement("super(rs)")
                .addStatement("this.postProcessing=postProcessing");
        builder.addMethod(constructor.build());

        // method

        MethodSpec.Builder method = MethodSpec.methodBuilder("postEnactmentProcessing")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Integer.class, "id")
                .addParameter(String.class, "template");
        method.addStatement("if (postProcessing != null) {\n" +
                "            postProcessing.apply(id, template);\n" +
                "        }");
        builder.addMethod(method.build());



        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_EnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* class with following code

            @Override
            public <T> T generic_enact(T bean,
                                       Consumer<T> check,
                                       BiConsumer<StringBuilder, T> composeQuery,
                                       BiConsumer<ResultSet, T> completeBean) {
                check.accept(bean);
                StringBuilder sb = new StringBuilder();
                composeQuery.accept(sb, bean);

                String statement = sb.toString();
                ResultSet rs;
                try {
                    rs = getExecuteQuery(statement);
                    if (!rs.next()) {
                        rs.close();
                        throw new SQLException("Single row result was expected but result set is empty ");
                    }
                    completeBean.accept(rs, bean);
                    if (rs.next()) {
                        rs.close();
                        throw new SQLException("Single row result was expected for query ");
                    } else {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new UncheckedException("Issue in enactment " + statement, e);
                }
                return bean;
            }

                          @Override
              public BeanCompleter beanCompleterFactory(ResultSet rs) {
                return new SqlBeanCompleter(rs);
              }

            abstract public ResultSet getExecuteQuery(String statement) throws SQLException;

                     public static void printResultSet(ResultSet resultSet) {
                try {
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    do  {  // in generic_enact, we have already done next(), we are on the first record
                        System.out.print ("Row " + resultSet.getRow() + " ");
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = resultSet.getString(i);
                            System.out.print(columnValue + " " + rsmd.getColumnName(i));
                        }
                        System.out.println("");
                    } while (resultSet.next());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

         */
        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_ENACTOR_IMPLEMENTATION);
        builder.addModifiers(Modifier.PUBLIC);
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR), Constants.BEAN_ENACTOR, "EnactorImplementation"), TypeName.get(ResultSet.class)));

        ParameterizedTypeName querierType = ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class));
        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(querierType, "querier", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(querierType, "querier");
        constructor.addStatement("this.querier=querier");
        builder.addMethod(constructor.build());

        /*
        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(Storage.class, "storage", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        FieldSpec.Builder field2 = FieldSpec.builder(Connection.class, "conn", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field2.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Storage.class, "storage")
                .addParameter(Connection.class, "conn");

        constructor.addStatement("this.storage=storage");
        constructor.addStatement("this.conn=conn");
        builder.addMethod(constructor.build());
         */

        // method
        MethodSpec.Builder mspec = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addTypeVariable(TypeVariableName.get("T"))
                .addParameter(TypeVariableName.get("T"), "bean")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Consumer.class), TypeVariableName.get("T")), "check")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class), ClassName.get(StringBuilder.class), TypeVariableName.get("T")), "composeQuery")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class), TypeName.get(ResultSet.class), TypeVariableName.get("T")), "completeBean")
                .returns(TypeVariableName.get("T"));

        mspec.addStatement("check.accept(bean)");
        mspec.addStatement("$T sb = new $T()", StringBuilder.class, StringBuilder.class);
        mspec.addStatement("composeQuery.accept(sb, bean)");
        mspec.addStatement("String statement = sb.toString()");
        mspec.addStatement("$T rs", ResultSet.class);
        mspec.beginControlFlow("try");
        mspec.addStatement("rs = querier.apply(statement)");
        mspec.beginControlFlow("if (!rs.next())");
        mspec.addStatement("rs.close()");
        mspec.addStatement("throw new $T(\"Single row result was expected but result set is empty \")", SQLException.class);
        mspec.endControlFlow();
        mspec.addStatement("completeBean.accept(rs, bean)");
        mspec.beginControlFlow("if (rs.next())");
        mspec.addStatement("rs.close()");
        mspec.addStatement("throw new $T(\"Single row result was expected for query \")", SQLException.class);
        mspec.nextControlFlow("else");
        mspec.addStatement("rs.close()");
        mspec.endControlFlow();
        mspec.nextControlFlow("catch ($T e)", SQLException.class);
        mspec.addStatement("e.printStackTrace()");
        mspec.addStatement("throw new $T(\"Issue in enactment \" + statement, e)", UncheckedException.class);
        mspec.endControlFlow();
        mspec.addStatement("return bean");

        builder.addMethod(mspec.build());

        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER), Constants.BEAN_COMPLETER))
                .addParameter(ResultSet.class, "rs");

        method.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER), Constants.SQL_BEAN_COMPLETER));
        builder.addMethod(method.build());



        /*
        // abstract method
        MethodSpec.Builder abstractMethod = MethodSpec.methodBuilder("executeQuery")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ResultSet.class)
                .addException(SQLException.class)
                .addParameter(String.class, "statement");

        builder.addMethod(abstractMethod.build());

         */

        // static method
        MethodSpec.Builder staticMethod = MethodSpec.methodBuilder("printResultSet")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ResultSet.class, "resultSet");

        staticMethod.beginControlFlow("try");
        staticMethod.addStatement("$T rsmd = resultSet.getMetaData()", ResultSetMetaData.class);
        staticMethod.addStatement("int columnsNumber = rsmd.getColumnCount()");
        staticMethod.beginControlFlow("do");
        staticMethod.addStatement("// in generic_enact, we have already done next(), we are on the first record");
        staticMethod.addStatement("System.out.print (\"Row \" + resultSet.getRow() + \" \")");
        staticMethod.beginControlFlow("for (int i = 1; i <= columnsNumber; i++)");
        staticMethod.beginControlFlow("if (i > 1)");
        staticMethod.addStatement("System.out.print(\",  \")");
        staticMethod.endControlFlow();
        staticMethod.addStatement("String columnValue = resultSet.getString(i)");
        staticMethod.addStatement("System.out.print(columnValue + \" \" + rsmd.getColumnName(i))");
        staticMethod.endControlFlow();
        staticMethod.addStatement("System.out.println(\"\")");
        staticMethod.endControlFlow("while (resultSet.next())");
        staticMethod.nextControlFlow("catch ($T e)", SQLException.class);
        staticMethod.addStatement("throw new $T(e)", RuntimeException.class);
        staticMethod.endControlFlow();


        builder.addMethod(staticMethod.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_IntegratorEnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* class with following code

             @Override
    public <IN, OUT> OUT generic_enact(OUT output,
                                       IN bean,
                                       Consumer<IN> check,
                                       BiConsumer<StringBuilder, IN> composeQuery,
                                       BiConsumer<ResultSet, OUT> completeBean) {
        check.accept(bean);
        StringBuilder sb = new StringBuilder();
        composeQuery.accept(sb, bean);

        String statement = sb.toString();
        System.out.println("statement = " + statement);
        ResultSet rs;
        try {
            rs = executeQuery(statement);
            if (!rs.next()) {
                rs.close();
                throw new SQLException("Single row result was expected but result set is empty ");
            }
            completeBean.accept(rs,output);
            if (rs.next()) {
                rs.close();
                throw new SQLException("Single row result was expected for query ");
            } else {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UncheckedException("Issue in enactment " + statement, e);
        }
        return output;
    }
                          @Override
              public BeanCompleter2 beanCompleterFactory(ResultSet rs) {
                return new SqlBeanCompleter3(rs);
              }
                              @Override
                public BeanCompleter2 beanCompleterFactory(ResultSet rs, Object [] extra) {
                    return new  SqlBeanCompleter3(rs);
                }



         */
        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_ENACTOR_IMPLEMENTATION3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2), Constants.BEAN_ENACTOR2, "EnactorImplementation"), TypeName.get(ResultSet.class)));

        ParameterizedTypeName querierType = ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class));
        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(querierType, "querier", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(querierType, "querier");
        constructor.addStatement("this.querier=querier");
        builder.addMethod(constructor.build());

        /*
        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(Storage.class, "storage", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        FieldSpec.Builder field2 = FieldSpec.builder(Connection.class, "conn", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field2.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Storage.class, "storage")
                .addParameter(Connection.class, "conn");

        constructor.addStatement("this.storage=storage");
        constructor.addStatement("this.conn=conn");
        builder.addMethod(constructor.build());
         */

        // method
        MethodSpec.Builder mspec = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addTypeVariable(TypeVariableName.get("IN"))
                .addTypeVariable(TypeVariableName.get("OUT"))
                .addParameter(TypeVariableName.get("OUT"), "output")
                .addParameter(TypeVariableName.get("IN"), "bean")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Consumer.class), TypeVariableName.get("IN")), "check")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class), ClassName.get(StringBuilder.class), TypeVariableName.get("IN")), "composeQuery")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class), TypeName.get(ResultSet.class), TypeVariableName.get("OUT")), "completeBean")
                .returns(TypeVariableName.get("OUT"));


        mspec.addStatement("check.accept(bean)");
        mspec.addStatement("$T sb = new $T()", StringBuilder.class, StringBuilder.class);
        mspec.addStatement("composeQuery.accept(sb, bean)");
        mspec.addStatement("String statement = sb.toString()");
        if(false) mspec.addStatement("System.out.println(\"statement = \" + statement)");
        mspec.addStatement("$T rs", ResultSet.class);
        mspec.beginControlFlow("try");
        mspec.addStatement("rs = querier.apply(statement)");
        mspec.beginControlFlow("if (!rs.next())");
        mspec.addStatement("rs.close()");
        mspec.addStatement("throw new $T(\"Single row result was expected but result set is empty \")", SQLException.class);
        mspec.endControlFlow();
        mspec.addStatement("completeBean.accept(rs, output)");
        if(false) mspec.addStatement(" try {\n" +
                "            System.out.println(\"found =>\" + new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(output));\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }");

        mspec.beginControlFlow("if (rs.next())");
        if (false) mspec.addStatement("  java.sql.ResultSetMetaData rsmd = rs.getMetaData();\n" +
                "        int columnsNumber = rsmd.getColumnCount();\n" +
                "        StringBuilder sb2=new StringBuilder();\n" +
                "        for (int i = 1; i <= columnsNumber; i++) {\n" +
                "            if (i > 1) sb2.append(\",  \");\n" +
                "            String columnValue = rs.getString(i);\n" +
                "            sb2.append(columnValue).append(\" \").append(rsmd.getColumnName(i));\n" +
                "        }\n" +
                "        System.out.println(\"found =>\" + sb2.toString());");
        mspec.addStatement("rs.close()");

        mspec.addStatement("throw new $T(\"Single row result was expected for query \")", SQLException.class);
        mspec.nextControlFlow("else");
        mspec.addStatement("rs.close()");
        mspec.endControlFlow();
        mspec.nextControlFlow("catch ($T e)", SQLException.class);
        mspec.addStatement("e.printStackTrace()");
        mspec.addStatement("throw new $T(\"Issue in enactment \" + statement, e)", UncheckedException.class);
        mspec.endControlFlow();
        mspec.addStatement("return output");

        builder.addMethod(mspec.build());


        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs");

        method.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));
        builder.addMethod(method.build());

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra");

        method2.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));
        builder.addMethod(method2.build());

        /*
        MethodSpec.Builder method3 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addParameter(BIFUN, "postProcessing");

        method3.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));
        builder.addMethod(method3.build());

        MethodSpec.Builder method4 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(BIFUN, "postProcessing");

        method4.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));
        builder.addMethod(method4.build());
         */



        /*
        // abstract method
        MethodSpec.Builder abstractMethod = MethodSpec.methodBuilder("executeQuery")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ResultSet.class)
                .addException(SQLException.class)
                .addParameter(String.class, "statement");

        builder.addMethod(abstractMethod.build());

         */


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_IntegratorEnactorImplementation4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_ENACTOR_IMPLEMENTATION4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_ENACTOR_IMPLEMENTATION3), Constants.SQL_ENACTOR_IMPLEMENTATION3));
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2), Constants.BEAN_ENACTOR2, "EnactorImplementation"), TypeName.get(ResultSet.class)));
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2_WP), Constants.BEAN_ENACTOR2_WP, "EnactorImplementation"), TypeName.get(ResultSet.class)));

        ParameterizedTypeName querierType = ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class));
        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(querierType, "querier", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(querierType, "querier");
        constructor.addStatement("super(querier)");
        constructor.addStatement("this.querier=querier");
        builder.addMethod(constructor.build());

        /*
        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(Storage.class, "storage", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        FieldSpec.Builder field2 = FieldSpec.builder(Connection.class, "conn", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field2.build());

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Storage.class, "storage")
                .addParameter(Connection.class, "conn");

        constructor.addStatement("this.storage=storage");
        constructor.addStatement("this.conn=conn");
        builder.addMethod(constructor.build());
         */

        /*
        // method
        MethodSpec.Builder mspec = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addTypeVariable(TypeVariableName.get("IN"))
                .addTypeVariable(TypeVariableName.get("OUT"))
                .addParameter(TypeVariableName.get("OUT"), "output")
                .addParameter(TypeVariableName.get("IN"), "bean")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Consumer.class), TypeVariableName.get("IN")), "check")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class), ClassName.get(StringBuilder.class), TypeVariableName.get("IN")), "composeQuery")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class), TypeName.get(ResultSet.class), TypeVariableName.get("OUT")), "completeBean")
                .returns(TypeVariableName.get("OUT"));


        mspec.addStatement("check.accept(bean)");
        mspec.addStatement("$T sb = new $T()", StringBuilder.class, StringBuilder.class);
        mspec.addStatement("composeQuery.accept(sb, bean)");
        mspec.addStatement("String statement = sb.toString()");
        mspec.addStatement("$T rs", ResultSet.class);
        mspec.beginControlFlow("try");
        mspec.addStatement("rs = querier.apply(statement)");
        mspec.beginControlFlow("if (!rs.next())");
        mspec.addStatement("rs.close()");
        mspec.addStatement("throw new $T(\"Single row result was expected but result set is empty \")", SQLException.class);
        mspec.endControlFlow();
        mspec.addStatement("completeBean.accept(rs, output)");
        mspec.beginControlFlow("if (rs.next())");
        mspec.addStatement("  java.sql.ResultSetMetaData rsmd = rs.getMetaData();\n" +
                "        int columnsNumber = rsmd.getColumnCount();\n" +
                "        StringBuilder sb2=new StringBuilder();\n" +
                "        for (int i = 1; i <= columnsNumber; i++) {\n" +
                "            if (i > 1) sb2.append(\",  \");\n" +
                "            String columnValue = rs.getString(i);\n" +
                "            sb2.append(columnValue).append(\" \").append(rsmd.getColumnName(i));\n" +
                "        }\n" +
                "        System.out.println(\"found =>\" + sb2.toString());");
        mspec.addStatement("rs.close()");

        mspec.addStatement("throw new $T(\"Single row result was expected for query \")", SQLException.class);
        mspec.nextControlFlow("else");
        mspec.addStatement("rs.close()");
        mspec.endControlFlow();
        mspec.nextControlFlow("catch ($T e)", SQLException.class);
        mspec.addStatement("e.printStackTrace()");
        mspec.addStatement("throw new $T(\"Issue in enactment \" + statement, e)", UncheckedException.class);
        mspec.endControlFlow();
        mspec.addStatement("return output");

        builder.addMethod(mspec.build());


         */

        /*
        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs");

        method.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));
        builder.addMethod(method.build());

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra");

        method2.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3));
        builder.addMethod(method2.build());

         */

        MethodSpec.Builder method3 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR);

        method3.addStatement("return new $T(rs,$N)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER4), Constants.SQL_BEAN_COMPLETER4),CompilerCommon.POST_PROCESSING_VAR);
        builder.addMethod(method3.build());

        MethodSpec.Builder method4 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR);

        method4.addStatement("return new $T(rs,$N)", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER4), Constants.SQL_BEAN_COMPLETER4),CompilerCommon.POST_PROCESSING_VAR);
        builder.addMethod(method4.build());




        /*
        // abstract method
        MethodSpec.Builder abstractMethod = MethodSpec.methodBuilder("executeQuery")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ResultSet.class)
                .addException(SQLException.class)
                .addParameter(String.class, "statement");

        builder.addMethod(abstractMethod.build());

         */


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_CompositeEnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate
                public class SqlCompositeEnactorImplementation extends SqlEnactorImplementation {

            public SqlCompositeEnactorImplementation(Function<String,ResultSet> querier) {
                super(querier);
            }

            @Override
            public BeanCompleter beanCompleterFactory(ResultSet rs) {
                return new SqlCompositeBeanCompleter(rs);
            }
        }

         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_ENACTOR_IMPLEMENTATION), Constants.SQL_ENACTOR_IMPLEMENTATION));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(querier)");
        builder.addMethod(constructor.build());

        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER), Constants.BEAN_COMPLETER))
                .addParameter(ResultSet.class, "rs");
        method.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER), Constants.SQL_COMPOSITE_BEAN_COMPLETER));
        builder.addMethod(method.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_CompositeBeanEnactor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate
            public class SqlCompositeBeanEnactor extends BeanEnactor<ResultSet> {


                public SqlCompositeBeanEnactor(Function<String,ResultSet> querier) {
                    super(new  SqlCompositeEnactorImplementation(querier), new BeanChecker());
                }

            }



         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_BEAN_ENACTOR);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR), Constants.BEAN_ENACTOR), TypeName.get(ResultSet.class)));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(new  $T(querier), new $T())", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION), Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION), ClassName.get(locations.getFilePackage(Constants.BEAN_CHECKER), Constants.BEAN_CHECKER));
        builder.addMethod(constructor.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_BeanEnactor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate


            public class SqlBeanEnactor extends BeanEnactor<ResultSet> {

                public SqlBeanEnactor(Function<String,ResultSet> querier) {
                    super(new  SqlEnactorImplementation(querier), new BeanChecker());
                }



            }

         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_BEAN_ENACTOR);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR), Constants.BEAN_ENACTOR), TypeName.get(ResultSet.class)));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(new  $T(querier), new $T())", ClassName.get(locations.getFilePackage(Constants.SQL_ENACTOR_IMPLEMENTATION), Constants.SQL_ENACTOR_IMPLEMENTATION), ClassName.get(locations.getFilePackage(Constants.BEAN_CHECKER), Constants.BEAN_CHECKER));
        builder.addMethod(constructor.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_BeanEnactor3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate


            public class SqlBeanEnactor3 extends BeanEnactor2<ResultSet> {

                public SqlBeanEnactor3(Function<String,ResultSet> querier) {
                    super(new SqlCompositeEnactorImplementation3(querier), new BeanChecker2());
                }

            }

         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_BEAN_ENACTOR3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2), Constants.BEAN_ENACTOR2), TypeName.get(ResultSet.class)));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier")
                .addParameter(SupplierOfString,PRINCIPAL_MANAGER_VAR);
        constructor.addStatement("super(new  $T(querier), new $T())", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3), Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3), ClassName.get(locations.getFilePackage(Constants.BEAN_CHECKER2), Constants.BEAN_CHECKER2));
        builder.addMethod(constructor.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }
    public SpecificationFile generateSqlIntegration_BeanEnactor4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate


            public class SqlBeanEnactor3 extends BeanEnactor2<ResultSet> {

                public SqlBeanEnactor3(Function<String,ResultSet> querier) {
                    super(new SqlCompositeEnactorImplementation3(querier), new BeanChecker2());
                }

            }

         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_BEAN_ENACTOR4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2_WP), Constants.BEAN_ENACTOR2_WP), TypeName.get(ResultSet.class)));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addParameter(SupplierOfString,PRINCIPAL_MANAGER_VAR);
        constructor.addStatement("super(new  $T(querier), new $T(), $N, $N)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4), Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4), ClassName.get(locations.getFilePackage(Constants.BEAN_CHECKER2), Constants.BEAN_CHECKER2), CompilerCommon.POST_PROCESSING_VAR, PRINCIPAL_MANAGER_VAR);
        builder.addMethod(constructor.build());


        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);

    }

    /* define the method to generate the following class;
      public class SqlCompositeEnactorImplementation3 extends SqlEnactorImplementation3 {

            public SqlCompositeEnactorImplementation3(Function<String,ResultSet> querier) {
                super( querier);
            }

            @Override
            public BeanCompleter2 beanCompleterFactory(ResultSet rs) {
                return new SqlCompositeBeanCompleter3(rs);
            }

            @Override
            public BeanCompleter2 beanCompleterFactory(ResultSet rs, Object [] extra) {
                return new SqlCompositeBeanCompleter3(rs, extra);
            }
        }

     */
    public SpecificationFile generateSqlIntegration_CompositeEnactorImplementation3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate
                public class SqlCompositeEnactorImplementation3 extends SqlEnactorImplementation3 {

            public SqlCompositeEnactorImplementation3(Function<String,ResultSet> querier) {
                super( querier);
            }

            @Override
            public BeanCompleter2 beanCompleterFactory(ResultSet rs) {
                return new SqlCompositeBeanCompleter3(rs);
            }

            @Override
            public BeanCompleter2 beanCompleterFactory(ResultSet rs, Object [] extra) {
                return new SqlCompositeBeanCompleter3(rs, extra);
            }
        }

         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_ENACTOR_IMPLEMENTATION3), Constants.SQL_ENACTOR_IMPLEMENTATION3));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(querier)");
        builder.addMethod(constructor.build());

        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs");
        method.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER3), Constants.SQL_COMPOSITE_BEAN_COMPLETER3));
        builder.addMethod(method.build());

        // method
        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra");
        method2.addStatement("return new $T(rs, extra)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER3), Constants.SQL_COMPOSITE_BEAN_COMPLETER3));
        builder.addMethod(method2.build());

        /*
        // method
        MethodSpec.Builder method3 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addParameter(BIFUN, "postProcessing");
        method3.addStatement("return new $T(rs, extra,postProcessing)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER3), Constants.SQL_COMPOSITE_BEAN_COMPLETER3));
        builder.addMethod(method3.build());

        // method
        MethodSpec.Builder method4 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(BIFUN, "postProcessing");
        method4.addStatement("return new $T(rs, null,postProcessing)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER3), Constants.SQL_COMPOSITE_BEAN_COMPLETER3));
        builder.addMethod(method4.build());

         */

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }
    public SpecificationFile generateSqlIntegration_CompositeEnactorImplementation4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        /* should generate
                public class SqlCompositeEnactorImplementation3 extends SqlEnactorImplementation3 {

            public SqlCompositeEnactorImplementation3(Function<String,ResultSet> querier) {
                super( querier);
            }

            @Override
            public BeanCompleter2 beanCompleterFactory(ResultSet rs) {
                return new SqlCompositeBeanCompleter3(rs);
            }

            @Override
            public BeanCompleter2 beanCompleterFactory(ResultSet rs, Object [] extra) {
                return new SqlCompositeBeanCompleter3(rs, extra);
            }
        }

         */

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_ENACTOR_IMPLEMENTATION4), Constants.SQL_ENACTOR_IMPLEMENTATION4));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(querier)");
        builder.addMethod(constructor.build());

        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs");
        method.addStatement("return new $T(rs)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER4), Constants.SQL_COMPOSITE_BEAN_COMPLETER4));
        builder.addMethod(method.build());

        // method
        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra");
        method2.addStatement("return new $T(rs, extra)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER4), Constants.SQL_COMPOSITE_BEAN_COMPLETER4));
        builder.addMethod(method2.build());

        // method
        MethodSpec.Builder method3 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR);
        method3.addStatement("return new $T(rs, extra,postProcessing)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER4), Constants.SQL_COMPOSITE_BEAN_COMPLETER4));
        builder.addMethod(method3.build());

        // method
        MethodSpec.Builder method4 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2))
                .addParameter(ResultSet.class, "rs")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR);
        method4.addStatement("return new $T(rs,postProcessing)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER4), Constants.SQL_COMPOSITE_BEAN_COMPLETER4));
        builder.addMethod(method4.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }

    /* define the method to generate the following class;
                public class SqlCompositeBeanCompleter3 extends BeanCompleter3 {

                    public static final String PARENT_COLUMN = "parent";
                    private final Object[] extra;
                    private final ResultSet rs;

                    public SqlCompositeBeanCompleter3(ResultSet rs) {
                        this(rs,null);
                    }

                    public SqlCompositeBeanCompleter3(ResultSet rs, Object[] extra) {
                        super(newGetter(rs));
                        this.rs=rs;
                        this.extra=extra;
                    }

                    public Integer getValueFromLocation() {
                        return (Integer) extra[0];
                    }


                    @Override
                    public boolean next() {
                        try {
                            return rs.next();  // ResultSet next() has already been called in generic_enact
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    public void setValueInLocation() {
                        if (extra != null) {
                            int parent = getter.get(Integer.class, PARENT_COLUMN);
                            extra[0]= parent;
                        }
                    }

                }


     */

    public static ParameterizedTypeName BIFUN = ParameterizedTypeName.get(BiFunction.class, Integer.class, String.class, Object.class);

    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_BEAN_COMPLETER3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.BEAN_COMPLETER3), Constants.BEAN_COMPLETER3));

        // fields
        FieldSpec.Builder field1 = FieldSpec.builder(Object[].class, "extra", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field1.build());

        FieldSpec.Builder field2 = FieldSpec.builder(ResultSet.class, "rs", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field2.build());


        // constants
        FieldSpec.Builder field3 = FieldSpec.builder(String.class, "PARENT_COLUMN", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", "parent");
        builder.addField(field3.build());

        // constructor
        MethodSpec.Builder constructor1 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addStatement("this(rs,(Object[]) null)");
        builder.addMethod(constructor1.build());

        MethodSpec.Builder constructor2 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addStatement("super($T.newGetter(rs))", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_COMPLETER3), Constants.SQL_BEAN_COMPLETER3))
                .addStatement("this.rs=rs")
                .addStatement("this.extra=extra");
        builder.addMethod(constructor2.build());



        // method
        MethodSpec.Builder method = MethodSpec.methodBuilder("getValueFromLocation")
                .addModifiers(Modifier.PUBLIC)
                .returns(Integer.class)
                .addStatement("return (Integer) extra[0]");
        builder.addMethod(method.build());



        // method
        MethodSpec.Builder method2 = MethodSpec.methodBuilder("next")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(boolean.class);
        method2.beginControlFlow("try");
        method2.addStatement("return rs.next()");
        method2.nextControlFlow("catch ($T e)", SQLException.class);
        method2.addStatement("throw new $T(e)", RuntimeException.class);
        method2.endControlFlow();
        builder.addMethod(method2.build());

        // method
        MethodSpec.Builder method3 = MethodSpec.methodBuilder("setValueInLocation")
                .addModifiers(Modifier.PUBLIC);
        method3.beginControlFlow("if (extra != null)");
        method3.addStatement("int parent = getter.get(Integer.class, PARENT_COLUMN)");
        method3.addStatement("extra[0]= parent");
        method3.endControlFlow();
        builder.addMethod(method3.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }
    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_BEAN_COMPLETER4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_COMPLETER3), Constants.SQL_COMPOSITE_BEAN_COMPLETER3));

        // fields

        FieldSpec.Builder field4 = FieldSpec.builder(BIFUN, CompilerCommon.POST_PROCESSING_VAR, Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(field4.build());


        // constructor
        MethodSpec.Builder constructor1 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addStatement("this(rs,(Object[]) null)");
        builder.addMethod(constructor1.build());

        MethodSpec.Builder constructor2 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addStatement("super(rs,extra)")
                .addStatement("this.postProcessing=null");
        builder.addMethod(constructor2.build());

        MethodSpec.Builder constructor3 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addParameter(Object[].class, "extra")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addStatement("super(rs,extra)")
                .addStatement("this.postProcessing=postProcessing");
        builder.addMethod(constructor3.build());

        MethodSpec.Builder constructor4 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ResultSet.class, "rs")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addStatement("super(rs,null)")
                .addStatement("this.postProcessing=postProcessing");
        builder.addMethod(constructor4.build());


        //method
        MethodSpec.Builder postMethod = MethodSpec.methodBuilder(POST_PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Integer.class, "id")
                .addParameter(String.class, "template")
                .beginControlFlow("if (postProcessing != null)")
                .addStatement("postProcessing.apply(id, template)")
                .endControlFlow()
                .returns(void.class);
        builder.addMethod(postMethod.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }

    /* define the method to generate the following class;
                public class SqlCompositeBeanEnactor3 extends BeanEnactor2<ResultSet> {

                    public SqlCompositeBeanEnactor3(Function<String,ResultSet> querier) {
                        super(new SqlCompositeEnactorImplementation3(querier), new BeanChecker2());
                    }

                }

     */

    public SpecificationFile generateSqlIntegration_CompositeBeanEnactor3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_BEAN_ENACTOR3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2_COMPOSITE), Constants.BEAN_ENACTOR2_COMPOSITE), TypeName.get(ResultSet.class)));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(new  $T(querier), new $T())", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3), Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3), ClassName.get(locations.getFilePackage(Constants.BEAN_CHECKER2), Constants.BEAN_CHECKER2));
        builder.addMethod(constructor.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }


    public SpecificationFile generateSqlIntegration_CompositeBeanEnactor4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_BEAN_ENACTOR4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR2_COMPOSITE_WP), Constants.BEAN_ENACTOR2_COMPOSITE_WP), TypeName.get(ResultSet.class)));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addParameter(SupplierOfString,PRINCIPAL_MANAGER_VAR);
        constructor.addStatement("super(new  $T(querier), new $T(),$N, $N)", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4), Constants.SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4), ClassName.get(locations.getFilePackage(Constants.BEAN_CHECKER2), Constants.BEAN_CHECKER2), CompilerCommon.POST_PROCESSING_VAR, PRINCIPAL_MANAGER_VAR);
        builder.addMethod(constructor.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }


    /* define the method to generate the following class;
            public class SqlCompositeEnactorConfigurator3 extends CompositeEnactorConfigurator2 {

                public SqlCompositeEnactorConfigurator3(Function<String, ResultSet> querier) {
                    super(new SqlCompositeBeanEnactor3(querier));
                }

            }

     */

    public SpecificationFile generateSqlIntegration_CompositeEnactorConfigurator3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_ENACTOR_CONFIGURATOR3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.COMPOSITE_ENACTOR_CONFIGURATOR2), Constants.COMPOSITE_ENACTOR_CONFIGURATOR2));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier");
        constructor.addStatement("super(new  $T(querier))", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_ENACTOR3), Constants.SQL_COMPOSITE_BEAN_ENACTOR3));
        builder.addMethod(constructor.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }
    public SpecificationFile generateSqlIntegration_CompositeEnactorConfigurator4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_COMPOSITE_ENACTOR_CONFIGURATOR4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.COMPOSITE_ENACTOR_CONFIGURATOR2), Constants.COMPOSITE_ENACTOR_CONFIGURATOR2));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addParameter(SupplierOfString,PRINCIPAL_MANAGER_VAR);
        constructor.addStatement("super(new  $T($N,$N,$N))", ClassName.get(locations.getFilePackage(Constants.SQL_COMPOSITE_BEAN_ENACTOR4), Constants.SQL_COMPOSITE_BEAN_ENACTOR4), "querier", CompilerCommon.POST_PROCESSING_VAR, PRINCIPAL_MANAGER_VAR);
        builder.addMethod(constructor.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }

    /* define the method to generate the following class;

        public class SqlEnactorConfigurator3 extends EnactorConfigurator2 {
            public SqlEnactorConfigurator3(Function<String, ResultSet> querier) {
                super(new SqlBeanEnactor3(querier));
            }

        }

     */

    public SpecificationFile generateSqlIntegration_EnactorConfigurator3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_ENACTOR_CONFIGURATOR3);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.ENACTOR_CONFIGURATOR2), Constants.ENACTOR_CONFIGURATOR2));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier")
                .addParameter(SupplierOfString,PRINCIPAL_MANAGER_VAR);
        constructor.addStatement("super(new  $T(querier,$N))", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_ENACTOR3), Constants.SQL_BEAN_ENACTOR3), PRINCIPAL_MANAGER_VAR);
        builder.addMethod(constructor.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }

    public SpecificationFile generateSqlIntegration_EnactorConfigurator4(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.SQL_ENACTOR_CONFIGURATOR4);
        builder.addModifiers(Modifier.PUBLIC);
        builder.superclass(ClassName.get(locations.getFilePackage(Constants.ENACTOR_CONFIGURATOR2), Constants.ENACTOR_CONFIGURATOR2));

        // constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(String.class), ClassName.get(ResultSet.class)), "querier")
                .addParameter(BIFUN, CompilerCommon.POST_PROCESSING_VAR)
                .addParameter(SupplierOfString,PRINCIPAL_MANAGER_VAR);
        constructor.addStatement("super(new  $T($N,$N,$N))", ClassName.get(locations.getFilePackage(Constants.SQL_BEAN_ENACTOR4), Constants.SQL_BEAN_ENACTOR4), "querier", CompilerCommon.POST_PROCESSING_VAR, PRINCIPAL_MANAGER_VAR);
        builder.addMethod(constructor.build());

        TypeSpec theClass = builder.build();

        String myPackage = locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName + DOT_JAVA_EXTENSION, myPackage);
    }


}



