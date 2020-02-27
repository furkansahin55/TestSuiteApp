package com.hhit.ciapp.tests;

import com.hhit.ciapp.classes.RequestBuilder;
import com.hhit.ciapp.classes.Test;
import com.hhit.ciapp.classes.TestException;
import com.hhit.ciapp.models.Status;
import com.hhit.ciapp.models.TestResult;
import org.json.JSONObject;

import java.io.IOException;

public class ExceptionTest implements Test {

    @Override
    public Integer getPriorityOrder() {
        return 1;
    }

    public TestResult runTest() throws TestException {

        RequestBuilder request = new RequestBuilder("https://oaf8593py2.execute-api.us-east-2.amazonaws.com/MIG/api/analytics/numbers/countByTypeAndStatus", "POST", "{}");

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

        //Escaped from catch means test passed
        //Set testResult status property
        testResult.setStatus(Status.PASSED);

        return testResult;
    }

}
