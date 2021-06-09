package com.banana.volunteer.VO.reportVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDataVO {

    @JsonProperty("total")
    private Integer reportTotal;

    @JsonProperty("report_list")
    private List<ReportViewListVO> reportViewListVO;

}
