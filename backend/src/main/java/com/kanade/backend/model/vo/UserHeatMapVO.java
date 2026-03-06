package com.kanade.backend.model.vo;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHeatMapVO {
    private LocalDate date;
    private Integer count;
    private Integer level;

}
