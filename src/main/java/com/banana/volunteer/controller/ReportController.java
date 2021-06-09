package com.banana.volunteer.controller;

import com.banana.volunteer.VO.ResultVO;
import com.banana.volunteer.VO.reportVO.ReportDataVO;
import com.banana.volunteer.VO.reportVO.ReportInfoVO;
import com.banana.volunteer.VO.reportVO.ReportViewListVO;
import com.banana.volunteer.entity.Student;
import com.banana.volunteer.enums.EnrollStatusEnum;
import com.banana.volunteer.enums.UserStatusEnum;
import com.banana.volunteer.holder.UserIdHolder;
import com.banana.volunteer.holder.UserOrgHolder;
import com.banana.volunteer.holder.UserStatusHolder;
import com.banana.volunteer.service.OrganizationService;
import com.banana.volunteer.service.ReportService;
import com.banana.volunteer.service.StudentService;
import com.banana.volunteer.entity.Report;
import com.banana.volunteer.entity.view.ReportView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private OrganizationService orgService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //2020-04-12 21:40

    /**
     * 1.获取所有志愿信息接口
     */
    @GetMapping("/getAll")
    public ResultVO getAll_ADMIN(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {

        log.info("[report/getAll]获取志愿，操作者id[{}]", UserIdHolder.get());

        // 这里page-1，为了前端容易理解
        Page<ReportView> reportViewPage = reportService.getAllView(page - 1, pageSize);
        List<ReportView> reportViewList = reportViewPage.getContent();

        // 封装ResultVO
        // 第一层
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");

        // 第二层
        ReportDataVO reportDataVO = new ReportDataVO();
        reportDataVO.setReportTotal((int) reportViewPage.getTotalElements());

        // 第三层
        List<ReportViewListVO> reportViewListVOList = new ArrayList();

        for (ReportView reportView : reportViewList) {
            ReportViewListVO reportViewListVO = new ReportViewListVO();

            reportViewListVO.setStudentId(reportView.getStuId());
            reportViewListVO.setStudentName(reportView.getStuName());
            reportViewListVO.setMajorName(reportView.getMajorName());
            reportViewListVO.setClassNumber(reportView.getStuClass());
            reportViewListVO.setStudentQQ(reportView.getStuQq());
            reportViewListVO.setStudentPhone(reportView.getStuPhone());

            ReportInfoVO firstWill = new ReportInfoVO();
            ReportInfoVO secondWill = new ReportInfoVO();

            // 填充第一志愿信息 第四层
            Integer orgFirstId = orgService.findOrgIdByOrgName(reportView.getOrgFirst());
            if (UserStatusHolder.get().equals(UserStatusEnum.ROOT.getCode().toString()) ||
                    UserOrgHolder.get().equals(orgFirstId.toString())) {
                firstWill.setOrganization(reportView.getOrgFirst());
                firstWill.setBranch(reportView.getBraFirst());
                firstWill.setReason(reportView.getReasonFirst());
            }

            // 填充第二志愿信息 第四层
            Integer orgSecondId = orgService.findOrgIdByOrgName(reportView.getOrgSecond());
            if (UserStatusHolder.get().equals(UserStatusEnum.ROOT.getCode().toString()) ||
                    (orgSecondId != null && UserOrgHolder.get().equals(orgSecondId.toString()))) {
                secondWill.setOrganization(reportView.getOrgSecond());
                secondWill.setBranch(reportView.getBraSecond());
                secondWill.setReason(reportView.getReasonSecond());
            }

            reportViewListVO.setFirstWill(firstWill);
            reportViewListVO.setSecondWill(secondWill);
            reportViewListVO.setIsDispensing(reportView.getIsDispensing());

            String strStatus = EnrollStatusEnum.UNDER_ENROLL.getMessage();
            if (UserStatusEnum.ADMIN.getCode().equals(reportView.getStatus())) {
                strStatus = EnrollStatusEnum.FIRST_ENROLL.getMessage();
            } else if (UserStatusEnum.ROOT.getCode().equals(reportView.getStatus())) {
                strStatus = EnrollStatusEnum.SECOND_ENROLL.getMessage();
            }

            reportViewListVO.setIsEnroll(strStatus);
            reportViewListVO.setUpdateTime(sdf.format(reportView.getUpdateTime()));
            reportViewListVO.setCreateTime(sdf.format(reportView.getCreateTime()));

            reportViewListVOList.add(reportViewListVO);
        }

        // 封装数据
        reportDataVO.setReportViewListVO(reportViewListVOList);
        resultVO.setData(reportDataVO);
        return resultVO;
    }

    /**
     * 2.提交志愿接口
     */
    @PostMapping("/insert")
    @Transactional
    public ResultVO insert(@RequestParam("stuId") String stuId, @RequestParam("stdName") String studentName, @RequestParam("majorId") Integer majorId,
                           @RequestParam("classNum") Integer classNumber, @RequestParam("stdQQ") String studentQQ, @RequestParam("stdPhone") String studentPhone,
                           @RequestParam("firstWill") Integer firstWill, @RequestParam("firstReason") String firstReason, Integer secondWill, String secondReason,
                           @RequestParam("isDispensing") Boolean disPensing, @RequestParam("code") String code, HttpServletRequest request) {

        if (request.getSession().getAttribute("img-code") == null) {
            log.error("验证码生成失败！");
            return new ResultVO(6, "验证码生成失败");
        }
        if (code.isEmpty() || !request.getSession().getAttribute("img-code").equals(code)) {
            log.info("【插入失败】验证码错误, 正确验证码:{} , 当前验证码:{} ", request.getSession().getAttribute("img-code"), code);
            return new ResultVO(1, "验证码错误");
        }
        if (firstWill == null || firstReason.isEmpty()) {
            log.info("【插入失败】第一志愿信息不完整，firstWill:{}, firstReason:{}", firstWill, firstReason);
            return new ResultVO(2, "第一志愿信息不完整");
        }
        if (studentService.findById(new Integer(stuId)) != null) {
            log.info("【插入失败】该学号已填写过志愿信息，请先联系管理员删除");
            return new ResultVO(3, "志愿已填写");
        }
        if (studentName.isEmpty()) {
            log.info("【插入失败】姓名为空");
            return new ResultVO(4, "姓名为空");
        }
        if (majorId == null) {
            log.info("【插入失败】专业id为空");
            return new ResultVO(5, "专业信息为空");
        }

        Student student = new Student();
        Report report = new Report();

        // 保存学生信息
        student.setStuId(new Integer(stuId));
        student.setStuName(studentName);
        student.setMajorId(majorId);
        student.setStuClass(classNumber);
        student.setStuQq(studentQQ);
        student.setStuPhone(studentPhone);
        studentService.save(student);

        // 保存志愿信息
        report.setStudentId(new Integer(stuId));
        report.setVolFirst(firstWill);
        if (secondWill != null) {
            report.setVolSecond(secondWill);
            report.setReasonSecond(secondReason);
        }
        report.setStatus(EnrollStatusEnum.UNDER_ENROLL.getCode());
        report.setReasonFirst(firstReason);
        report.setDispensing(disPensing);
        reportService.save(report);

        return new ResultVO(0, "success");
    }

    /**
     * 3.根据学生学号查找志愿接口
     *
     * @param stuId 学生学号
     * @return
     */
    @PostMapping("/getByStuId")
    public ResultVO getByStuId_ADMIN(@RequestParam("stuId") String stuId) {

        log.info("[report/getByStuId]查找志愿[{}]，操作者id[{}]", stuId, UserIdHolder.get());

        ReportView report = reportService.findViewByStuId(new Integer(stuId));

        if (report == null) {
            return new ResultVO(1, "查找失败");
        }

        // 对管理员过滤志愿信息
        if (UserStatusHolder.get().equals(UserStatusEnum.ADMIN.getCode().toString())) {
            Integer orgFirstId = orgService.findOrgIdByOrgName(report.getOrgFirst());
            Integer orgSecondId = orgService.findOrgIdByOrgName(report.getOrgSecond());
            if (!UserOrgHolder.get().equals(orgFirstId.toString())) {
                if (orgSecondId == null || !UserOrgHolder.get().equals(orgSecondId.toString()))
                    return new ResultVO(1, "查找失败");
            }
        }

        ResultVO resultVO = new ResultVO(0, "查找成功");

        ReportViewListVO reportViewListVO = new ReportViewListVO();
        reportViewListVO.setStudentId(report.getStuId());
        reportViewListVO.setStudentName(report.getStuName());
        reportViewListVO.setMajorName(report.getMajorName());
        reportViewListVO.setClassNumber(report.getStuClass());
        reportViewListVO.setStudentQQ(report.getStuQq());
        reportViewListVO.setStudentPhone(report.getStuPhone());

        ReportInfoVO firstWill = new ReportInfoVO();
        ReportInfoVO secondWill = new ReportInfoVO();

        // 填充第一志愿信息
        Integer firstOrgId = orgService.findOrgIdByOrgName(report.getOrgFirst());
        if (UserStatusHolder.get().equals(UserStatusEnum.ROOT.getCode().toString())
                || UserOrgHolder.get().equals(firstOrgId.toString())) {
            firstWill.setOrganization(report.getOrgFirst());
            firstWill.setBranch(report.getBraFirst());
            firstWill.setReason(report.getReasonFirst());
        }

        // 填充第二志愿信息
        Integer secondOrgId = orgService.findOrgIdByOrgName(report.getOrgSecond());
        if (UserStatusHolder.get().equals(UserStatusEnum.ROOT.getCode().toString())
                || (secondOrgId != null && UserOrgHolder.get().equals(secondOrgId.toString()))) {
            secondWill.setOrganization(report.getOrgSecond());
            secondWill.setBranch(report.getBraSecond());
            secondWill.setReason(report.getReasonSecond());
        }

        reportViewListVO.setFirstWill(firstWill);
        reportViewListVO.setSecondWill(secondWill);
        reportViewListVO.setIsDispensing(report.getIsDispensing());

        String strStatus = EnrollStatusEnum.UNDER_ENROLL.getMessage();
        if (UserStatusEnum.ADMIN.getCode().equals(report.getStatus())) {
            strStatus = EnrollStatusEnum.FIRST_ENROLL.getMessage();
        } else if (UserStatusEnum.ROOT.getCode().equals(report.getStatus())) {
            strStatus = EnrollStatusEnum.SECOND_ENROLL.getMessage();
        }

        reportViewListVO.setIsEnroll(strStatus);
        reportViewListVO.setUpdateTime(sdf.format(report.getUpdateTime()));
        reportViewListVO.setCreateTime(sdf.format(report.getCreateTime()));
        reportViewListVO.setRemark(report.getRemark());

        resultVO.setData(reportViewListVO);
        return resultVO;
    }

    /**
     * 4.录取接口 (管理员身份)
     */
    @Transactional
    @PostMapping("/enroll_ADMIN")
    public ResultVO enroll_ADMIN(@RequestParam("stuId") Integer stuId, @RequestParam("willId") Integer willId) {

        log.info("[report/enroll_ADMIN]录取志愿[{}]，操作者id[{}]", stuId, UserIdHolder.get());

        Report report = reportService.findEntityByStuId(stuId);

        Integer orgId = orgService.findOrgIdByBranchId(willId);

        if (willId.equals(report.getVolFirst()) && UserOrgHolder.get().equals(orgId.toString())) {
            studentService.enroll(stuId, 1);
            log.info("录取学号一志愿: {} 成功", stuId);
            return new ResultVO(0, "录取成功");
        }

        if (willId.equals(report.getVolSecond()) && UserOrgHolder.get().equals(orgId.toString())) {
            studentService.enroll(stuId, 2);
            log.info("录取学号二志愿: {} 成功", stuId);
            return new ResultVO(0, "录取成功");
        }
        return new ResultVO(1, "录取失败，参数错误");
    }

    /**
     * 5.录取接口 (超级管理员身份)
     */
    @Transactional
    @PostMapping("/enroll_ROOT")
    public ResultVO enroll_ROOT(@RequestParam("stuId") Integer stuId, @RequestParam("willId") Integer willId) {

        log.info("[report/enroll_ROOT]录取志愿[{}]，操作者id[{}]", stuId, UserIdHolder.get());

        Report report = reportService.findEntityByStuId(stuId);

        if (willId == null) {
            return new ResultVO(1, "录取失败，参数错误");
        }

        if (willId.equals(report.getVolFirst())) {
            studentService.enroll(stuId, 1);
            return new ResultVO(0, "录取成功");
        }

        if (willId.equals(report.getVolSecond())) {
            studentService.enroll(stuId, 2);
            return new ResultVO(0, "录取成功");
        }
        return new ResultVO(1, "录取失败，参数错误");
    }

    /**
     * 6.更新志愿信息接口 （管理员身份）
     */
    @PostMapping("/update_ADMIN")
    @Transactional
    public ResultVO update_ADMIN(@RequestParam("stuId") String stuId, @RequestParam("majorId") Integer majorId,
                                 @RequestParam("classNum") Integer classNumber, @RequestParam("stdQQ") String studentQQ,
                                 @RequestParam("stdPhone") String studentPhone, @RequestParam("firstWill") Integer firstWill,
                                 String firstReason, Integer secondWill, String secondReason, @RequestParam("remark") String remark) {

        log.info("[report/update_ADMIN]更新志愿[{}]，操作者id[{}]", stuId, UserIdHolder.get());

        // 查询学生
        Student student = studentService.findById(new Integer(stuId));

        // 查询志愿
        Report report = reportService.findEntityByStuId(new Integer(stuId));

        if (report.getStatus() != EnrollStatusEnum.UNDER_ENROLL.getCode()) {
            log.error("【更新志愿失败】该学生已被录取，不允许修改志愿，学生号:{} ", stuId);
            return new ResultVO(1, "更新失败");
        }

        student.setStuId(new Integer(stuId));
        student.setMajorId(majorId);
        student.setStuClass(classNumber);
        student.setStuQq(studentQQ);
        student.setStuPhone(studentPhone);

        // 保存学生信息
        studentService.update(student);

        report.setStudentId(new Integer(stuId));

        // 注意管理员身份的组织判断
        Integer orgFirstId = orgService.findOrgIdByBranchId(firstWill);
        if (firstWill != null && !firstReason.isEmpty() && UserOrgHolder.get().equals(orgFirstId.toString())) {
            report.setVolFirst(firstWill);
            report.setReasonFirst(firstReason);
        }

        Integer orgSecondId = orgService.findOrgIdByBranchId(secondWill);
        if (secondWill != null && !secondReason.isEmpty() && UserOrgHolder.get().equals(orgSecondId.toString())) {
            report.setVolSecond(secondWill);
            report.setReasonSecond(secondReason);
        }
        report.setRemark(remark);

        // 保存志愿信息
        reportService.update(report);

        return new ResultVO(0, "更新成功");
    }

    /**
     * 7. 更新志愿信息接口 (超级管理员)
     */
    @PostMapping("/update_ROOT")
    @Transactional
    public ResultVO update_ROOT(@RequestParam("stuId") String stuId, @RequestParam("majorId") Integer majorId,
                                @RequestParam("classNum") Integer classNumber, @RequestParam("stdQQ") String studentQQ,
                                @RequestParam("stdPhone") String studentPhone, @RequestParam("firstWill") Integer firstWill,
                                String firstReason, Integer secondWill, String secondReason,
                                @RequestParam("isDispensing") Boolean isDispensing, @RequestParam("remark") String remark) {

        log.info("[report/update_ROOT]更新志愿[{}]，操作者id[{}]", stuId, UserIdHolder.get());

        // 查询学生
        Student student = studentService.findById(new Integer(stuId));

        // 查询志愿
        Report report = reportService.findEntityByStuId(new Integer(stuId));

        if (report.getStatus() != EnrollStatusEnum.UNDER_ENROLL.getCode()) {
            log.error("【更新志愿失败】该学生已被录取，不允许修改志愿，学生号:{} ", stuId);
            return new ResultVO(1, "更新失败");
        }

        student.setStuId(new Integer(stuId));
        student.setMajorId(majorId);
        student.setStuClass(classNumber);
        student.setStuQq(studentQQ);
        student.setStuPhone(studentPhone);

        // 保存学生信息
        studentService.update(student);

        report.setStudentId(new Integer(stuId));

        if (firstWill != null && !firstReason.isEmpty()) {
            report.setVolFirst(firstWill);
            report.setReasonFirst(firstReason);
        }

        if (secondWill != null && !secondReason.isEmpty()) {
            report.setVolSecond(secondWill);
            report.setReasonSecond(secondReason);
        }

        report.setDispensing(isDispensing);
        report.setRemark(remark);

        // 保存志愿信息
        reportService.update(report);

        return new ResultVO(0, "更新成功");
    }

    /**
     * 8.删除一条志愿信息接口 (超级管理员身份)
     *
     * @param stuId
     * @return
     */
    @PostMapping("/delete")
    @Transactional
    public ResultVO delete_ROOT(@RequestParam("stuId") Integer stuId) {

        log.info("[report/delete]删除志愿[{}]，操作者id[{}]", stuId, UserIdHolder.get());

        reportService.deleteByStuId(stuId);
        studentService.deleteById(stuId);
        return new ResultVO(0, "删除成功");
    }
}
