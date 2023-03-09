package com.ray.domain.vo;

import com.ray.domain.entity.Role;
import com.ray.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleAndIdsVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserVo user;
}
