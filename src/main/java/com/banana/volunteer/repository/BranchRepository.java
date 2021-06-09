package com.banana.volunteer.repository;

import com.banana.volunteer.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Integer> {

    List<Branch> findAllByOrgId(Integer orgId);

    Branch findByBranchId(Integer branchId);
}
