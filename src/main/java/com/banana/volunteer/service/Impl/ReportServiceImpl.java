package com.banana.volunteer.service.Impl;

import com.banana.volunteer.enums.UserStatusEnum;
import com.banana.volunteer.repository.ReportRepository;
import com.banana.volunteer.repository.ReportViewRepository;
import com.banana.volunteer.holder.UserOrgHolder;
import com.banana.volunteer.holder.UserStatusHolder;
import com.banana.volunteer.service.OrganizationService;
import com.banana.volunteer.service.ReportService;
import com.banana.volunteer.entity.Report;
import com.banana.volunteer.entity.view.ReportView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportViewRepository reportViewRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private OrganizationService orgService;

    @Override
    public Page<ReportView> getAllView(Integer pageNum, Integer pageSize) {
        // 判断身份以返回不同结果
        if (UserStatusHolder.get().equals(UserStatusEnum.ROOT.getCode().toString())) {
            Sort sort = Sort.by(Sort.Order.asc("createTime"));
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            Page<ReportView> page = reportViewRepository.findAll(pageable);
            return page;
        } else {
            // 通过登录用户所属组织id查找相关志愿
            Sort sort = Sort.by(Sort.Order.asc("createTime"));
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            String orgName = orgService.findOrgNameByOrgId(new Integer(UserOrgHolder.get()));
            Page<ReportView> page = reportViewRepository.findByOrgName(orgName, pageable);
            return page;
        }
    }

    @Override
    public void deleteByStuId(Integer stuId) {
        reportRepository.deleteByStudentId(stuId);
    }

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public ReportView findViewByStuId(Integer stuId) {
        return reportViewRepository.findById(stuId).orElse(null);
    }

    @Override
    public Report findEntityByStuId(Integer stuId) {
        return reportRepository.findByStudentId(stuId);
    }

    @Override
    public Report update(Report report) {
        return reportRepository.saveAndFlush(report);
    }
}
