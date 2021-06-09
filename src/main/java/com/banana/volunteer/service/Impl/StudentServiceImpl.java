package com.banana.volunteer.service.Impl;

import com.banana.volunteer.entity.Student;
import com.banana.volunteer.enums.EnrollStatusEnum;
import com.banana.volunteer.repository.StudentRepository;
import com.banana.volunteer.entity.Report;
import com.banana.volunteer.service.ReportService;
import com.banana.volunteer.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repository;
    @Autowired
    private ReportService reportService;

    @Override
    public Student save(Student student) {
        return repository.save(student);
    }

    @Override
    public Student findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public boolean enroll(Integer stuId, Integer flag) {
        Student student = repository.findById(stuId).orElse(null);
        Report report = reportService.findEntityByStuId(stuId);
        // 第一志愿录取
        if (flag == 1 && report.getStatus().equals(EnrollStatusEnum.UNDER_ENROLL.getCode())) {
            student.setBranchId(report.getVolFirst());
            report.setStatus(EnrollStatusEnum.FIRST_ENROLL.getCode());
            repository.save(student);   // 更新学生信息
            reportService.save(report); // 更新志愿信息
            return true;
        }
        // 第二志愿录取
        if (flag == 2 && report.getStatus().equals(EnrollStatusEnum.UNDER_ENROLL.getCode())) {
            student.setBranchId(report.getVolSecond());
            report.setStatus(EnrollStatusEnum.SECOND_ENROLL.getCode());
            repository.save(student);   // 更新学生信息
            reportService.save(report); // 更新志愿信息
            return true;
        }
        return false;
    }

    @Override
    public Student update(Student student) {
        return repository.saveAndFlush(student);
    }

    @Override
    public void deleteById(Integer stuId) {
        repository.deleteById(stuId);
    }
}
