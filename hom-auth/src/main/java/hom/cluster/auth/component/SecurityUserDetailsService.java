package hom.cluster.auth.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.common.utils.CollectionUtils;
import hom.cluster.auth.model.Permission;
import hom.cluster.auth.service.PermissionService;
import hom.cluster.auth.service.UserService;
import hom.cluster.common.dao.entity.LocalUser;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LocalUser localUser = userService.getUserByUsername(username);
        if (Objects.isNull(localUser)) {
            throw new UsernameNotFoundException("user not exists");
        }

        //获取权限
        List<Permission> permissions = permissionService.listPermissionsByUserId(localUser.getId());
        String[] authorities = {};
        if (CollectionUtils.isNotEmpty(permissions)) {
            List<String> codes = permissions.stream().map(Permission::getCode).collect(Collectors.toList());
            authorities = new String[codes.size()];
            codes.toArray(authorities);
        }

        //正常情况下应该先加密后存在数据库，而不是存明文，然后在此处加密
        String password = passwordEncoder.encode(localUser.getPassword());

        //身份令牌
        JSONObject principal = new JSONObject();
        principal.put("userid", localUser.getId());
        principal.put("username", localUser.getUsername());
        String principalJson = JSON.toJSONString(principal, SerializerFeature.WriteMapNullValue);
        System.out.println("principal:" + principalJson);
        return User.withUsername(principalJson)
                .password(password)
                .authorities(authorities)
                //.accountExpired(true)//账号过期
                //.accountLocked(true)//账号锁定
                //.credentialsExpired(true)//证书过期
                .build();
    }
}
