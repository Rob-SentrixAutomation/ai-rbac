# RBAC权限管理系统

基于Spring Boot + MyBatis Plus + Spring Security + JWT + Redis的RBAC权限管理系统

## 技术栈

- **Java**: 17
- **Spring Boot**: 2.5.15
- **Spring Security**: 5.7.14
- **MyBatis Plus**: 3.5.4
- **MySQL**: 8.0+
- **Redis**: 5.0+
- **JWT**: 0.11.5
- **Knife4j**: 4.4.0 (API文档)
- **Hutool**: 5.8.23
- **Lombok**: 1.18.30
- **Fastjson**: 2.0.58

## 功能特性

### 1. 用户认证
- JWT Token认证
- Redis存储用户会话
- 密码BCrypt加密
- 登录/登出功能

### 2. RBAC权限模型
- **用户管理**: 用户增删改查、密码管理
- **角色管理**: 角色增删改查、权限分配
- **菜单管理**: 菜单增删改查、权限标识
- **部门管理**: 部门增删改查、树形结构

### 3. 权限控制
- **接口权限**: 基于`@PreAuthorize`注解的方法级权限控制
- **数据权限**: 基于`@DataScope`注解的数据范围过滤
  - 全部数据权限
  - 自定数据权限
  - 本部门数据权限
  - 本部门及以下数据权限
  - 仅本人数据权限

### 4. 通用功能
- **统一返回**: Result封装统一返回格式
- **统一异常处理**: 全局异常处理器
- **国际化支持**: 中英文切换
- **逻辑删除**: 软删除支持
- **自动填充**: 创建时间、更新时间、创建者、更新者自动填充
- **分页查询**: 集成PageHelper
- **接口文档**: Knife4j在线API文档

## 数据库设计

### 核心表
- `sys_user`: 用户表
- `sys_role`: 角色表
- `sys_menu`: 菜单权限表
- `sys_dept`: 部门表
- `sys_user_role`: 用户角色关联表
- `sys_role_menu`: 角色菜单关联表
- `sys_role_dept`: 角色部门关联表(数据权限)

### 通用字段
所有业务表都包含以下通用字段:
```sql
remark VARCHAR(500) COMMENT '备注'
create_by VARCHAR(64) COMMENT '创建者'
create_time DATETIME COMMENT '创建时间'
update_by VARCHAR(64) COMMENT '更新者'
update_time DATETIME COMMENT '更新时间'
status INT COMMENT '状态(0正常 1停用)'
del_flag INT COMMENT '删除标志(0存在 1删除)'
```

## 快速开始

### 1. 环境准备
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+

### 2. 数据库初始化
执行 `sql/schema.sql` 创建数据库和表

### 3. 修改配置
修改 `src/main/resources/application.yml` 中的数据库和Redis配置:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac_system
    username: root
    password: your_password
  
  redis:
    host: localhost
    port: 6379
    password: your_password
```

### 4. 启动项目
```bash
mvn clean package
java -jar target/rbac-system-1.0.0.jar
```

### 5. 访问系统
- **接口文档**: http://localhost:8080/api/doc.html
- **默认账号**: admin / admin123

## 项目结构

```
src/main/java/com/example/rbac/
├── annotation/          # 自定义注解
│   └── DataScope.java  # 数据权限注解
├── aspect/             # 切面
│   └── DataScopeInterceptor.java  # 数据权限拦截器
├── common/             # 公共类
│   └── Result.java     # 统一返回结果
├── config/             # 配置类
│   ├── I18nConfig.java
│   ├── JwtProperties.java
│   ├── Knife4jConfig.java
│   ├── MybatisPlusConfig.java
│   ├── RedisConfig.java
│   └── SecurityConfig.java
├── controller/         # 控制器
├── domain/            # 实体类
├── dto/               # 数据传输对象
├── exception/         # 异常处理
├── mapper/            # Mapper接口
├── security/          # 安全配置
├── service/           # 服务层
├── utils/             # 工具类
└── RbacApplication.java  # 启动类
```

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出

### 用户管理
- `GET /api/system/user/list` - 查询用户列表
- `GET /api/system/user/{userId}` - 查询用户详情
- `POST /api/system/user` - 新增用户
- `PUT /api/system/user` - 修改用户
- `DELETE /api/system/user/{userId}` - 删除用户

### 角色管理
- `GET /api/system/role/list` - 查询角色列表
- `GET /api/system/role/{roleId}` - 查询角色详情
- `POST /api/system/role` - 新增角色
- `PUT /api/system/role` - 修改角色
- `DELETE /api/system/role/{roleId}` - 删除角色

### 菜单管理
- `GET /api/system/menu/list` - 查询菜单列表
- `GET /api/system/menu/{menuId}` - 查询菜单详情
- `POST /api/system/menu` - 新增菜单
- `PUT /api/system/menu` - 修改菜单
- `DELETE /api/system/menu/{menuId}` - 删除菜单

### 部门管理
- `GET /api/system/dept/list` - 查询部门列表
- `GET /api/system/dept/{deptId}` - 查询部门详情
- `POST /api/system/dept` - 新增部门
- `PUT /api/system/dept` - 修改部门
- `DELETE /api/system/dept/{deptId}` - 删除部门

## 权限使用示例

### 1. 接口权限控制
```java
@PreAuthorize("hasAuthority('system:user:list')")
@GetMapping("/list")
public Result<Page<SysUser>> list() {
    // 业务逻辑
}
```

### 2. 数据权限控制
```java
@DataScope(deptAlias = "d", userAlias = "u")
@GetMapping("/list")
public Result<Page<SysUser>> list() {
    // 自动根据用户的数据权限范围过滤数据
}
```

## 国际化

通过请求参数 `lang` 切换语言:
- 中文: `?lang=zh_CN`
- 英文: `?lang=en_US`

## 注意事项

1. 默认管理员账号密码: admin / admin123
2. JWT密钥请在生产环境修改
3. Redis用于存储用户会话,过期时间30分钟
4. 所有接口都有权限控制,登录后需携带Token访问
5. Token格式: `Authorization: Bearer {token}`

## 开发建议

1. 新增实体类继承`BaseEntity`获得通用字段
2. 使用MyBatis Plus简化CRUD操作
3. 使用`@DataScope`注解实现数据权限控制
4. 使用`@PreAuthorize`注解实现接口权限控制
5. 使用Knife4j接口文档注解完善API文档

## License

MIT License
