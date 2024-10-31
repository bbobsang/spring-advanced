package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private static final Logger logger = LoggerFactory.getLogger(UserAdminService.class);
    private final UserRepository userRepository;

    @Transactional
    public void changeUserRole(long userId, UserRoleChangeRequest userRoleChangeRequest) {
        // 로그 기록 시작
        logAccess(userId, "사용자 역할 변경 요청");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User ID: {} - 사용자 정보를 찾을 수 없습니다.", userId);
                    return new InvalidRequestException("User not found");
                });

        user.updateRole(UserRole.of(userRoleChangeRequest.getRole()));

        // 로그 기록 종료
        logAccess(userId, "사용자 역할 변경 완료");
    }

    // 로그 기록 메서드
    private void logAccess(long userId, String message) {
        logger.info("User ID: {} - {}", userId, message);
    }
}
