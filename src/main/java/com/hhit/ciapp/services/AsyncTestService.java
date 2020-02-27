package com.hhit.ciapp.services;

import com.hhit.ciapp.classes.TestException;
import com.hhit.ciapp.models.*;
import com.hhit.ciapp.repositories.TestBatchRepository;
import com.hhit.ciapp.repositories.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class AsyncTestService {
    //Boolean variable to check if test is running
    private boolean isTestRunning;

    //Test batch POJO and failed test counter
    private TestBatch testBatch;

    //Variables to share the status dynamically
    private int totalTestCount;
    private int executedTestCount;

    private TestBatchRepository testBatchRepository;
    private TestResultRepository testResultRepository;
    private TestCRUDService testCRUDService;

    @Autowired
    public AsyncTestService(TestBatchRepository testBatchRepository, TestResultRepository testResultRepository, TestCRUDService testCRUDService) {
        this.testBatchRepository = testBatchRepository;
        this.testResultRepository = testResultRepository;
        this.testCRUDService = testCRUDService;
        isTestRunning = false;
    }

    @Async
    public void asyncTest(SelectedTests selectedTests) {

        //Failed test counter
        Integer failedTestCount = 0;

        //Change isTestRunning state
        isTestRunning = true;

        //Initialize Atomic Variables
        executedTestCount = 0;
        totalTestCount = selectedTests.getTestNames().length;

        //Create test batch and persist
        testBatch = new TestBatch();
        testBatch.setTotalTestCount(totalTestCount);
        testBatchRepository.save(testBatch);

        //Get the Test Objects And Run
        for (String testName : selectedTests.getTestNames()) {

            //Get the test model by name
            TestModel test = testCRUDService.getTestByName(testName);

            //Run the test and catch TestException if occurs
            TestResult testResult = null;
            try {
                testResult = test.getTestObject().runTest();
            } catch (TestException e) {
                //Get the test result from exception object
                testResult = e.getTr();
                //Set the test result properties according to exception
                testResult.setStatus(Status.FAILED);
                testResult.setErrorMessage(e.getMessage());
                testResult.setErrorCount(e.getErrorCount());
            }

            //Set testResult batch property
            testResult.setTestBatch(testBatch);

            //Set the test name
            testResult.setTestName(test.getTestName());

            //Save the Test Result to Database
            testResultRepository.save(testResult);

            //Increment executed test counter
            executedTestCount += 1;

            //Increment failed test counter if result is failed
            if (testResult.getStatus() == Status.FAILED) failedTestCount += 1;
        }


        //Update Test Batch for failed test count variable
        testBatch.setFailedTestCount(failedTestCount);
        testBatchRepository.save(testBatch);

        //Test Finished
        isTestRunning = false;

    }

    public Integer getExecutedTestCount() {
        return executedTestCount;
    }

    public Integer getTotalTestCount() {
        return totalTestCount;
    }

    public Boolean getIsTestRunning() {
        return isTestRunning;
    }

    public TestBatch getTestBatch() {
        return testBatch;
    }


}