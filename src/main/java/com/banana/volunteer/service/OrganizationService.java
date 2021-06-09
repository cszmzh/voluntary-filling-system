package com.banana.volunteer.service;

import com.banana.volunteer.entity.Branch;
import com.banana.volunteer.entity.Organization;

import java.util.List;

public interface OrganizationService {

    /**
     * 1.通过下属组织id查找组织名字
     */
    String findOrgNameByBranchId(Integer branchId);

    /**
     * 2.获取所有组织
     */
    List<Organization> findAllOrg();

    /**
     * 3.根据组织id获取下属分支
     */
    List<Branch> findAllBranchByOrgId(Integer orgId);

    /**
     * 4.通过下属组织id查找组织id
     */
    Integer findOrgIdByBranchId(Integer branchId);

    /**
     * 5.通过组织名查找组织id
     */
    Integer findOrgIdByOrgName(String orgName);

    /**
     * 6.通过组织id查找组织名
     */
    String findOrgNameByOrgId(Integer orgId);

    /**
     * 7.通过分支id查找分支名
     */
    String findBranchNameByBranchId(Integer branchId);

    /**
     * 8.根据组织id删除组织
     */
    void deleteOrgByOrgId(Integer orgId);

    /**
     * 9.更新组织
     */
    Organization updateOrg(Organization organization);

    /**
     * 10.创建组织
     */
    Organization createOrg(Organization organization);

    /**
     * 11.获取所有分支
     */
    List<Branch> findAllBranch();

    /**
     * 12.根据分支id删除分支
     */
    void deleteBranchByBranchId(Integer branchId);

    /**
     * 13.创建分支
     */
    Branch createBranch(Branch branch);

    /**
     * 14.更新分支
     */
    Branch updateBranch(Branch branch);
}
