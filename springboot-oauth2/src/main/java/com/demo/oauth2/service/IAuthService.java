package com.demo.oauth2.service;

import com.demo.oauth2.bean.AuthorityEntity;

import java.util.List;

/**
 * @author Yangjie.Chen
 * @description 权限接口
 * @date 2020/3/12
 */
public interface IAuthService {

    /**
     * 查询角色拥有的权限
     * @param roleId 角色id
     * @return
     */
    List<AuthorityEntity> listByRole(Integer roleId);
}
