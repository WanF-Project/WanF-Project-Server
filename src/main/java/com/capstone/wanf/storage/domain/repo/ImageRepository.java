package com.capstone.wanf.storage.domain.repo;

import com.capstone.wanf.storage.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
