package com.banana.volunteer.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Data
public class Major {
    @Id
    @Min(1)
    @Max(99999)
    private Integer majorId;
    @Size(min = 2, max = 15)
    private String majorName;
    @Min(1)
    @Max(99)
    private Integer classNum;
}
