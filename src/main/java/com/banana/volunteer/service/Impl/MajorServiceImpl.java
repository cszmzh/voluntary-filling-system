package com.banana.volunteer.service.Impl;

import com.banana.volunteer.entity.Major;
import com.banana.volunteer.enums.ResultEnum;
import com.banana.volunteer.exception.BusinessException;
import com.banana.volunteer.repository.MajorRepository;
import com.banana.volunteer.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorRepository repository;

    @Override
    public List<Major> findAll() {
        return repository.findAll();
    }

    @Override
    public Major update(Major major) {
        return repository.saveAndFlush(major);
    }

    @Override
    public void delete(Integer majorId) {
        repository.deleteById(majorId);
    }

    @Override
    public Major create(Major major) {
        if (!repository.findById(major.getMajorId()).isPresent()) {
            return repository.saveAndFlush(major);
        } else {
            throw new BusinessException(ResultEnum.ID_DUPLICATE);
        }
    }
}
