package com.banana.volunteer.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Size(min = 3, max = 12)
    private String userName;
    @Size(min = 1, max = 12)
    private String realName;
    @Size(min = 6, max = 16)
    private String userPassword;
    private Integer branchId;
    private Integer userStatus;
    private Date createTime;
}
