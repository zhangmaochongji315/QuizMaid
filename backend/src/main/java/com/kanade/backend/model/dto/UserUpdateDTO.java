package com.kanade.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserUpdateDTO {
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;


}
