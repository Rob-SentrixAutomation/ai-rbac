package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rbac.domain.SysMenu;
import com.example.rbac.mapper.SysMenuMapper;
import com.example.rbac.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Long, SysMenu> menuMap = menus.stream()
                .peek(menu -> {
                    if (menu.getChildren() == null) {
                        menu.setChildren(new ArrayList<>());
                    } else {
                        menu.getChildren().clear();
                    }
                })
                .collect(Collectors.toMap(SysMenu::getMenuId, m -> m));

        List<SysMenu> roots = new ArrayList<>();
        for (SysMenu menu : menus) {
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0) {
                roots.add(menu);
                continue;
            }
            SysMenu parent = menuMap.get(parentId);
            if (parent != null) {
                parent.getChildren().add(menu);
            } else {
                roots.add(menu);
            }
        }

        Comparator<SysMenu> comparator = Comparator.comparing(SysMenu::getOrderNum, Comparator.nullsLast(Integer::compareTo));
        sortTree(roots, comparator);
        return roots;
    }

    private void sortTree(List<SysMenu> nodes, Comparator<SysMenu> comparator) {
        nodes.sort(comparator);
        for (SysMenu node : nodes) {
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                sortTree(node.getChildren(), comparator);
            }
        }
    }
}

