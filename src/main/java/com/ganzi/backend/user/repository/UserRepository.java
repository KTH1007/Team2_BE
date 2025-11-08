package com.ganzi.backend.user.repository;

import com.ganzi.backend.global.oauth.SocialProvider;
import com.ganzi.backend.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findBySocialProviderAndOauthId(SocialProvider socialProvider, String oauthId);
}
