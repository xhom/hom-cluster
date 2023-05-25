package hom.cluster.auth.service;


import hom.cluster.common.dao.entity.LocalUser;

/**
 * @author visy.wang
 * @description: 用户服务
 * @date 2023/5/24 13:22
 */
public interface UserService {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    LocalUser getUserByUsername(String username);
}
