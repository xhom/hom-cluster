package hom.cluster.service.a.model;

import lombok.Data;

import java.util.Date;

/**
 * @author visy.wang
 * @description: 登录用户信息
 * @date 2023/5/27 14:11
 */
@Data
public class LoginUser {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date createDt;
    private Date updateDt;
    private Date lastLoginDt;
}
