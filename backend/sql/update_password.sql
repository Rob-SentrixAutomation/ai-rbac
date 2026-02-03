-- 更新管理员密码
-- 密码: admin123
-- 注意：这是使用BCrypt加密的新密码哈希

UPDATE sys_user SET password = '$2a$10$OQIcPt.OpNwBgCulyW2wa.yW6YoCGqf9Ak6kZBrwir3mZj0OjfRWi' WHERE username = 'admin';

-- 验证更新
SELECT user_id, username, SUBSTRING(password, 1, 20) as password_prefix, status FROM sys_user WHERE username = 'admin';
