package com.banana.volunteer.controller;

import com.alibaba.fastjson.JSONObject;
import com.banana.volunteer.VO.ResultVO;
import com.banana.volunteer.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 1.获取数据分析情况
     */
    @GetMapping("/get")
    public ResultVO getData_ADMIN() {
        ResultVO resultVO = new ResultVO(0, "success");

        JSONObject json = new JSONObject();
        json.put("peopleNum", dataService.getTotalNum());

        // 第一志愿
        JSONObject child1 = new JSONObject();
        List<String> orgFirstList = dataService.getOrgByOrder(1);
        for (String s : orgFirstList) {
            long num = dataService.getNumByOrg(1, s);
            child1.put(s, num);
        }
        json.put("first", child1);

        // 第二志愿
        JSONObject child2 = new JSONObject();
        List<String> orgSecondList = dataService.getOrgByOrder(2);
        for (String s : orgSecondList) {
            long num = dataService.getNumByOrg(2, s);
            child2.put(s, num);
        }
        json.put("second", child2);

        resultVO.setData(json);
        return resultVO;
    }
}
