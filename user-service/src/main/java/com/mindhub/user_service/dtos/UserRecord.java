package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.UserRol;

public record UserRecord(Long id,String username, String email, UserRol rol) {
}
