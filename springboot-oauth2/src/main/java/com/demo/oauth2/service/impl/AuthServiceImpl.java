package com.demo.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.oauth2.bean.AuthorityEntity;
import com.demo.oauth2.bean.RoleAuth;
import com.demo.oauth2.dao.AuthMapper;
import com.demo.oauth2.dao.RoleAuthMapper;
import com.demo.oauth2.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yangjie.Chen
 * @description 权限接口实现类
 * @date 2020/3/12
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private RoleAuthMapper raMapper;

    @Override
    public List<AuthorityEntity> listByRole(Integer roleId) {
        RoleAuth roleAuth = new RoleAuth();
        roleAuth.setRoleId(roleId);
        QueryWrapper<RoleAuth> raWrapper = new QueryWrapper<>(roleAuth);
        List<RoleAuth> ras = raMapper.selectList(raWrapper);

        if (!CollectionUtils.isEmpty(ras)) {
            List<Integer> authIds = ras.stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
            return authMapper.selectBatchIds(authIds);
        }
        return null;
    }
}
