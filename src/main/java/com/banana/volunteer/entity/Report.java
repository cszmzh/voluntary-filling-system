package com.banana.volunteer.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;
    private Integer studentId;
    private Integer volFirst;
    private Integer volSecond;
    private String reasonFirst;
    private String reasonSecond;
    private boolean isDispensing;
    private Integer status;
    private Date updateTime;
    private Date createTime;
    private String remark;
}
