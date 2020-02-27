package com.hhit.ciapp.classes;

import com.hhit.ciapp.models.DataValidationAssertion;
import com.hhit.ciapp.models.DataValidationTests;
import com.hhit.ciapp.models.Status;
import com.hhit.ciapp.models.TestResult;
import org.json.JSONArray;
import org.json.JSONObject;
import ro.skyah.comparator.JSONCompare;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DataValidationTest implements Test {

    //Request Builder
    private RequestBuilder request;

    private List<DataValidationAssertion> assertions;

    private String tableName;

    private String JSONArrayKey;


    public DataValidationTest(DataValidationTests testProperties) {
        //Create request object
        this.request = new RequestBuilder(testProperties.getTestUrl(), testProperties.getTestRequestMethod(), testProperties.getTestRequestBody());
        //Assign the table name
        this.tableName = testProperties.getTestOracleTable();
        //Assign the assertions
        this.assertions = testProperties.getDataAssert();
        //Assign the array key
        this.JSONArrayKey = testProperties.getTestJSONArrayKey();
    }

    @Override
    public Integer getPriorityOrder() {
        return null;
    }


    public TestResult runTest() throws TestException {

        //Create TestResult object
        TestResult testResult = new TestResult();

        //Make request to get JSON
        JSONObject responseJSON = null;
        try {
            responseJSON = request.makeRequest();
        } catch (IOException e) {
            throw new TestException(testResult, e.toString(), 1);

        }

        //Escapes from catch means request is OK.
        //Set the responseJSON property
        testResult.setResponseJSON(responseJSON.toString());

        //Make sql query to get specific columns from table
        JSONArray target;
        try {
            //Connect to DB
            OracleDatabase oracleDB = new OracleDatabase();
            //Get tables and store
            ResultSet dbColumns = oracleDB.getAllByTableName(tableName,assertions);
            System.out.println(dbColumns.getObject(1));

            //Map result set to JSON Array
            target = JSONUtils.mapResultSet(dbColumns);
            //Close the DB connection
            oracleDB.close();
        } catch (Error | SQLException e){
            throw new TestException(testResult, e.toString(), 1);
        }


        //Compare request json with DB Columns
        try {
            //Get relevant JSON Array
            JSONArray source = responseJSON.getJSONArray(JSONArrayKey);
            //Compare Jsons
            JSONCompare.assertEquals(target.toString(), source.toString());

        } catch (AssertionError e){
            throw new TestException(testResult, e.toString(), 1);
        }

        //Escaped from catch means test passed
        //Set testResult status property
        testResult.setStatus(Status.PASSED);

        return testResult;
    }

}
