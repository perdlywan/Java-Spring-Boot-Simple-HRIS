package com.example.simple_hris.config.audit;

import com.example.simple_hris.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // CASE 1: belum login (register, init data)
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            return Optional.of("SYSTEM");
        }

        // CASE 2: sudah login
        Object principal = auth.getPrincipal();

        if (principal instanceof User user) {
            return Optional.of(user.getUsername());
        }

        return Optional.of("SYSTEM");
    }
}
