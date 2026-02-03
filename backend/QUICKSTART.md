# RBAC权限管理系统 - 快速启动指南

## 🚀 快速开始(5分钟启动)

### 第一步: 准备环境
确保已安装以下软件:
- ✅ JDK 17
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Redis 5.0+

### 第二步: 创建数据库
1. 登录MySQL
```bash
mysql -u root -p
```

2. 执行SQL脚本
```bash
source sql/schema.sql
```

或者直接在MySQL客户端执行`sql/schema.sql`文件的内容

### 第三步: 配置连接
修改 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rbac_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root          # 改为你的MySQL用户名
    password: root          # 改为你的MySQL密码
  
  redis:
    host: localhost         # 改为你的Redis地址
    port: 6379
    password:               # 如果有密码则填写
```

### 第四步: 启动Redis
```bash
redis-server
```

### 第五步: 运行项目

#### 方式1: Maven命令
```bash
mvn spring-boot:run
```

#### 方式2: IDEA运行
1. 打开项目
2. 找到 `RbacApplication.java`
3. 右键 -> Run 'RbacApplication'

#### 方式3: 打包运行
```bash
mvn clean package -DskipTests
java -jar target/rbac-system-1.0.0.jar
```

### 第六步: 访问系统
启动成功后,控制台会显示:
```
============================
RBAC权限管理系统启动成功!
接口文档地址: http://localhost:8080/api/doc.html
============================
```

访问接口文档: **http://localhost:8080/api/doc.html**

## 🎯 测试登录

### 1. 使用Knife4j测试
1. 打开: http://localhost:8080/api/doc.html
2. 找到"认证管理" -> "用户登录"
3. 点击"调试"
4. 输入:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
5. 点击"发送"
6. 复制返回的token
7. 点击右上角"文档管理" -> "全局参数设置"
8. 添加Authorization: `Bearer {复制的token}`

### 2. 使用curl测试
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 3. 使用Postman测试
参考 `API-TEST.md` 文档

## 📊 系统账号

| 用户名 | 密码 | 角色 | 权限范围 |
|-------|------|------|---------|
| admin | admin123 | 超级管理员 | 全部数据权限 |

## 🔧 常见问题

### 1. 端口被占用
修改 `application.yml` 中的端口:
```yaml
server:
  port: 8081  # 改为其他端口
```

### 2. MySQL连接失败
- 检查MySQL是否启动
- 检查用户名密码是否正确
- 检查数据库`rbac_system`是否创建

### 3. Redis连接失败
- 检查Redis是否启动: `redis-cli ping`
- 检查Redis地址和密码是否正确

### 4. 编译失败
确保使用JDK 17:
```bash
java -version
```

### 5. Token无效
- Token有效期24小时
- 确保请求头格式: `Authorization: Bearer {token}`
- 重新登录获取新Token

## 📝 开发建议

### 1. 开发环境推荐
- **IDE**: IntelliJ IDEA 2023+
- **插件**: Lombok Plugin, MyBatis Plugin

### 2. 代码格式
项目使用标准Java代码规范,建议配置IDEA自动格式化

### 3. 数据库工具
推荐使用:
- Navicat
- DBeaver
- MySQL Workbench

### 4. API测试工具
- **Knife4j**: 内置接口文档(推荐)
- **Postman**: 功能强大
- **Apifox**: 国产工具

## 🎨 功能演示

### 1. 权限控制测试
1. 使用admin登录
2. 创建一个新用户test001
3. 给test001分配"普通角色"
4. 用test001登录
5. 尝试访问用户管理(会因权限不足而失败)

### 2. 数据权限测试
1. 创建部门: 技术部
2. 创建用户A,分配到技术部,角色设置"本部门数据权限"
3. 创建用户B,分配到技术部
4. 创建用户C,分配到市场部
5. 用用户A登录查询用户列表,只能看到技术部的用户

## 📚 下一步

- 阅读 `README.md` 了解完整功能
- 阅读 `API-TEST.md` 学习API测试
- 参考代码注释学习实现细节
- 根据业务需求扩展功能

## 💡 提示

1. 首次启动可能需要下载Maven依赖,请耐心等待
2. 建议使用国内Maven镜像加速下载
3. Redis和MySQL必须先启动
4. 修改配置文件后需要重启项目
5. 生产环境请修改JWT密钥和密码强度

## 🆘 获取帮助

如遇到问题:
1. 查看控制台错误日志
2. 检查MySQL和Redis连接
3. 查看 `README.md` 和 `API-TEST.md`
4. 检查Knife4j文档中的接口说明

---

**祝你使用愉快! 🎉**
