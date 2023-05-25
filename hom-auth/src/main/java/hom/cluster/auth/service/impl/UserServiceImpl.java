package hom.cluster.auth.service.impl;

import hom.cluster.auth.model.LocalUser;
import hom.cluster.auth.service.UserService;
import hom.cluster.auth.simulate.VirtualDB;
import org.springframework.stereotype.Service;

/**
 * @author visy.wang
 * @description: 用户服务实现
 * @date 2023/5/24 13:23
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public LocalUser getUserByUsername(String username) {
        return VirtualDB.getUser(username);
    }
}
