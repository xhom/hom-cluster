package hom.cluster.auth.service.impl;

import hom.cluster.auth.model.Permission;
import hom.cluster.auth.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author visy.wang
 * @description: 权限服务实现
 * @date 2023/5/24 13:23
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private static final List<Permission> permissionList = new ArrayList<>();

    static {
        //这部分数据实际应该存在数据库中
        permissionList.add(new Permission(1L, 1L, "User"));
        permissionList.add(new Permission(2L, 1L, "Admin"));
        permissionList.add(new Permission(3L, 2L, "User"));
        permissionList.add(new Permission(4L, 3L, "Admin"));
    }

    @Override
    public List<Permission> listPermissionsByUserId(Long userId) {
        return  permissionList.stream().filter(p -> p.getUserId().equals(userId)).collect(Collectors.toList());
    }
}
