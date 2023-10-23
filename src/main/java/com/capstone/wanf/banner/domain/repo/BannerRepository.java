package com.capstone.wanf.banner.domain.repo;

import com.capstone.wanf.banner.domain.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
