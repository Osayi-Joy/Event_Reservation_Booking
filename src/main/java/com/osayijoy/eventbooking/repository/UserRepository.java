package com.osayijoy.eventbooking.repository;

import com.osayijoy.eventbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Joy Osayi
 * @createdOn Jun-26(Wed)-2024
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findFirstByEmailOrderByCreatedDate(String username);
}
