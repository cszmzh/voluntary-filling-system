package com.banana.volunteer.repository;

import com.banana.volunteer.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    List<Organization> findAll();

    Organization findByOrgId(Integer orgId);

    Organization findByOrgName(String name);
}
