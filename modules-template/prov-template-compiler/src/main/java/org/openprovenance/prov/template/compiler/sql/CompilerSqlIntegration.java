package org.openprovenance.prov.template.compiler.sql;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerSqlIntegration {
    private final CompilerUtil compilerUtil;

    public CompilerSqlIntegration(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateSqlIntegration_BeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

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



        TypeSpec theClass= builder.build();

        String myPackage= locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_CompositeBeanCompleter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

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



        TypeSpec theClass= builder.build();

        String myPackage= locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the backend
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }

    public SpecificationFile generateSqlIntegration_BeanCompleter3(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

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



        TypeSpec theClass= builder.build();

        String myPackage= locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}