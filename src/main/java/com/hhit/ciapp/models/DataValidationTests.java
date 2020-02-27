package com.hhit.ciapp.models;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Entity
@Table(name = "data_validation_test")
public class DataValidationTests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "test_id")
    private int id;

    @Column(name = "test_name")
    @NotEmpty(message = "*Please provide test name")
    private String testName;

    @Column(name = "test_request_url")
    @NotEmpty(message = "*Please provide request url")
    private String testUrl;

    @Column(name = "test_request_method")
    @NotEmpty(message = "*Please provide request method")
    private String testRequestMethod;

    @Lob
    @Column(name = "test_request_body")
    private String testRequestBody;

    @Column(name = "test_oracle_table")
    @NotEmpty(message = "*Please provide table name")
    private String testOracleTable;

    @Column(name = "test_json_array")
    @NotEmpty(message = "*Please provide table name")
    private String testJSONArrayKey;

    @Column(name = "data_assert")
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = {CascadeType.ALL})
    private List<DataValidationAssertion> dataAssert;

    @Column
    private int priorityOrder;

}
