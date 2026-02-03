package com.example.rbac.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 权限校验服务，支持超级权限 *:*
 */
@Component("pms")
public class PermissionService {

    public boolean hasPerm(String... perms) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }
        Set<String> authSet = AuthorityUtils.authorityListToSet(auth.getAuthorities());
        if (authSet.contains("*:*")) {
            return true;
        }
        if (perms == null || perms.length == 0) {
            return false;
        }
        for (String p : perms) {
            if (authSet.contains(p)) {
                return true;
            }
        }
        return false;
    }
}
