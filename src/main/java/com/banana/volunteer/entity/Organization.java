package com.banana.volunteer.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Data
public class Organization {
    @Id
    @Min(1)
    @Max(99999)
    private Integer orgId;
    @Size(min = 2, max = 20)
    private String orgName;
    @Size(max = 100)
    private String orgDes;
    @Size(max = 20)
    private String managerName;
}
