package com.banana.volunteer.entity.view;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class ReportView {
    @Id
    private Integer stuId;
    private String stuName;
    private String majorName;
    private Integer stuClass;
    private String stuPhone;
    private String stuQq;
    private String orgFirst;
    private String braFirst;
    private String orgSecond;
    private String braSecond;
    private String reasonFirst;
    private String reasonSecond;
    private Integer isDispensing;
    private Integer status;
    private Date updateTime;
    private Date createTime;
    private String remark;
}
