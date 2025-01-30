package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.UserRole;

public record UserRecord(Long id,String username, String email, UserRole rol) {
}
