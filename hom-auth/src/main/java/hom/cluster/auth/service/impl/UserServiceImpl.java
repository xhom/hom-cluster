package hom.cluster.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import hom.cluster.auth.service.UserService;
import hom.cluster.common.dao.entity.LocalUser;
import hom.cluster.common.dao.mapper.LocalUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author visy.wang
 * @description: 用户服务实现
 * @date 2023/5/24 13:23
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private LocalUserMapper localUserMapper;

    @Override
    public LocalUser getUserByUsername(String username) {
        return localUserMapper.selectOne(new QueryWrapper<LocalUser>().lambda().eq(LocalUser::getUsername, username));
    }
}
