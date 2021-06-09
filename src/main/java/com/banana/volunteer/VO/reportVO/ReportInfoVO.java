package com.banana.volunteer.VO.reportVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportInfoVO {

    private String organization;

    private String branch;

    private String reason;

}
