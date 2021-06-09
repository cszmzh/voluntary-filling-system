package com.banana.volunteer.service;

import com.banana.volunteer.entity.Report;
import com.banana.volunteer.entity.view.ReportView;
import org.springframework.data.domain.Page;

public interface ReportService {

    /**
     * 1.查询当前所有志愿信息
     *
     * @param page     //当前页数
     * @param pageSize //一页显示多少条
     * @return Page<ReportView>
     */
    Page<ReportView> getAllView(Integer page, Integer pageSize);

    /**
     * 2.通过学号删除一条志愿信息
     */
    void deleteByStuId(Integer stuId);

    /**
     * 3.保存一条志愿信息
     */
    Report save(Report report);

    /**
     * 4.根据学号查找一条志愿信息（视图）
     *
     * @param stuId 学号
     * @return 志愿视图
     */
    ReportView findViewByStuId(Integer stuId);

    /**
     * 5.根据学号查找一条志愿实体类
     *
     * @param stuId 学号
     * @return 志愿实体
     */
    Report findEntityByStuId(Integer stuId);

    /**
     * 6.更新一条志愿信息
     *
     * @param report 志愿实体
     * @return 志愿实体
     */
    Report update(Report report);
}
