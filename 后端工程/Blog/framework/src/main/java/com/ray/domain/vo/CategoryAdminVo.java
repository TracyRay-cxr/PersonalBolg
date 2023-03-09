package com.ray.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAdminVo {
    private Long id;
    private String name;
    private String description;
    private String status;
}
