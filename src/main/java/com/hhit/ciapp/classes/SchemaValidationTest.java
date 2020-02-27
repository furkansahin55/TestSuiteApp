package com.hhit.ciapp.classes;

import com.hhit.ciapp.models.SchemaValidationTests;
import com.hhit.ciapp.models.Status;
import com.hhit.ciapp.models.TestResult;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class SchemaValidationTest implements Test {

    private RequestBuilder request;

    private String validationSchemaAsString;


    public SchemaValidationTest(SchemaValidationTests testProperties) {
        //Convert String to JSON object
        this.validationSchemaAsString = testProperties.getTestSchema();
        //Create request object
        this.request = new RequestBuilder(testProperties.getTestUrl(), testProperties.getTestRequestMethod(), testProperties.getTestRequestBody());
    }

    @Override
    public Integer getPriorityOrder() {
        return null;
    }

    public TestResult runTest() throws TestException {

        //Create TestResult objects
        TestResult testResult = new TestResult();

        //Try to convert String to JSON Object
        JSONObject validationSchema = null;
        try {
            validationSchema = new JSONObject(validationSchemaAsString);
        } catch (Exception e) {
            throw new TestException(testResult, "Given Request body is not a valid JSON Object : " + e.toString(), 1);
        }

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

        //Create schema with given properties
        SchemaLoader loader = SchemaLoader.builder()
                .schemaJson(validationSchema)
                .draftV7Support()
                .build();
        Schema schema = loader.load().build();

        //Validate responseJSON
        try {
            schema.validate(responseJSON);
        } catch (ValidationException e) {
            //Get all error messages
            List<String> errors = e.getAllMessages();
            throw new TestException(testResult, String.join("\n", errors), errors.size());
        }

        //Escaped from catch means test passed
        //Set testResult status property
        testResult.setStatus(Status.PASSED);

        return testResult;
    }

}
