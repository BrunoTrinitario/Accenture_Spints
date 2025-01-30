package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.UserRole;
import com.mindhub.user_service.models.UserStatus;

public record UserRegistrationRecord(Long id, String username, String email, UserRole rol, UserStatus userStatus) {
}
