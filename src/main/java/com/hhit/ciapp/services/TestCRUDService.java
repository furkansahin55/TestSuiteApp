package com.hhit.ciapp.services;

import com.hhit.ciapp.classes.DataValidationTest;
import com.hhit.ciapp.classes.SchemaValidationTest;
import com.hhit.ciapp.models.DataValidationTests;
import com.hhit.ciapp.models.SchemaValidationTests;
import com.hhit.ciapp.models.TestModel;
import com.hhit.ciapp.repositories.DataValidationTestsRepository;
import com.hhit.ciapp.repositories.SchemaValidationTestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TestCRUDService {
    private SchemaValidationTestsRepository schemaValidationTestsRepository;
    private DataValidationTestsRepository dataValidationTestsRepository;
    private ReflectionsService reflectionsService;


    @Autowired
    public TestCRUDService(SchemaValidationTestsRepository schemaValidationTestsRepository, DataValidationTestsRepository dataValidationTestsRepository, ReflectionsService reflectionsService) {
        this.schemaValidationTestsRepository = schemaValidationTestsRepository;
        this.dataValidationTestsRepository = dataValidationTestsRepository;
        this.reflectionsService = reflectionsService;
    }

    public TestModel getTestByName(String testName) {
        List<TestModel> allTests = getAllTests();
        for (TestModel test : allTests) {
            if (test.getTestName().equals(testName))
                return test;
        }
        //Return null if test not found
        return null;
    }

    public List<TestModel> getAllTests() {
        //Get test lists
        List<SchemaValidationTests> schemas = schemaValidationTestsRepository.findAll();
        List<DataValidationTests> data = dataValidationTestsRepository.findAll();


        List<TestModel> allTests = new ArrayList<>();

        //Get all tests from schema validation table
        for (SchemaValidationTests schema : schemas) {
            TestModel test = new TestModel();
            test.setTestObject(new SchemaValidationTest(schema));
            test.setTestName(schema.getTestName());
            test.setPriorityOrder(schema.getPriorityOrder());
            allTests.add(test);
        }

        //Get all tests from data validation table
        for (DataValidationTests datum : data) {
            TestModel test = new TestModel();
            test.setTestObject(new DataValidationTest(datum));
            test.setTestName(datum.getTestName());
            test.setPriorityOrder(datum.getPriorityOrder());
            allTests.add(test);
        }

        //Get all exceptional tests from reflection service and add to all tests list
        allTests.addAll(reflectionsService.getAllExceptionalTests());

        //Order by priority order
        Collections.sort(allTests);

        return allTests;
    }


}
