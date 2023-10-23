package com.capstone.wanf.banner.service;

import com.capstone.wanf.banner.domain.entity.Banner;
import com.capstone.wanf.banner.domain.repo.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }
}
