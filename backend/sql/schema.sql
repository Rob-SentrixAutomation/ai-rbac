-- 创建数据库
CREATE DATABASE IF NOT EXISTS rbac_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE rbac_system;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户昵称',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '用户性别(0男 1女 2未知)',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '密码',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int DEFAULT 0 COMMENT '状态(0正常 1停用)',
  `del_flag` int DEFAULT 0 COMMENT '删除标志(0存在 1删除)',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int DEFAULT 0 COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '数据范围(1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5仅本人数据权限)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int DEFAULT 0 COMMENT '状态(0正常 1停用)',
  `del_flag` int DEFAULT 0 COMMENT '删除标志(0存在 1删除)',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色信息表';

-- 菜单权限表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '菜单类型(M目录 C菜单 F按钮)',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单状态(0显示 1隐藏)',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '#' COMMENT '菜单图标',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int DEFAULT 0 COMMENT '状态(0正常 1停用)',
  `del_flag` int DEFAULT 0 COMMENT '删除标志(0存在 1删除)',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

-- 部门表
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint DEFAULT 0 COMMENT '父部门ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int DEFAULT 0 COMMENT '状态(0正常 1停用)',
  `del_flag` int DEFAULT 0 COMMENT '删除标志(0存在 1删除)',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联表';

-- 角色部门关联表(数据权限)
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色和部门关联表';

-- 初始化部门数据
INSERT INTO `sys_dept` VALUES (100, 0, '0', '总公司', 0, '管理员', '15888888888', 'admin@example.com', NULL, 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '研发部门', 1, '研发负责人', '15888888888', 'rd@example.com', NULL, 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '市场部门', 2, '市场负责人', '15888888888', 'market@example.com', NULL, 'admin', now(), '', now(), 0, 0);

-- 初始化用户数据(密码: admin123)
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '管理员', 'admin@example.com', '15888888888', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.qYCrm/NW', '管理员', 'admin', now(), '', now(), 0, 0);

-- 初始化角色数据
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', '超级管理员', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '5', '普通角色', 'admin', now(), '', now(), 0, 0);

-- 初始化菜单数据
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, 'M', '0', NULL, 'system', '系统管理目录', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, 1, 'user', 'system/user/index', 'C', '0', 'system:user:list', 'user', '用户管理菜单', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (3, '角色管理', 1, 2, 'role', 'system/role/index', 'C', '0', 'system:role:list', 'peoples', '角色管理菜单', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (4, '菜单管理', 1, 3, 'menu', 'system/menu/index', 'C', '0', 'system:menu:list', 'tree-table', '菜单管理菜单', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (5, '部门管理', 1, 4, 'dept', 'system/dept/index', 'C', '0', 'system:dept:list', 'tree', '部门管理菜单', 'admin', now(), '', now(), 0, 0);

INSERT INTO `sys_menu` VALUES (100, '用户查询', 2, 1, '', '', 'F', '0', 'system:user:query', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (101, '用户新增', 2, 2, '', '', 'F', '0', 'system:user:add', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (102, '用户修改', 2, 3, '', '', 'F', '0', 'system:user:edit', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (103, '用户删除', 2, 4, '', '', 'F', '0', 'system:user:remove', '#', '', 'admin', now(), '', now(), 0, 0);

-- 角色管理按钮
INSERT INTO `sys_menu` VALUES (110, '角色查询', 3, 1, '', '', 'F', '0', 'system:role:query', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (111, '角色新增', 3, 2, '', '', 'F', '0', 'system:role:add', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (112, '角色修改', 3, 3, '', '', 'F', '0', 'system:role:edit', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (113, '角色删除', 3, 4, '', '', 'F', '0', 'system:role:remove', '#', '', 'admin', now(), '', now(), 0, 0);

-- 菜单管理按钮
INSERT INTO `sys_menu` VALUES (120, '菜单查询', 4, 1, '', '', 'F', '0', 'system:menu:query', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (121, '菜单新增', 4, 2, '', '', 'F', '0', 'system:menu:add', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (122, '菜单修改', 4, 3, '', '', 'F', '0', 'system:menu:edit', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (123, '菜单删除', 4, 4, '', '', 'F', '0', 'system:menu:remove', '#', '', 'admin', now(), '', now(), 0, 0);

-- 部门管理按钮
INSERT INTO `sys_menu` VALUES (130, '部门查询', 5, 1, '', '', 'F', '0', 'system:dept:query', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (131, '部门新增', 5, 2, '', '', 'F', '0', 'system:dept:add', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (132, '部门修改', 5, 3, '', '', 'F', '0', 'system:dept:edit', '#', '', 'admin', now(), '', now(), 0, 0);
INSERT INTO `sys_menu` VALUES (133, '部门删除', 5, 4, '', '', 'F', '0', 'system:dept:remove', '#', '', 'admin', now(), '', now(), 0, 0);

-- 超级管理员全权限标识
INSERT INTO `sys_menu` VALUES (900, '不限制（超级权限）', 1, 99, '', '', 'F', '0', '*:*', '#', '拥有所有权限标识', 'admin', now(), '', now(), 0, 0);

-- 初始化用户角色关联
INSERT INTO `sys_user_role` VALUES (1, 1);

-- 初始化角色菜单关联
INSERT INTO `sys_role_menu` VALUES (1, 900);
