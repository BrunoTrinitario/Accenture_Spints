package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.UserRol;

public record NewUserRecord(String username,String password, String email) {
}
