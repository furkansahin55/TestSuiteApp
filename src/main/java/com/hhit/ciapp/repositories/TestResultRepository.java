package com.hhit.ciapp.repositories;

import com.hhit.ciapp.models.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    List<TestResult> findAllByTestBatchId(Integer testBatchId);

}
