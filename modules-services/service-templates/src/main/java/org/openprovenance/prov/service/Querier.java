package org.openprovenance.prov.service;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.inface.BiConsumerWithException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Querier  {
    final private static Logger logger = LogManager.getLogger(Querier.class);

    final private Storage storage;
    final private Connection conn;

    public Querier(Storage storage, Connection conn) {
        this.storage = storage;
        this.conn = conn;
    }

    public <T> T do_query(T data,
                          Consumer<T> check,
                          BiConsumer<StringBuilder,T> composeQuery,
                          BiConsumerWithException<ResultSet,T> completeData) {
        if (check!=null) check.accept(data);
        StringBuilder sb=new StringBuilder();
        composeQuery.accept(sb,data);
        String statement=sb.toString();
        logger.info("Executing statement: " + statement);
        ResultSet rs;
        try {
            rs=storage.executeQuery(conn,statement);
            completeData.accept(rs, data);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new org.openprovenance.prov.model.exception.UncheckedException("Issue in enactment " + statement, e);
        }
        return data;
    }

    public <T> boolean do_statements(T data,
                                     Consumer<T> check,
                                     BiConsumer<StringBuilder,T> composeQuery) {
        if (check!=null) check.accept(data);
        StringBuilder sb=new StringBuilder();
        composeQuery.accept(sb,data);
        String statement=sb.toString();
        boolean rs;
        try {
            rs=storage.executeStatements(conn,statement);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new org.openprovenance.prov.model.exception.UncheckedException("Issue in enactment " + statement, e);
        }
        return rs;
    }



    public long storeBlobs(InputStream descriptorStream, InputStream binaryStream, String mediaType, String name, String insertStatement) throws SQLException, IOException {
        try (descriptorStream; binaryStream) {
            PreparedStatement statement = conn.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);

            statement.setBinaryStream(1, descriptorStream);
            statement.setBinaryStream(2, binaryStream);
            statement.setString(3, mediaType);
            statement.setString(4, name);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating resource_data, no rows affected.");
            }

            long id;

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            } finally {
                statement.close();
            }
            return id;
        }
    }

    public Triple<InputStream,String,String> getBlobs3(long id, String query) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    InputStream binaryStream = rs.getBinaryStream(1);
                    String mediaType = rs.getString(2);
                    String name = rs.getString(3);
                    return Triple.of(binaryStream, mediaType, name);
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public InputStream getBlobs(long id, String query) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    InputStream binaryStream = rs.getBinaryStream(1);
                    return binaryStream;
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
