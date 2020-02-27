package com.hhit.ciapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "data_validation_assertion")
public class DataValidationAssertion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assertion_id")
    private int id;

    @Column(name = "column_name")
    private String dbColumnName;

    @Column(name = "json_key")
    private String jsonKey;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "test_id")
    private DataValidationTests dataValidationTest;
}
