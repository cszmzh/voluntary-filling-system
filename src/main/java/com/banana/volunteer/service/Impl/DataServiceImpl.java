package com.banana.volunteer.service.Impl;

import com.banana.volunteer.repository.ReportViewRepository;
import com.banana.volunteer.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private ReportViewRepository reportViewRepository;

    @Override
    public long getTotalNum() {
        return reportViewRepository.count();
    }

    @Override
    public List<String> getOrgByOrder(Integer order) {
        if (order == 1) {
            return reportViewRepository.findDistinctByOrgFirst();
        }
        return reportViewRepository.findDistinctByOrgSecond();
    }

    @Override
    public long getNumByOrg(Integer order, String orgName) {
        if (order == 1) {
            return reportViewRepository.countDistinctByOrgFirst(orgName);
        }
        return reportViewRepository.countDistinctByOrgSecond(orgName);
    }
}
