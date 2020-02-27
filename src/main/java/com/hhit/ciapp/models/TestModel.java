package com.hhit.ciapp.models;

import com.hhit.ciapp.classes.Test;
import lombok.Data;


@Data
public class TestModel implements java.lang.Comparable<TestModel> {
    Test testObject;
    String testName;
    Integer priorityOrder;

    @Override
    public int compareTo(TestModel o) {
        return this.priorityOrder.compareTo(o.getPriorityOrder());
    }
}
