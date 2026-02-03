



# 使用ai 协助编写，99.9%代码由ai生成，使用code buddy完成

> 前后端分离，前端vue2 +Element ui +vue-element-admin
> 后端 mybatis plus ， jdk17,springboot 



# RBAC权限管理系统 - 前端

基于 Vue 2 + Element UI 的权限管理系统前端。

## 技术栈

- Vue 2.6.14
- Vue Router 3.5.3
- Vuex 3.6.2
- Element UI 2.15.13
- Axios 0.27.2

## 项目结构

```
www/
├── public/                # 静态资源
├── src/
│   ├── api/              # API接口
│   ├── components/       # 公共组件
│   ├── filters/          # 全局过滤器
│   ├── layout/           # 布局组件
│   ├── router/           # 路由配置
│   ├── store/            # Vuex状态管理
│   ├── styles/           # 全局样式
│   ├── utils/            # 工具函数
│   ├── views/            # 页面组件
│   ├── App.vue           # 根组件
│   ├── main.js           # 入口文件
│   └── permission.js     # 权限控制
├── .env.development      # 开发环境配置
├── .env.production       # 生产环境配置
├── vue.config.js         # Vue CLI配置
├── babel.config.js       # Babel配置
└── package.json          # 项目依赖

## 功能模块

- 用户管理：用户的增删改查、重置密码
- 角色管理：角色的增删改查、分配菜单权限、数据权限
- 菜单管理：菜单的增删改查、树形展示
- 部门管理：部门的增删改查、树形展示
- 登录认证：JWT token认证、权限控制

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器 (http://localhost:9527)
npm run dev
```

## 构建

```bash
# 生产环境构建
npm run build:prod

# 预发布环境构建
npm run build:stage
```

## 注意事项

1. 默认后端接口地址：`http://localhost:8080/api`
2. 可在 `.env.development` 中修改开发环境配置
3. 可在 `.env.production` 中修改生产环境配置
4. 默认登录账号：admin / admin123

## 接口说明

所有接口统一返回格式：
```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

Token通过请求头 `Authorization` 传递。
