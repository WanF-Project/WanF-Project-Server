package com.capstone.wanf.profile.domain.repo;

import com.capstone.wanf.profile.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
