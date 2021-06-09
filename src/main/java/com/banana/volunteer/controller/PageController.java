package com.banana.volunteer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    // 通过controller返回html界面
    @RequestMapping("/index")
    public String indexJumpPage() {
        return "index";
    }

    @RequestMapping("/login")
    public String loginJumpPage() {
        return "login";
    }

    @RequestMapping("/data")
    public String dataJumpPage() {
        return "data";
    }

    @RequestMapping("/reportDetail")
    public String reportDetailJumpPage() {
        return "reportDetail";
    }

    @RequestMapping("/submitReport")
    public String submitReportJumpPage() {
        return "submitReport";
    }

    @RequestMapping("/authority")
    public String authorityJumpPage() {
        return "authority.html";
    }

    @RequestMapping("/major")
    public String majorJumpPage() {
        return "major.html";
    }

    @RequestMapping("/organization")
    public String organizationJumpPage() {
        return "organization.html";
    }

    @RequestMapping("/branch")
    public String branchJumpPage() {
        return "branch.html";
    }
}
