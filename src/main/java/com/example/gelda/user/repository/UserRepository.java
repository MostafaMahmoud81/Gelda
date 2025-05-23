package com.example.gelda.user.repository;


import com.example.gelda.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByMobileNumber(String mobileNumber);

    Optional<User> findByNationalId(String nationalId);
}
