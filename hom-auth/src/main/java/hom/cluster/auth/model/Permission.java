package hom.cluster.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author visy.wang
 * @description: 权限
 * @date 2023/5/24 13:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private Long userId;
    private String code;
}
