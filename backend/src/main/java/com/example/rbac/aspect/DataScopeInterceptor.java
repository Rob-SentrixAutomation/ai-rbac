package com.example.rbac.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.example.rbac.annotation.DataScope;
import com.example.rbac.utils.LoginUser;
import com.example.rbac.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 数据权限拦截器
 */
@Slf4j
@Component
public class DataScopeInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        try {
            // 获取当前登录用户
            LoginUser loginUser = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
            if (loginUser == null) {
                return;
            }

            String dataScope = loginUser.getDataScope();
            if (StrUtil.isBlank(dataScope)) {
                return;
            }

            // 检查方法是否有DataScope注解
            DataScope dataScopeAnnotation = getDataScopeAnnotation(ms);
            if (dataScopeAnnotation == null) {
                return;
            }

            // 根据数据权限范围过滤
            String originalSql = boundSql.getSql();
            String newSql = buildDataScopeSql(originalSql, dataScope, loginUser, dataScopeAnnotation);
            
            if (!originalSql.equals(newSql)) {
                PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
                mpBoundSql.sql(newSql);
            }
        } catch (Exception e) {
            log.error("数据权限过滤失败", e);
        }
    }

    /**
     * 获取DataScope注解
     */
    private DataScope getDataScopeAnnotation(MappedStatement ms) {
        try {
            String id = ms.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            
            Class<?> clazz = Class.forName(className);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && method.isAnnotationPresent(DataScope.class)) {
                    return method.getAnnotation(DataScope.class);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 构建数据权限SQL
     */
    private String buildDataScopeSql(String originalSql, String dataScope, LoginUser loginUser, DataScope annotation) {
        try {
            Select select = (Select) CCJSqlParserUtil.parse(originalSql);
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            Expression where = plainSelect.getWhere();
            
            Expression dataScopeExpression = null;
            
            switch (dataScope) {
                case "1": // 全部数据权限
                    return originalSql;
                    
                case "2": // 自定数据权限(需要查询用户关联的部门)
                    // 这里简化处理,实际需要查询sys_role_dept表
                    break;
                    
                case "3": // 本部门数据权限
                    if (loginUser.getDeptId() != null) {
                        EqualsTo deptEquals = new EqualsTo();
                        deptEquals.setLeftExpression(new Column(annotation.deptAlias() + ".dept_id"));
                        deptEquals.setRightExpression(new StringValue(String.valueOf(loginUser.getDeptId())));
                        dataScopeExpression = deptEquals;
                    }
                    break;
                    
                case "4": // 本部门及以下数据权限
                    // 需要查询部门树,这里简化处理
                    if (loginUser.getDeptId() != null) {
                        EqualsTo deptEquals = new EqualsTo();
                        deptEquals.setLeftExpression(new Column(annotation.deptAlias() + ".dept_id"));
                        deptEquals.setRightExpression(new StringValue(String.valueOf(loginUser.getDeptId())));
                        dataScopeExpression = deptEquals;
                    }
                    break;
                    
                case "5": // 仅本人数据权限
                    if (loginUser.getUserId() != null) {
                        EqualsTo userEquals = new EqualsTo();
                        userEquals.setLeftExpression(new Column(annotation.userAlias() + ".user_id"));
                        userEquals.setRightExpression(new StringValue(String.valueOf(loginUser.getUserId())));
                        dataScopeExpression = userEquals;
                    }
                    break;
            }
            
            if (dataScopeExpression != null) {
                if (where != null) {
                    AndExpression andExpression = new AndExpression(where, dataScopeExpression);
                    plainSelect.setWhere(andExpression);
                } else {
                    plainSelect.setWhere(dataScopeExpression);
                }
                return select.toString();
            }
        } catch (Exception e) {
            log.error("构建数据权限SQL失败", e);
        }
        
        return originalSql;
    }
}
