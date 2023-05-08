package org.example.config.security;

import lombok.RequiredArgsConstructor;
import org.example.repository.AuthUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SessionUser {
    private final AuthUserRepository userRepository;

    public UserDetails user() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }


    public Integer getId() {
        UserDetails user = user();
        if (Objects.isNull(user)) {
            return -1;
        }
        return user.getId();
    }

}
