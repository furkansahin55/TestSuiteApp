package com.hhit.ciapp.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "schema_validation_test")
public class SchemaValidationTests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "test_id")
    private int id;

    @Column(name = "test_name")
    @NotEmpty(message = "*Please provide test name")
    private String testName;

    @Lob
    @Column(name = "test_schema")
    @NotEmpty(message = "*Please provide test schema")
    private String testSchema;

    @Column(name = "test_request_url")
    @NotEmpty(message = "*Please provide request url")
    private String testUrl;

    @Column(name = "test_request_method")
    @NotEmpty(message = "*Please provide request method")
    private String testRequestMethod;

    @Lob
    @Column(name = "test_request_body")
    private String testRequestBody;

    @Column
    private int priorityOrder;

}
