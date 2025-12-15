#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.storage;

import org.openprovenance.prov.service.core.Storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.openprovenance.prov.service.core.Storage.displayResultSet;

abstract public class StorageTest extends junit.framework.TestCase {

    String statement0="select * from insert_plead_transforming_composite_and_linker (ARRAY[${symbol_escape}n" +
            "( -1,'ddd'::TEXT,5,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type,${symbol_escape}n" +
            "     ( -1,'ddd'::TEXT,6,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type]);${symbol_escape}n";

    String statement1="WITH ${symbol_escape}n" +
            "    insertion_result AS (select * from insert_plead_transforming_composite_and_linker (ARRAY[${symbol_escape}n" +
            "( -1,'ddd'::TEXT,5,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type,${symbol_escape}n" +
            "     ( -1,'ddd'::TEXT,6,null,null,null,-2,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type])),${symbol_escape}n" +
            "insertion_result2 AS (${symbol_escape}n" +
            "   INSERT INTO record_index(key,table_name,principal)${symbol_escape}n" +
            "   SELECT id,'plead_transforming'::TEXT,'lavm'::TEXT${symbol_escape}n" +
            "   FROM insertion_result${symbol_escape}n" +
            "   returning *),${symbol_escape}n" +
            "insertion_result3 AS (${symbol_escape}n" +
            "   INSERT INTO record_index(key,table_name,principal)${symbol_escape}n" +
            "   SELECT parent as key,'plead_transforming_composite'::TEXT,'lavm'::TEXT${symbol_escape}n" +
            "   from insertion_result)${symbol_escape}n" +
            "select * from insertion_result${symbol_escape}n" +
            ";${symbol_escape}n";


    String statement2=" WITH ${symbol_escape}n" +
            "    insertion_result AS (select * from insert_plead_transforming ('dd'::TEXT,2,null,null,null,null,null,''::TEXT,NULL,NULL,NULL))${symbol_escape}n" +
            "INSERT INTO record_index(key,table_name,principal)${symbol_escape}n" +
            "VALUES ((SELECT id FROM insertion_result),${symbol_escape}n" +
            "'plead_transforming'::TEXT,${symbol_escape}n" +
            "'lavm'::TEXT)${symbol_escape}n" +
            "RETURNING (SELECT ID FROM insertion_result) as id${symbol_escape}n" +
            ",(SELECT transformed_file FROM insertion_result),(SELECT transforming FROM insertion_result);${symbol_escape}n";

    public void testExample () throws SQLException {

        Storage storage = new Storage();

        Connection conn=storage.setup("localhost", "luc", "admin");


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
