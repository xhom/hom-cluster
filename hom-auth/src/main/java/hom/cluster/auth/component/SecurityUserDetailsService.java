package hom.cluster.auth.component;

import com.alibaba.nacos.common.utils.CollectionUtils;
import hom.cluster.auth.model.Permission;
import hom.cluster.auth.model.LocalUser;
import hom.cluster.auth.service.PermissionService;
import hom.cluster.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author visy.wang
 * @description: 用户信息查询服务实现
 * @date 2023/5/23 18:02
 */
@Component
public class SecurityUserDetailsService implements UserDetailsService {
    /*@Autowired
    private PasswordEncoder passwordEncoder;*/

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*String pwd = "123456";
        String password = passwordEncoder.encode(pwd);
        return User.withUsername(username).password(password).authorities("admin","user").build();*/

        LocalUser localUser = userService.getUserByUsername(username);
        if (Objects.isNull(localUser)) {
            return null;
        }
        //获取权限
        List<Permission> permissions = permissionService.listPermissionsByUserId(localUser.getId());
        String[] authorities = {};
        if (CollectionUtils.isNotEmpty(permissions)) {
            List<String> codes = permissions.stream().map(Permission::getCode).collect(Collectors.toList());
            authorities = new String[codes.size()];
            codes.toArray(authorities);
        }
        //身份令牌
        //String principal = JSON.toJSONString(localUser);
        return User.withUsername(username).password(localUser.getPassword()).authorities(authorities).build();
    }


}
