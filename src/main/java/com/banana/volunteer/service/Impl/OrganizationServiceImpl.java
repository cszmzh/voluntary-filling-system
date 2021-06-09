package com.banana.volunteer.service.Impl;

import com.banana.volunteer.entity.Branch;
import com.banana.volunteer.entity.Organization;
import com.banana.volunteer.enums.ResultEnum;
import com.banana.volunteer.exception.BusinessException;
import com.banana.volunteer.repository.BranchRepository;
import com.banana.volunteer.repository.OrganizationRepository;
import com.banana.volunteer.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    private BranchRepository braRepository;

    @Override
    public String findOrgNameByBranchId(Integer branchId) {
        Branch branch = braRepository.findByBranchId(branchId);
        Organization organization = orgRepository.findByOrgId(branch.getOrgId());
        return organization.getOrgName();
    }

    @Override
    public List<Organization> findAllOrg() {
        return orgRepository.findAll();
    }

    @Override
    public List<Branch> findAllBranchByOrgId(Integer orgId) {
        return braRepository.findAllByOrgId(orgId);
    }

    @Override
    public Integer findOrgIdByBranchId(Integer branchId) {
        Branch branch = braRepository.findByBranchId(branchId);
        if (branch == null) {
            return null;
        }
        return branch.getOrgId();
    }

    @Override
    public Integer findOrgIdByOrgName(String orgName) {
        Organization organization = orgRepository.findByOrgName(orgName);
        if (organization == null) {
            return null;
        }
        return organization.getOrgId();
    }

    @Override
    public String findOrgNameByOrgId(Integer orgId) {
        Organization organization = orgRepository.findByOrgId(orgId);
        if (organization == null) {
            return null;
        }
        return organization.getOrgName();
    }

    @Override
    public String findBranchNameByBranchId(Integer branchId) {
        Branch branch = braRepository.findByBranchId(branchId);
        if (branch == null) {
            return null;
        }
        return branch.getBranchName();
    }

    @Override
    public void deleteOrgByOrgId(Integer orgId) {
        orgRepository.deleteById(orgId);
    }

    @Override
    public Organization updateOrg(Organization organization) {
        return orgRepository.saveAndFlush(organization);
    }

    @Override
    public Organization createOrg(Organization organization) {
        if (!orgRepository.findById(organization.getOrgId()).isPresent()) {
            return orgRepository.saveAndFlush(organization);
        } else {
            throw new BusinessException(ResultEnum.ID_DUPLICATE);
        }
    }

    @Override
    public List<Branch> findAllBranch() {
        return braRepository.findAll();
    }

    @Override
    public void deleteBranchByBranchId(Integer branchId) {
        braRepository.deleteById(branchId);
    }

    @Override
    public Branch createBranch(Branch branch) {
        if (!braRepository.findById(branch.getBranchId()).isPresent()) {
            return braRepository.saveAndFlush(branch);
        } else {
            throw new BusinessException(ResultEnum.ID_DUPLICATE);
        }
    }

    @Override
    public Branch updateBranch(Branch branch) {
        return braRepository.saveAndFlush(branch);
    }
}
