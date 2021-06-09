package com.banana.volunteer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.banana.volunteer.entity.Report;
import com.banana.volunteer.entity.Student;
import com.banana.volunteer.enums.EnrollStatusEnum;
import com.banana.volunteer.service.ReportService;
import com.banana.volunteer.service.StudentService;
import com.github.javafaker.Faker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Locale;

@SpringBootTest
@RunWith(SpringRunner.class)
class DataTestInput {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ReportService reportService;

    @Test
    public void test() throws IOException {

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = new Request.Builder()
//                .url("http://127.0.0.1:8095/stuInfo2.json")
//                .method("GET", null)
//                .build();
//        Response response = client.newCall(request).execute();
//
//        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
//
//        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject child = jsonArray.getJSONObject(i);
//            Integer stuId = new Integer(child.getString("XH"));
//            if (stuId < 20187000 && stuId > 20185267) {
//
//                Faker faker = new Faker(new Locale("zh-CN"));
//
//                Student student = new Student();
//                Report report = new Report();
//
//                // 保存学生信息
//                student.setStuId(stuId);
//                student.setStuName(child.getString("NAME"));
//                student.setMajorId(faker.random().nextInt(1, 6));
//                student.setStuClass(faker.random().nextInt(1, 3));
//                student.setStuQq(faker.random().nextInt(135200102, 950250130).toString());
//                if (child.getString("SJH") != null) {
//                    student.setStuPhone(child.getString("SJH"));
//                } else {
//                    student.setStuPhone(faker.phoneNumber().cellPhone());
//                }
//                studentService.save(student);
//
//                // 保存志愿信息
//                report.setStudentId(stuId);
//                report.setVolFirst(faker.random().nextInt(10000, 10018));
//
//                String reasonFirst = "我是" + child.getString("NAME") + "，我出生在" + faker.country().name() +
//                        "，喜欢吃" + faker.food().fruit() + "，将来想成为一个" + faker.job().position() + "，就读于" +
//                        faker.university().name() + "，喜欢乐器:" + faker.music().instrument() + "，喜欢玩" + faker.esports().game() +
//                        "，喜欢看书，特别是" + faker.book().author() + "写的，今天天气" + faker.weather().description() +
//                        "，所以我想加入这个组织，谢谢。";
//
//                report.setReasonFirst(reasonFirst);
//
//                if (faker.random().nextInt(1, 2) == 2) {
//                    report.setVolSecond(faker.random().nextInt(10000, 10018));
//                    String rasonSecond = "我是" + child.getString("NAME") + "，我出生在" + faker.country().name() +
//                            "，喜欢吃" + faker.food().fruit() + "，将来想成为一个" + faker.job().position() + "，就读于" +
//                            faker.university().name() + "，喜欢乐器:" + faker.music().instrument() + "，喜欢玩" + faker.esports().game() +
//                            "，喜欢看书，特别是" + faker.book().author() + "写的，今天天气" + faker.weather().description() +
//                            "，所以我想加入这个组织，谢谢。";
//                    report.setReasonSecond(rasonSecond);
//                }
//
//                report.setStatus(EnrollStatusEnum.UNDER_ENROLL.getCode());
//                if (faker.random().nextInt(0, 1) == 1) {
//                    report.setDispensing(true);
//                } else {
//                    report.setDispensing(false);
//                }
//                reportService.save(report);
//            }
//        }
    }
}