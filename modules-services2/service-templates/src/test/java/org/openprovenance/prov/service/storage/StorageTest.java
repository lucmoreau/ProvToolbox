package org.openprovenance.prov.service.storage;

import org.openprovenance.prov.template.service.Storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.openprovenance.prov.template.service.Storage.displayResultSet;

public class StorageTest extends junit.framework.TestCase {

    String statement0="select * from insert_plead_transforming_composite_and_linker (ARRAY[\n" +
            "( -1,'ddd'::TEXT,5,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type,\n" +
            "     ( -1,'ddd'::TEXT,6,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type]);\n";

    String statement1="WITH \n" +
            "    insertion_result AS (select * from insert_plead_transforming_composite_and_linker (ARRAY[\n" +
            "( -1,'ddd'::TEXT,5,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type,\n" +
            "     ( -1,'ddd'::TEXT,6,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type])),\n" +
            "insertion_result2 AS (\n" +
            "   INSERT INTO record_index(key,table_name,principal)\n" +
            "   SELECT id,'plead_transforming'::TEXT,'lavm'::TEXT\n" +
            "   FROM insertion_result\n" +
            "   returning *),\n" +
            "insertion_result3 AS (\n" +
            "   INSERT INTO record_index(key,table_name,principal)\n" +
            "   SELECT parent as key,'plead_transforming_composite'::TEXT,'lavm'::TEXT\n" +
            "   from insertion_result)\n" +
            "select * from insertion_result\n" +
            ";\n";


    String statement2=" WITH \n" +
            "    insertion_result AS (select * from insert_plead_transforming ('dd'::TEXT,2,null,null,null,null,null,''::TEXT,NULL,NULL,NULL))\n" +
            "INSERT INTO record_index(key,table_name,principal)\n" +
            "VALUES ((SELECT id FROM insertion_result),\n" +
            "'plead_transforming'::TEXT,\n" +
            "'lavm'::TEXT)\n" +
            "RETURNING (SELECT ID FROM insertion_result) as id\n" +
            ",(SELECT transformed_file FROM insertion_result),(SELECT transforming FROM insertion_result);\n";

    public void testExample () throws SQLException {

        Storage storage = new Storage();

        Connection conn=storage.setup("jdbc:postgresql://localhost:5432/templates?sslmode=disable", "luc", "admin");


        System.out.println("<<<<<");
        ResultSet rs0=storage.executeQuery(conn, statement0);
        displayResultSet(rs0);
        System.out.println(">>>>>");

        System.out.println("<<<<<");
        ResultSet rs1=storage.executeQuery(conn, statement1);
        displayResultSet(rs1);
        System.out.println(">>>>>");

        System.out.println("<<<<<");
        ResultSet rs2=storage.executeQuery(conn, statement2);
        displayResultSet(rs2);
        System.out.println(">>>>>");




    }
}
