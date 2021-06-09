package com.banana.volunteer.service;

import com.banana.volunteer.entity.Student;

public interface StudentService {

    /**
     * 1.保存学生
     *
     * @param student 学生实体
     * @return 学生实体
     */
    Student save(Student student);

    /**
     * 2.通过学号查找学生
     *
     * @param stuId 学号
     * @return 学生实体
     */
    Student findById(Integer stuId);

    /**
     * 3.录取学生
     *
     * @param stuId 学号
     * @param flag  志愿 1/2
     */
    boolean enroll(Integer stuId, Integer flag);

    /**
     * 4.更新学生信息
     *
     * @param student 学生实体
     * @return 学生实体
     */
    Student update(Student student);

    /**
     * 5.删除学生信息
     *
     * @param stdId 学号
     */
    void deleteById(Integer stdId);
}
