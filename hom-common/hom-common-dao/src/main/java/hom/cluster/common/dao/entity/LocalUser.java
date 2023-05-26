package hom.cluster.common.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author visy.wang
 * @description: 本地用户
 * @date 2023/5/25 16:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalUser {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date createDt;
    private Date updateDt;
    private Date lastLoginDt;
}
