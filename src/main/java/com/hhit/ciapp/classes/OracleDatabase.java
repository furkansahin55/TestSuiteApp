package com.hhit.ciapp.classes;

import com.hhit.ciapp.models.DataValidationAssertion;

import java.sql.*;
import java.util.List;


public class OracleDatabase {

    Connection con;

    public OracleDatabase() throws SQLException {
        //Initialize the connection
        //Create  the connection object
        con = DriverManager.getConnection("jdbc:oracle:thin:@ora-db-nmt-dis-3.c9ot2ck0lzi6.us-east-2.rds.amazonaws.com:1521:ORCL", "nmtsbdis", "!d1sllpt3lstr4");

    }

    public ResultSet getAllByTableName(String tableName, List<DataValidationAssertion> assertions) throws SQLException {

        //Create the statement object
        Statement stmt = con.createStatement();

        //Prepare query for as declaration
        String sql = "";
        for (DataValidationAssertion assertion:assertions) {
            sql +=assertion.getDbColumnName()+ " AS \"" +assertion.getJsonKey() + "\", ";
        }
        //Delete the last comma
        sql = sql.substring(0, sql.length() - 2);
        //Complete the query
        sql += " FROM "+tableName;

        //Execute query
        ResultSet rs = stmt.executeQuery("SELECT "+ sql);

        rs.next();

        return rs;

    }

    public void close() throws SQLException {
        //Close the connection object
        con.close();
    }

}