package hom.cluster.auth.model;

import lombok.Data;

/**
 * @author visy.wang
 * @description: 用户
 * @date 2023/5/24 13:20
 */
@Data
public class LocalUser {
    private Long id;
    private String username;
    private String password;
}
