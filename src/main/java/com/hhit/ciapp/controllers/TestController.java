package com.hhit.ciapp.controllers;

import com.hhit.ciapp.classes.JSONUtils;
import com.hhit.ciapp.models.*;
import com.hhit.ciapp.repositories.DataValidationTestsRepository;
import com.hhit.ciapp.repositories.SchemaValidationTestsRepository;
import com.hhit.ciapp.repositories.TestBatchRepository;
import com.hhit.ciapp.repositories.TestResultRepository;
import com.hhit.ciapp.services.AsyncTestService;
import com.hhit.ciapp.services.SessionService;
import com.hhit.ciapp.services.TestCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TestController {

    private SessionService sessionService;

    private AsyncTestService asyncTestService;

    private TestBatchRepository testBatchRepository;

    private TestResultRepository testResultRepository;

    private SchemaValidationTestsRepository schemaValidationTestsRepository;

    private DataValidationTestsRepository dataValidationTestsRepository;

    private TestCRUDService testCRUDService;


    @Autowired
    public TestController(SessionService sessionService, AsyncTestService asyncTestService, TestBatchRepository testBatchRepository, TestResultRepository testResultRepository, SchemaValidationTestsRepository schemaValidationTestsRepository, DataValidationTestsRepository dataValidationTestsRepository, TestCRUDService testCRUDService) {
        this.sessionService = sessionService;
        this.asyncTestService = asyncTestService;
        this.testBatchRepository = testBatchRepository;
        this.testResultRepository = testResultRepository;
        this.schemaValidationTestsRepository = schemaValidationTestsRepository;
        this.dataValidationTestsRepository = dataValidationTestsRepository;
        this.testCRUDService = testCRUDService;
    }

    @RequestMapping(value = {"/select-test"}, method = RequestMethod.GET)
    public String selectTest(HttpSession session, Model model) {
        //If session not valid redirect to login
        if (!sessionService.validateSession(session, model)) return "login";

        //If a test is running at that time redirect to /live-test
        if (asyncTestService.getIsTestRunning()) return "redirect:/live-test";

        //Add attribute and show HTMLs
        List<TestModel> allTests = testCRUDService.getAllTests();


        model.addAttribute("allTests", allTests);
        model.addAttribute("selectedTests", new SelectedTests());
        return "select-test";
    }

    @RequestMapping(value = {"/run-test"}, method = RequestMethod.POST)
    public String runTest(@ModelAttribute SelectedTests selectedTests) {
        //Start Async testing
        asyncTestService.asyncTest(selectedTests);
        //Load live-test.html to observe the test dynamically
        return "redirect:/live-test";
    }

    @RequestMapping(value = {"/live-test"}, method = RequestMethod.GET)
    public String liveTest() {
        //Load live-test.html to observe the test dynamically
        return "live-test";
    }


    @RequestMapping(value = {"/test-reports"}, method = RequestMethod.GET)
    public String testReports(Model model) {
        List<TestBatch> test_batches = testBatchRepository.findAll();
        model.addAttribute("test_batches", test_batches);
        return "test-reports";
    }

    @GetMapping("/test-batch/{id}")
    public String getTestBatch(@PathVariable(name = "id") Integer id, Model model, HttpSession session) {
        if (!sessionService.validateSession(session, model)) return "login";

        //Get all test results of the batch by id
        List<TestResult> testResults = testResultRepository.findAllByTestBatchId(id);

        //Add as model attribute
        model.addAttribute("testResults", testResults);
        return "test-batch";
    }


    @GetMapping("/test-result/{id}")
    public String getTestResult(@PathVariable(name = "id") Integer id, Model model, HttpSession session) {
        if (!sessionService.validateSession(session, model)) return "login";
        Optional<TestResult> testResult = testResultRepository.findById(id);

        //Pretty print for ResponseJSON if not null
        String responseJSON = testResult.get().getResponseJSON();
        if (responseJSON != null) {
            responseJSON = JSONUtils.toPrettyFormat(responseJSON);
            testResult.get().setResponseJSON(responseJSON);
        }

        //Add as model attribute
        model.addAttribute("testResult", testResult.get());
        return "test-result";
    }


    @RequestMapping(value = {"/create-schema-validation-test"}, method = RequestMethod.GET)
    public String createTest(HttpSession session, Model model) {
        //If session not valid redirect to login
        if (!sessionService.validateSession(session, model)) return "login";

        //Add attribute and show HTML
        model.addAttribute("test", new SchemaValidationTests());
        return "create-schema-validation-test";
    }

    @RequestMapping(value = {"/create-schema-validation-test"}, method = RequestMethod.POST)
    public ModelAndView createTestProcess(@ModelAttribute SchemaValidationTests test, HttpSession session, ModelMap model) {
        //If session not valid redirect to login
        if (!sessionService.validateSession(session, (Model) model)) return new ModelAndView("login.html", model);

        //Save the test to database and redirect with success message
        schemaValidationTestsRepository.save(test);
        model.addAttribute("message", "Test Created Succesfully.");
        model.addAttribute("type", "success");
        return new ModelAndView("redirect:/dashboard", model);
    }

    @RequestMapping(value = {"/create-data-validation-test"}, method = RequestMethod.GET)
    public String createDataTest(HttpSession session, Model model) {
        //If session not valid redirect to login
        if (!sessionService.validateSession(session, model)) return "login";

        //Add attribute and show HTML
        model.addAttribute("test", new DataValidationTests());
        return "create-data-validation-test";
    }


    @RequestMapping(value = {"/create-data-validation-test"}, method = RequestMethod.POST)
    public ModelAndView createDataTestProcess(@RequestParam(value = "assertionColumn[]") String[] assertionColumn, @RequestParam(value = "assertionKey[]") String[] assertionKey, @ModelAttribute DataValidationTests test, HttpSession session, ModelMap model) {
        //If session not valid redirect to login
        if (!sessionService.validateSession(session, (Model) model)) return new ModelAndView("login.html", model);

        //Initialize the assertion list
        test.setDataAssert(new ArrayList<>());

        System.out.println(test.toString());

        //Add assertions and save the test to database
        for(int i=0;i<assertionColumn.length;i++){
            //Create new assertion
            DataValidationAssertion assertion = new DataValidationAssertion();
            assertion.setDbColumnName(assertionColumn[i]);
            assertion.setJsonKey(assertionKey[i]);

            //Add it to data validation test assertions
            test.getDataAssert().add(assertion);
        }

        //Persist the test
        dataValidationTestsRepository.save(test);

        //Redirect with successful
        model.addAttribute("message", "Test Created Successfully.");
        model.addAttribute("type", "success");
        return new ModelAndView("redirect:/dashboard", model);
    }

}