package com.hhit.ciapp.controllers;

import com.hhit.ciapp.models.TestResult;
import com.hhit.ciapp.repositories.TestResultRepository;
import com.hhit.ciapp.services.AsyncTestService;
import com.hhit.ciapp.services.ReflectionsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestAPIController {

    private TestResultRepository testResultRepository;
    private AsyncTestService asyncTestService;
    private ReflectionsService reflectionsService;

    @Autowired
    public TestAPIController(TestResultRepository testResultRepository, AsyncTestService asyncTestService, ReflectionsService reflectionsService) {
        this.testResultRepository = testResultRepository;
        this.asyncTestService = asyncTestService;
        this.reflectionsService = reflectionsService;
    }

    @RequestMapping(value = {"/debug"}, method = RequestMethod.GET)
    @ResponseBody
    public TestResult debug() {
        return null;
    }

    @RequestMapping(value = {"/get-test-results"}, method = RequestMethod.GET)
    @ResponseBody
    public List<TestResult> getTestResults() {
        return testResultRepository.findAllByTestBatchId(asyncTestService.getTestBatch().getId());
    }


    @RequestMapping(value = {"/is-test-running"}, method = RequestMethod.GET)
    @ResponseBody
    public String isTestRunning() {
        JSONObject result = new JSONObject();
        result.put("value", asyncTestService.getIsTestRunning());
        return result.toString();
    }


    @RequestMapping(value = {"/get-total-test-count"}, method = RequestMethod.GET)
    @ResponseBody
    public String getTotalTestCount() {
        JSONObject result = new JSONObject();
        result.put("value", asyncTestService.getTotalTestCount());
        return result.toString();
    }


    @RequestMapping(value = {"/get-executed-test-count"}, method = RequestMethod.GET)
    @ResponseBody
    public String getExecuteTestCount() {
        JSONObject result = new JSONObject();
        result.put("value", asyncTestService.getExecutedTestCount());
        return result.toString();
    }

    @RequestMapping(value = {"/get-progress-bar"}, method = RequestMethod.GET)
    @ResponseBody
    public String getProgressBar() {
        int bar = (100 / asyncTestService.getTotalTestCount()) * asyncTestService.getExecutedTestCount();
        //Set bar 100 if all tests finished
        if (asyncTestService.getTotalTestCount().equals(asyncTestService.getExecutedTestCount())) bar = 100;
        JSONObject result = new JSONObject();
        result.put("value", bar);
        return result.toString();
    }

}
