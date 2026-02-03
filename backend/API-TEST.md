# API测试指南

## 测试环境
- 基础URL: `http://localhost:8080/api`
- 默认账号: `admin`
- 默认密码: `admin123`

## 1. 登录获取Token

**请求:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": "eyJhbGciOiJIUzI1NiJ9..."
}
```

将返回的`data`字段(Token)复制,后续请求需要携带此Token。

## 2. 携带Token访问接口

所有需要认证的接口都需要在请求头中携带Token:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 3. 用户管理API测试

### 3.1 查询用户列表
```http
GET /api/system/user/list?pageNum=1&pageSize=10
Authorization: Bearer {your_token}
```

### 3.2 查询用户详情
```http
GET /api/system/user/1
Authorization: Bearer {your_token}
```

### 3.3 新增用户
```http
POST /api/system/user
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "username": "test001",
  "nickname": "测试用户",
  "password": "123456",
  "deptId": 100,
  "email": "test@example.com",
  "phone": "13800138000",
  "sex": "0",
  "status": 0
}
```

### 3.4 修改用户
```http
PUT /api/system/user
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "userId": 2,
  "nickname": "测试用户修改",
  "email": "test2@example.com"
}
```

### 3.5 删除用户
```http
DELETE /api/system/user/2
Authorization: Bearer {your_token}
```

## 4. 角色管理API测试

### 4.1 查询角色列表
```http
GET /api/system/role/list?pageNum=1&pageSize=10
Authorization: Bearer {your_token}
```

### 4.2 新增角色
```http
POST /api/system/role
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "roleName": "测试角色",
  "roleKey": "test",
  "roleSort": 3,
  "dataScope": "5",
  "status": 0
}
```

## 5. 菜单管理API测试

### 5.1 查询菜单列表
```http
GET /api/system/menu/list
Authorization: Bearer {your_token}
```

### 5.2 新增菜单
```http
POST /api/system/menu
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "menuName": "测试管理",
  "parentId": 0,
  "orderNum": 10,
  "path": "test",
  "menuType": "M",
  "visible": "0",
  "perms": "system:test:list",
  "icon": "test",
  "status": 0
}
```

## 6. 部门管理API测试

### 6.1 查询部门列表
```http
GET /api/system/dept/list
Authorization: Bearer {your_token}
```

### 6.2 新增部门
```http
POST /api/system/dept
Authorization: Bearer {your_token}
Content-Type: application/json

{
  "parentId": 100,
  "ancestors": "0,100",
  "deptName": "测试部门",
  "orderNum": 3,
  "leader": "测试负责人",
  "phone": "13800138000",
  "email": "test@example.com",
  "status": 0
}
```

## 7. 登出
```http
POST /api/auth/logout
Authorization: Bearer {your_token}
```

## 注意事项

1. **Token过期**: Token有效期为24小时,过期后需要重新登录
2. **权限不足**: 如果返回403,说明当前用户没有该接口的权限
3. **参数校验**: 必填参数不能为空,否则返回400错误
4. **数据权限**: 用户只能看到其数据权限范围内的数据

## 使用Knife4j测试

推荐使用Knife4j在线文档进行测试:

1. 访问: http://localhost:8080/api/doc.html
2. 点击右上角"文档管理" -> "全局参数设置"
3. 添加参数:
   - 参数名: `Authorization`
   - 参数值: `Bearer {your_token}`
   - 参数位置: header
4. 保存后即可直接在页面上测试所有接口

## Postman导入

可以将以上API保存为Postman Collection,便于批量测试。

### 环境变量设置
- `base_url`: http://localhost:8080/api
- `token`: 登录后获取的Token
