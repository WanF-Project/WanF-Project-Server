package com.capstone.wanf.profile.domain.repo;

import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}
