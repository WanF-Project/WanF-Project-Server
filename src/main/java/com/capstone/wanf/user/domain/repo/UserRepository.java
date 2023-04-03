package com.capstone.wanf.user.domain.repo;

import com.capstone.wanf.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndVerificationCode(String email, String verificationCode);

    Optional<User> findByEmail(String email);
}