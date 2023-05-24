package hom.cluster.auth.service;

import hom.cluster.auth.model.Permission;

import java.util.List;

/**
 * @author visy.wang
 * @description: 权限服务
 * @date 2023/5/24 13:23
 */
public interface PermissionService {

    /**
     * 查询用户的权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> listPermissionsByUserId(Long userId);

}
