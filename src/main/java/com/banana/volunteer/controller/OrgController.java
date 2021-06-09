package com.banana.volunteer.controller;

import com.banana.volunteer.VO.ResultVO;
import com.banana.volunteer.VO.branchInfoVO.BranchResult;
import com.banana.volunteer.entity.Branch;
import com.banana.volunteer.entity.Organization;
import com.banana.volunteer.holder.UserIdHolder;
import com.banana.volunteer.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/org")
@Slf4j
public class OrgController {

    @Autowired
    private OrganizationService orgService;

    /**
     * 1.获取所有组织
     */
    @GetMapping("/getMain")
    public ResultVO getAllOrg() throws InterruptedException {
        Thread.sleep(100);
        List<Organization> orgList = orgService.findAllOrg();
        return new ResultVO(0, "success", orgList);
    }

    /**
     * 2.根据组织id获取所有下属分支
     */
    @GetMapping("/getBranch")
    public ResultVO getAllBranch(@RequestParam("orgId") Integer orgId) {
        List<Branch> branchList = orgService.findAllBranchByOrgId(orgId);
        return new ResultVO(0, "success", branchList);
    }

    @GetMapping("/getBranchResult")
    public ResultVO getAllBranchResult() {
        List<Branch> branchList = orgService.findAllBranch();
        List<BranchResult> branchResultList = new ArrayList<>();
        for (Branch branch : branchList) {
            BranchResult branchResult = new BranchResult();
            branchResult.setBranchId(branch.getBranchId());
            branchResult.setOrgName(orgService.findOrgNameByBranchId(branch.getBranchId()));
            branchResult.setBranchName(branch.getBranchName());
            branchResult.setBranchDes(branch.getBranchDes());
            branchResult.setManagerName(branch.getManagerName());
            branchResultList.add(branchResult);
        }
        return new ResultVO(0, "success", branchResultList);
    }

    /**
     * 3.根据组织id删除组织
     */
    @PostMapping("/deleteOrg")
    public ResultVO deleteOrgByOrgId_ROOT(@RequestParam("orgId") Integer orgId) {
        log.info("[org/deleteOrg]删除组织id[{}]，操作者id[{}]", orgId, UserIdHolder.get());
        orgService.deleteOrgByOrgId(orgId);
        return new ResultVO(0, "success");
    }

    /**
     * 4.更新组织信息
     */
    @PostMapping("/updateOrg")
    public ResultVO updateOrg_ROOT(@RequestParam("orgId") Integer orgId, @RequestParam("orgName") String orgName,
                                   @RequestParam("orgDes") String orgDes, @RequestParam("managerName") String managerName) {
        log.info("[org/updateOrg]更新组织id[{}]，操作者id[{}]", orgId, UserIdHolder.get());
        Organization organization = new Organization();
        organization.setOrgId(orgId);
        organization.setOrgName(orgName);
        organization.setOrgDes(orgDes);
        organization.setManagerName(managerName);
        return new ResultVO(0, "success", orgService.updateOrg(organization));
    }

    /**
     * 5.创建组织
     */
    @PostMapping("/createOrg")
    public ResultVO createOrg_ROOT(@RequestParam("orgId") Integer orgId, @RequestParam("orgName") String orgName,
                                   @RequestParam("orgDes") String orgDes, @RequestParam("managerName") String managerName) {
        log.info("[org/createOrg]创建组织id[{}]，操作者id[{}]", orgId, UserIdHolder.get());
        Organization organization = new Organization();
        organization.setOrgId(orgId);
        organization.setOrgName(orgName);
        organization.setOrgDes(orgDes);
        organization.setManagerName(managerName);
        return new ResultVO(0, "success", orgService.createOrg(organization));
    }

    /**
     * 6.根据分支id删除分支
     */
    @PostMapping("/deleteBranch")
    public ResultVO deleteBranch_ROOT(@RequestParam("branchId") Integer branchId) {
        log.info("[org/deleteBranch]删除分支id[{}]，操作者id[{}]", branchId, UserIdHolder.get());
        orgService.deleteBranchByBranchId(branchId);
        return new ResultVO(0, "success");
    }

    /**
     * 7.更新分支
     */
    @PostMapping("/updateBranch")
    public ResultVO updateBranch_ROOT(@RequestParam("branchId") Integer branchId, @RequestParam("orgId") Integer orgId,
                                      @RequestParam("branchName") String branchName, @RequestParam("branchDes") String branchDes,
                                      @RequestParam("managerName") String managerName) {
        log.info("[org/updateBranch]更新分支id[{}]，操作者id[{}]", branchId, UserIdHolder.get());
        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setOrgId(orgId);
        branch.setBranchName(branchName);
        branch.setBranchDes(branchDes);
        branch.setManagerName(managerName);
        orgService.updateBranch(branch);
        return new ResultVO(0, "success");
    }

    /**
     * 8.创建分支
     */
    @PostMapping("/createBranch")
    public ResultVO createBranch_ROOT(@RequestParam("branchId") Integer branchId, @RequestParam("orgId") Integer orgId,
                                      @RequestParam("branchName") String branchName, @RequestParam("branchDes") String branchDes,
                                      @RequestParam("managerName") String managerName) {
        log.info("[org/createBranch]创建分支id[{}]，操作者id[{}]", branchId, UserIdHolder.get());
        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setOrgId(orgId);
        branch.setBranchName(branchName);
        branch.setBranchDes(branchDes);
        branch.setManagerName(managerName);
        orgService.createBranch(branch);
        return new ResultVO(0, "success");
    }
}
