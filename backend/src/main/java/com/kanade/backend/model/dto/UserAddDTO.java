package com.kanade.backend.model.dto;

import lombok.Data;

@Data
public class UserAddDTO {
    private String username;
    private String nickname;
    private String role;
}
