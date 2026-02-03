package com.example.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rbac.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户权限
     */
    @Select("SELECT DISTINCT m.perms " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.role_id " +
            "LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id " +
            "WHERE u.username = #{username} AND m.perms IS NOT NULL AND m.perms != '' " +
            "AND u.del_flag = 0 AND r.del_flag = 0 AND m.del_flag = 0")
    List<String> selectPermissionsByUsername(@Param("username") String username);

    /**
     * 根据用户名查询用户角色
     */
    @Select("SELECT r.data_scope " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.role_id " +
            "WHERE u.username = #{username} AND u.del_flag = 0 AND r.del_flag = 0 " +
            "ORDER BY r.role_sort LIMIT 1")
    String selectDataScopeByUsername(@Param("username") String username);

    /**
     * 根据用户名查询角色标识列表
     */
    @Select("SELECT DISTINCT r.role_key " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.role_id " +
            "WHERE u.username = #{username} AND u.del_flag = 0 AND r.del_flag = 0")
    List<String> selectRoleKeysByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询角色ID列表
     */
    @Select("SELECT ur.role_id FROM sys_user_role ur WHERE ur.user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}

