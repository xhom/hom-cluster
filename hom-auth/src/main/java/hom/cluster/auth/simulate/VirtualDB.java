package hom.cluster.auth.simulate;

import hom.cluster.auth.model.LocalUser;
import hom.cluster.auth.model.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author visy.wang
 * @description: 模拟数据库
 * @date 2023/5/25 12:52
 */
public class VirtualDB {
    private static final List<LocalUser> userList = new ArrayList<>();
    private static final List<Permission> permissionList = new ArrayList<>();

    static {
        userList.add(new LocalUser(1L, "zhangSan", "123456"));
        userList.add(new LocalUser(2L, "liSi", "123abc"));
        userList.add(new LocalUser(3L, "wangWu", "abc123"));

        permissionList.add(new Permission(1L, 1L, "User"));
        permissionList.add(new Permission(2L, 1L, "Admin"));
        permissionList.add(new Permission(3L, 2L, "User"));
        permissionList.add(new Permission(4L, 3L, "Admin"));
    }

    public static LocalUser getUser(String name){
        return userList.stream().filter(u -> u.getUsername().equals(name)).findFirst().orElse(null);
    }

    public static List<Permission> getPermissions(Long userId){
        return permissionList.stream().filter(p -> p.getUserId().equals(userId)).collect(Collectors.toList());
    }

}
