package org.openprovenance.prov.service.core;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Properties;
import java.util.function.Function;

import static org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath;

public class Storage {
    public static final String SQL_FILE_NAME ;
    static Logger logger = LogManager.getLogger(Storage.class);

    public Connection setup(String host, String username, String password) {
        //host="localhost"; // "localhost
        String url = "jdbc:postgresql://" + host + ":5432/bookptm?sslmode=disable";
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");
        System.out.println("DB url: " + url);
        System.out.println("DB props: " + props);
        try {
            Class.forName("org.postgresql.Driver");  // added this, since https://stackoverflow.com/questions/1911253/the-infamous-java-sql-sqlexception-no-suitable-driver-found
            Connection conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
    static final String version;

    public static final String PROV_SUSTAINABILITY_WEBAPP_CONFIG_PROPERTIES = "/properties/the-template-library.webapp.config.properties";

    static {
        Properties properties=getPropertiesFromClasspath(Storage.class, PROV_SUSTAINABILITY_WEBAPP_CONFIG_PROPERTIES);
        System.out.println(Storage.class.getClassLoader().getResource(PROV_SUSTAINABILITY_WEBAPP_CONFIG_PROPERTIES));
        System.out.println("DB properties: " + properties);
        if (properties==null) {
            // load properties from default absolute file path
            properties = new Properties();
            try {
                properties.load(new FileInputStream("/Users/luc/git-papers/papers/book-ptm/project/service-book/target/classes/properties/the-template-library.webapp.config.properties"));
                System.out.println(properties);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        version=properties.getProperty("project.version");
        SQL_FILE_NAME= "/META-INF/resources/webjars/template-intro1/" + version + "/sql/prov-template-library-transport.sql";
        logger.info("SQL file: " + SQL_FILE_NAME);
    }


    public static String getStringFromClasspath(Class<?> clazz, String propFileName) {
        try {
            return IOUtils.resourceToString(propFileName, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean initializeDB(Connection conn) throws SQLException {
        String statements=getStringFromClasspath(this.getClass(), SQL_FILE_NAME);
        return executeStatements(conn, statements);
    }

    public boolean executeStatements(Connection conn, String statements) throws SQLException {
        try (Statement st = conn.createStatement()) {
            return st.execute(statements);
        }
    }

    public void executeQuery(Connection conn) throws SQLException {
        String statements=getStringFromClasspath(this.getClass(), SQL_FILE_NAME);
        executeQuery(conn, statements);
    }

    public ResultSet executeQuery(Connection conn, String statements) throws SQLException {
        Statement st = conn.createStatement();
        //logger.info("Executing query: " + statements);
        ResultSet rs = st.executeQuery(statements);
        //displayResultSet(rs);
        return rs;
    }

    public Function<String,ResultSet> queryExecutor(Connection conn) {
        return (String statement) -> {
            //logger.info("Executing query: " + statement);
            try {
                return executeQuery(conn, statement);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
    }

    static public void displayResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        StringBuilder sb=new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) sb.append(",  ");
                String columnValue = resultSet.getString(i);
                sb.append(columnValue).append(" ").append(rsmd.getColumnName(i));
            }
            sb.append("\n");
        }
        logger.info(sb.toString());
        System.out.println(sb.toString());

    }

    public void isSingleRow(ResultSet rs) throws SQLException {
        int rowcount=0;
        if (rs.last()) {
            rowcount = rs.getRow();
            rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }
        if (rowcount!=1) throw new SQLException("Single row result was expected for query " );
    }
}
