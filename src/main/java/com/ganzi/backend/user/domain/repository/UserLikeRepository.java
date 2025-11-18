package com.ganzi.backend.user.domain.repository;

import com.ganzi.backend.user.domain.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {

    Optional<UserLike> findByUserIdAndAnimal_DesertionNo(Long userId, String desertionNo);

    List<UserLike> findByUserIdAndLikedTrue(Long userId);
}