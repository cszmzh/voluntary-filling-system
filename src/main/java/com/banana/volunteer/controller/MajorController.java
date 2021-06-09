package com.banana.volunteer.controller;

import com.banana.volunteer.VO.ResultVO;
import com.banana.volunteer.entity.Major;
import com.banana.volunteer.holder.UserIdHolder;
import com.banana.volunteer.service.MajorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/major")
@Slf4j
public class MajorController {

    @Autowired
    private MajorService majorService;

    /**
     * 1.获取所有专业
     */
    @GetMapping("/getAll")
    public ResultVO getAllMajor() {
        List<Major> majorList = majorService.findAll();
        return new ResultVO(0, "success", majorList);
    }

    /**
     * 2.更新专业
     */
    @PostMapping("/update")
    public ResultVO updateMajor_ROOT(@RequestParam("majorId") Integer majorId, @RequestParam("majorName") String majorName,
                                     @RequestParam("classNum") Integer classNum) {

        log.info("[major/update]更新专业id[{}]，操作者id[{}]", majorId, UserIdHolder.get());

        Major major = new Major();
        major.setMajorId(majorId);
        major.setMajorName(majorName);
        major.setClassNum(classNum);
        return new ResultVO(0, "success", majorService.update(major));
    }

    /**
     * 3.删除专业
     */
    @PostMapping("/delete")
    public ResultVO deleteMajor_ROOT(@RequestParam("majorId") Integer majorId) {

        log.info("[major/delete]更新专业id[{}]，操作者id[{}]", majorId, UserIdHolder.get());

        majorService.delete(majorId);
        return new ResultVO(0, "success");
    }

    /**
     * 4.创建专业
     */
    @PostMapping("/create")
    public ResultVO createMajor_ROOT(@RequestParam("majorId") Integer majorId, @RequestParam("majorName") String majorName,
                                     @RequestParam("classNum") Integer classNum) {

        log.info("[major/create]创建专业id[{}]，操作者id[{}]", majorId, UserIdHolder.get());

        Major major = new Major();
        major.setMajorId(majorId);
        major.setMajorName(majorName);
        major.setClassNum(classNum);
        return new ResultVO(0, "success", majorService.create(major));
    }
}
