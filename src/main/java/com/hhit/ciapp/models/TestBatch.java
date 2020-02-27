package com.hhit.ciapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "test_batch")
public class TestBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "batch_id")
    private int id;

    @JsonIgnore
    @Column(name = "test_results")
    @OneToMany(mappedBy = "testBatch")
    private List<TestResult> testResults;

    @Column(name = "total_test_count")
    private Integer totalTestCount;

    @Column(name = "failed_test_count")
    private Integer failedTestCount = 0;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

}
