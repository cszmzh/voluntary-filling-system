package com.banana.volunteer.repository;

import com.banana.volunteer.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    Report findByStudentId(Integer studentId);

    void deleteByStudentId(Integer studentId);
}
