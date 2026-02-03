package com.example.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rbac.domain.SysDept;
import com.example.rbac.mapper.SysDeptMapper;
import com.example.rbac.service.ISysDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        if (depts == null || depts.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Long, SysDept> deptMap = depts.stream()
                .peek(dept -> {
                    if (dept.getChildren() == null) {
                        dept.setChildren(new ArrayList<>());
                    } else {
                        dept.getChildren().clear();
                    }
                })
                .collect(Collectors.toMap(SysDept::getDeptId, d -> d));

        List<SysDept> roots = new ArrayList<>();
        for (SysDept dept : depts) {
            Long parentId = dept.getParentId();
            if (parentId == null || parentId == 0) {
                roots.add(dept);
                continue;
            }
            SysDept parent = deptMap.get(parentId);
            if (parent != null) {
                parent.getChildren().add(dept);
            } else {
                roots.add(dept);
            }
        }

        Comparator<SysDept> comparator = Comparator.comparing(SysDept::getOrderNum, Comparator.nullsLast(Integer::compareTo));
        sortTree(roots, comparator);
        return roots;
    }

    private void sortTree(List<SysDept> nodes, Comparator<SysDept> comparator) {
        nodes.sort(comparator);
        for (SysDept node : nodes) {
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                sortTree(node.getChildren(), comparator);
            }
        }
    }
}

