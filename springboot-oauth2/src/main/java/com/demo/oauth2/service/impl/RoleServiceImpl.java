package com.demo.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.oauth2.bean.RoleEntity;
import com.demo.oauth2.bean.UserRole;
import com.demo.oauth2.dao.RoleMapper;
import com.demo.oauth2.dao.UserRoleMapper;
import com.demo.oauth2.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yangjie.Chen
 * @description RoleService 接口实现类
 * @date 2020/3/12
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper urMapper;


    @Override
    public List<RoleEntity> listByUser(String userId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        QueryWrapper<UserRole> urWrapper = new QueryWrapper<>(userRole);
        List<UserRole> userRoles = urMapper.selectList(urWrapper);

        if (!CollectionUtils.isEmpty(userRoles)) {
            List<Integer> roleIds = userRoles.stream()
                    .map(UserRole::getRoleId)
                    .collect(Collectors.toList());

            return roleMapper.selectBatchIds(roleIds);
        }
        return null;
    }
}
