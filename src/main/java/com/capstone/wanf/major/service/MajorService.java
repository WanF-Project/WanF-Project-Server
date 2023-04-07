package com.capstone.wanf.major.service;

import com.capstone.wanf.major.domain.entity.Major;
import com.capstone.wanf.major.domain.entity.repo.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

    public Major findById(Long id) {
        Major major = majorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("선택한 전공이 존재하지 않습니다."));

        return major;
    }

}
