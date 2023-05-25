package hom.cluster.auth.service.impl;

import hom.cluster.auth.model.Permission;
import hom.cluster.auth.service.PermissionService;
import hom.cluster.auth.simulate.VirtualDB;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author visy.wang
 * @description: 权限服务实现
 * @date 2023/5/24 13:23
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Override
    public List<Permission> listPermissionsByUserId(Long userId) {
        return VirtualDB.getPermissions(userId);
    }
}
