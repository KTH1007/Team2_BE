package com.ganzi.backend.user.domain.repository;

import com.ganzi.backend.user.domain.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.id = :userId ORDER BY ui.createdAt ASC")
    List<UserInterest> findByUserId(@Param("userId") Long userId);
}
