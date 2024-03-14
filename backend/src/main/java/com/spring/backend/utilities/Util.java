package com.spring.backend.utilities;

import com.spring.backend.exception.BusinessException;
import com.spring.backend.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class Util {

    private Util() {
    }

    public static UserDetailsImpl getUserAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User logged info  " + principal);
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal);
        }
        throw new BusinessException("User is not authenticated");
    }
}
