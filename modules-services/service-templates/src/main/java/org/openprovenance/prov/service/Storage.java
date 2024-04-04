package org.openprovenance.prov.service;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Properties;

import static org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath;

public class Storage {
    public static final String SQL_FILE_NAME ;
    static Logger logger = LogManager.getLogger(Storage.class);

    public Connection setup(String host, String username, String password) {
        host="localhost"; // "localhost
        String url = "jdbc:postgresql://" + host + ":5432/templates?sslmode=disable";
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

    public static final String PROV_SUSTAINABILITY_WEBAPP_CONFIG_PROPERTIES = "/properties/prov-template-library.webapp.config.properties";

    static {
        Properties properties=getPropertiesFromClasspath(Storage.class, PROV_SUSTAINABILITY_WEBAPP_CONFIG_PROPERTIES);
        version=properties.getProperty("project.version");
        SQL_FILE_NAME= "/META-INF/resources/webjars/prov-template-library/" + version + "/sql/prov-template-library-plead.sql";
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
        ResultSet rs = st.executeQuery(statements);
        //displayResultSet(rs);
        return rs;
    }

    private void displayResultSet(ResultSet resultSet) throws SQLException {
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
