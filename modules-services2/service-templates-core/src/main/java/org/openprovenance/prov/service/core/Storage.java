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
    static Logger logger = LogManager.getLogger(Storage.class);

    public Connection setup(String url, String username, String password) {
        //host="localhost"; // "localhost
        //String url = "jdbc:postgresql://" + host + ":5432/bookptm?sslmode=disable";
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");
        logger.info("DB url: " + url);
        logger.info("DB props: " + props);
        try {
            Class.forName("org.postgresql.Driver");  // added this, since https://stackoverflow.com/questions/1911253/the-infamous-java-sql-sqlexception-no-suitable-driver-found
            Connection conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getStringFromClasspath(Class<?> clazz, String propFileName) {
        try {
            return IOUtils.resourceToString(propFileName, Charset.defaultCharset(), clazz.getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean initializeDB(Connection conn, String sqlInitializer) throws SQLException {
        String statements=getStringFromClasspath(this.getClass(), sqlInitializer);
        logger.info("*** Initializing DB with script: " + sqlInitializer);
       // System.out.println(statements);
        return executeStatements(conn, statements);
    }

    public boolean executeStatements(Connection conn, String statements) throws SQLException {
        try (Statement st = conn.createStatement()) {
            logger.info("Executing statements: " + statements);
            return st.execute(statements);
        }
    }


    public ResultSet executeQuery(Connection conn, String statements) throws SQLException {
        Statement st = conn.createStatement();
        logger.info("Executing query: " + statements);
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
