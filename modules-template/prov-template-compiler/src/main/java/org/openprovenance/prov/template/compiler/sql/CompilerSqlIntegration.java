package org.openprovenance.prov.template.compiler.sql;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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


    public SpecificationFile generateSqlIntegration_EnactorImplementation(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

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
        builder.addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT);
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(Constants.BEAN_ENACTOR), Constants.BEAN_ENACTOR, "EnactorImplementation"), TypeName.get(ResultSet.class)));

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
        mspec.addStatement("rs = executeQuery(statement)");
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

        // abstract method
        MethodSpec.Builder abstractMethod = MethodSpec.methodBuilder("executeQuery")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ResultSet.class)
                .addException(SQLException.class)
                .addParameter(String.class, "statement");

        builder.addMethod(abstractMethod.build());

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


        TypeSpec theClass= builder.build();

        String myPackage= locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theClass, configs, myPackage, stackTraceElement);

        // note, this goes to the back
        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }

}