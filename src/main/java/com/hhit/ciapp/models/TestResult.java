package com.hhit.ciapp.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "test_result")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "result_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "test_batch_id", nullable = false)
    private TestBatch testBatch;

    //@Lob for maximum storage
    @Lob
    @Column(name = "result_JSON")
    private String responseJSON;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "result_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    @Column(name = "result_error_message")
    private String errorMessage;

    @Column(name = "result_error_count")
    private Integer errorCount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

}

