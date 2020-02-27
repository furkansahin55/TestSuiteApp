package com.hhit.ciapp.repositories;

import com.hhit.ciapp.models.DataValidationTests;
import com.hhit.ciapp.models.SchemaValidationTests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataValidationTestsRepository extends JpaRepository<DataValidationTests, Integer> {
}
