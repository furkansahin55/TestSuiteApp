package com.hhit.ciapp.classes;

import com.hhit.ciapp.models.TestResult;
import lombok.Data;

public class TestException extends Exception {

    private int errorCount;
    private TestResult tr;

    public TestException(TestResult tr, String errorMessage, int errorCount) {
        super(errorMessage);
        this.errorCount = errorCount;
        this.tr = tr;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public TestResult getTr() {
        return tr;
    }

    public void setTr(TestResult tr) {
        this.tr = tr;
    }
}
