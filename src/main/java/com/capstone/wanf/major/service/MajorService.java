package com.capstone.wanf.major.service;

import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.major.domain.entity.Major;
import com.capstone.wanf.major.domain.entity.repo.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.MAJOR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

    public Major findById(Long id) {
        Major major = majorRepository.findById(id).orElseThrow(() -> new RestApiException(MAJOR_NOT_FOUND));

        return major;
    }
}
