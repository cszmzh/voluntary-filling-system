package com.banana.volunteer.service;

import java.util.List;

public interface DataService {
    /**
     * 1.统计志愿数
     */
    long getTotalNum();

    /**
     * 2.获取填报志愿组织
     *
     * @param order 志愿次序 1/2
     */
    List<String> getOrgByOrder(Integer order);

    /**
     * 3.根据志愿统计填报次数
     *
     * @param order   志愿次序 1/2
     * @param orgName 组织名
     */
    long getNumByOrg(Integer order, String orgName);
}
