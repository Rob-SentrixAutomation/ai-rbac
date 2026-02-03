# 依赖修复说明

## 问题描述
`@EnableSwagger2`注解无法使用,原因是误以为需要手动添加该注解。

## 解决方案

Knife4j 4.4.0的`knife4j-openapi2-spring-boot-starter`已经支持**自动配置**,不需要手动添加任何Swagger相关注解!

### 正确配置方式

#### 1. pom.xml依赖
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi2-spring-boot-starter</artifactId>
    <version>4.4.0</version>
</dependency>
```

#### 2. application.yml配置
```yaml
knife4j:
  enable: true  # 只需要这一行配置即可!
  setting:
    language: zh-CN
```

#### 3. Knife4jConfig.java
```java
@Configuration  // 只需要@Configuration注解!
public class Knife4jConfig {
    
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.rbac.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }
    
    // 其他配置方法...
}
```

**重点**: 
- ❌ 不需要 `@EnableSwagger2`
- ❌ 不需要 `@EnableSwagger2WebMvc`
- ❌ 不需要 `@EnableKnife4j`
- ✅ 只需要 `@Configuration` 和 `yml中的knife4j.enable=true`

## 为什么不需要@EnableSwagger2?

Knife4j 4.4.0的`knife4j-openapi2-spring-boot-starter`是一个**Spring Boot Starter**,它包含了:

1. **自动配置类**: 自动配置Swagger和Knife4j
2. **条件装配**: 当检测到`knife4j.enable=true`时自动启用
3. **内置所有依赖**: 包含springfox-boot-starter等

因此,Spring Boot会通过**自动配置机制**完成所有Swagger的初始化工作,无需手动添加`@EnableSwagger2`等注解。

## 自动配置原理

```
1. 项目启动
   ↓
2. Spring Boot扫描classpath下的META-INF/spring.factories
   ↓
3. 发现knife4j-openapi2-spring-boot-starter的自动配置类
   ↓
4. 检查knife4j.enable=true
   ↓
5. 自动创建Swagger配置(相当于@EnableSwagger2的效果)
   ↓
6. 扫描并加载用户自定义的Docket Bean
   ↓
7. Swagger和Knife4j启动完成
```

## 配置变更

### application.yml
```yaml
knife4j:
  enable: true  # 启用Knife4j自动配置
  setting:
    language: zh-CN
```

### Knife4jConfig.java
```java
@Configuration  // 只需要@Configuration
public class Knife4jConfig {
    
    @Bean
    public Docket createRestApi() {
        // Docket配置
    }
}
```

## 验证方法

### 1. 清理并重新编译
```bash
mvn clean compile
```

### 2. 启动项目
```bash
mvn spring-boot:run
```

### 3. 访问文档
浏览器访问: http://localhost:8080/api/doc.html

应该能正常打开Knife4j文档界面。

## 常见问题

### Q1: 启动时找不到Swagger相关类
**原因**: 依赖冲突或未正确引入
**解决**: 删除`.m2`仓库中的knife4j目录,重新下载依赖

### Q2: 访问doc.html返回404
**原因**: 
1. 路径配置错误
2. Security拦截了静态资源

**解决**: 检查SecurityConfig是否放行了Swagger路径:
```java
.antMatchers("/doc.html", "/swagger-resources/**", "/webjars/**", "/v2/api-docs").permitAll()
```

### Q3: 接口文档显示但无法调试
**原因**: 跨域或安全配置问题
**解决**: 
1. 检查CORS配置
2. 检查全局参数设置中的Token配置

## 推荐的依赖版本组合

适用于Spring Boot 2.x的完整组合:

```xml
<properties>
    <spring.boot.version>2.5.15</spring.boot.version>
    <knife4j.version>4.4.0</knife4j.version>
    <mybatis-plus.version>3.5.4</mybatis-plus.version>
</properties>

<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Knife4j (自动配置) -->
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-openapi2-spring-boot-starter</artifactId>
        <version>4.4.0</version>
    </dependency>
    
    <!-- MyBatis Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.4</version>
    </dependency>
</dependencies>
```

## 总结

✅ **正确做法**: 
- 使用`knife4j-openapi2-spring-boot-starter:4.4.0`
- 配置文件添加`knife4j.enable=true`
- 配置类只需`@Configuration`注解
- 无需`@EnableSwagger2`等注解

✅ **优势**:
- Spring Boot自动配置,开箱即用
- 配置简单,无需手动启用Swagger
- 完全兼容Spring Boot 2.x

**现在重新执行 `mvn clean compile` 应该可以正常编译了!** 🎉
