package hom.cluster.auth.model;

import lombok.Data;

/**
 * @author visy.wang
 * @description: 权限
 * @date 2023/5/24 13:22
 */
@Data
public class Permission {
    private Long id;
    private Long userId;
    private String code;
}
