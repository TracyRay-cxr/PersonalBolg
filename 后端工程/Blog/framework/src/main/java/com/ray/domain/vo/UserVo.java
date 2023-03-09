package com.ray.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
//@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 昵称
     */
    private String nickName;

    private String sex;
    private String email;
    private String status;
    private String userName;
    private String phonenumber;
}
