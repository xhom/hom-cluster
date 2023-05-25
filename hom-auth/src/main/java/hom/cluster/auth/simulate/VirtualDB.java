package hom.cluster.auth.simulate;

import hom.cluster.auth.model.Permission;
import hom.cluster.common.dao.entity.LocalUser;

import java.util.ArrayList;
import java.util.Date;
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
        Date now = new Date();
        userList.add(new LocalUser(1L, "zhangSan", "123456", "zs@q.com", "17712338977", now, now, null));
        userList.add(new LocalUser(2L, "liSi", "123abc", "ls@q.com", "19912338977", now, now, null));
        userList.add(new LocalUser(3L, "wangWu", "abc123", "ww@q.com", "18812338977", now, now, null));

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
