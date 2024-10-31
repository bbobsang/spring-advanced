package org.example.expert.domain.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.example.expert.domain.user.entity.User;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(org.example.expert.domain.common.annotation.Logging)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = (HttpServletRequest) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        long userId = getCurrentUserId(); // 사용자 ID 가져오기
        String url = request.getRequestURI(); // 요청 URL
        long start = System.currentTimeMillis();

        Object result; // 메서드 결과를 저장할 변수
        try {
            result = joinPoint.proceed(); // 메서드 실행
            return result; // 실행 결과 반환
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            logger.info("User ID: {} - Executed {} in {} ms", userId, url, executionTime);
        }
    }

    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal(); // UserDetails 구현체가 User인 경우
            return user.getId(); // 사용자 ID 반환
        }
        throw new RuntimeException("User not authenticated"); // 인증되지 않은 경우 예외 처리
    }
}
