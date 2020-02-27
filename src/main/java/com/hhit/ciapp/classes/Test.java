package com.hhit.ciapp.classes;

import com.hhit.ciapp.models.TestResult;

public interface Test {
    Integer getPriorityOrder();

    TestResult runTest() throws TestException;
}
