package com.banana.volunteer.repository;

import com.banana.volunteer.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    List<Major> findAll();
}
