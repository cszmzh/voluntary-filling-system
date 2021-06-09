package com.banana.volunteer.service;

import com.banana.volunteer.entity.Major;

import java.util.List;

public interface MajorService {
    /**
     * 1.获取所有专业信息
     *
     * @return 专业列表
     */
    List<Major> findAll();

    /**
     * 2.更新专业信息
     *
     * @return 专业实体
     */
    Major update(Major major);

    /**
     * 3.删除专业
     *
     * @param majorId 专业id
     */
    void delete(Integer majorId);

    /**
     * 4.创建专业
     *
     * @param major 专业实体
     * @return 专业实体
     */
    Major create(Major major);
}
