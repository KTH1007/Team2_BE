package com.ganzi.backend.global.config;

import com.ganzi.backend.global.security.userdetails.CustomUserDetails;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<Long> {
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals(ANONYMOUS_USER)) {
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails.getUser().getId());
        }

        return Optional.empty();
    }
}
