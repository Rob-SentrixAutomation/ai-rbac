<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="请输入角色名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="角色状态" clearable size="small">
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="roleList">
      <el-table-column label="角色ID" align="center" prop="roleId" />
      <el-table-column label="角色名称" align="center" prop="roleName" />
      <el-table-column label="角色标识" align="center" prop="roleKey" />
      <el-table-column label="显示顺序" align="center" prop="roleSort" />
      <el-table-column label="数据权限" align="center" prop="dataScope">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.dataScope == 1">全部数据权限</el-tag>
          <el-tag v-else-if="scope.row.dataScope == 2" type="success">自定义数据权限</el-tag>
          <el-tag v-else-if="scope.row.dataScope == 3" type="warning">本部门数据权限</el-tag>
          <el-tag v-else-if="scope.row.dataScope == 4" type="info">本部门及以下数据权限</el-tag>
          <el-tag v-else type="danger">仅本人数据权限</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == '0' ? 'success' : 'danger'">
            {{ scope.row.status == '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleDataScope(scope.row)"
          >数据权限</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色标识" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入角色标识" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox>
          <el-tree
            :data="menuOptions"
            show-checkbox
            ref="menu"
            node-key="menuId"
            :default-expand-all="false"
            :props="{ children: 'children', label: 'menuName' }"
          ></el-tree>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配数据权限对话框 -->
    <el-dialog title="分配数据权限" :visible.sync="openDataScope" width="500px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select v-model="form.dataScope">
            <el-option :value="1" label="全部数据权限"></el-option>
            <el-option :value="2" label="自定义数据权限"></el-option>
            <el-option :value="3" label="本部门数据权限"></el-option>
            <el-option :value="4" label="本部门及以下数据权限"></el-option>
            <el-option :value="5" label="仅本人数据权限"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope == 2">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选</el-checkbox>
          <el-tree
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="dept"
            node-key="deptId"
            :props="{ children: 'children', label: 'deptName' }"
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRoleList, getRole, addRole, updateRole, deleteRole, dataScope } from '@/api/role'
import { getMenuTree } from '@/api/menu'
import { getDeptTree } from '@/api/dept'
import { parseTime } from '@/filters'

export default {
  name: 'Role',
  data() {
    return {
      loading: true,
      roleList: [],
      total: 0,
      title: '',
      open: false,
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      menuOptions: [],
      deptOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        roleKey: [
          { required: true, message: '角色标识不能为空', trigger: 'blur' }
        ],
        roleSort: [
          { required: true, message: '显示顺序不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getMenuTreeselect()
    this.getDeptTreeselect()
  },
  methods: {
    parseTime,
    getList() {
      this.loading = true
      getRoleList(this.queryParams).then(response => {
        this.roleList = response.data.records
        this.total = response.data.total
        this.loading = false
      })
    },
    getMenuTreeselect() {
      getMenuTree().then(response => {
        this.menuOptions = response.data
      })
    },
    getDeptTreeselect() {
      getDeptTree().then(response => {
        this.deptOptions = response.data
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    cancelDataScope() {
      this.openDataScope = false
      this.reset()
    },
    reset() {
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: '0',
        menuIds: [],
        deptIds: [],
        dataScope: 1,
        remark: undefined
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加角色'
    },
    handleUpdate(row) {
      this.reset()
      const roleId = row.roleId
      getRole(roleId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改角色'
        this.$nextTick(() => {
          this.$refs.menu.setCheckedKeys(this.form.menuIds)
        })
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 获取完全勾选的节点ID和半选的父节点ID
          const checkedKeys = this.$refs.menu.getCheckedKeys()
          const halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys()
          this.form.menuIds = checkedKeys.concat(halfCheckedKeys)
          
          if (this.form.roleId != undefined) {
            updateRole(this.form).then(response => {
              this.$message.success('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addRole(this.form).then(response => {
              this.$message.success('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('是否确认删除角色名称为"' + row.roleName + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteRole(row.roleId)
      }).then(() => {
        this.getList()
        this.$message.success('删除成功')
      })
    },
    handleDataScope(row) {
      this.reset()
      const roleId = row.roleId
      getRole(roleId).then(response => {
        this.form = response.data
        this.openDataScope = true
        this.$nextTick(() => {
          this.$refs.dept.setCheckedKeys(this.form.deptIds)
        })
      })
    },
    submitDataScope() {
      if (this.form.roleId != undefined) {
        // 获取完全勾选的节点ID和半选的父节点ID
        const checkedKeys = this.$refs.dept.getCheckedKeys()
        const halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys()
        this.form.deptIds = checkedKeys.concat(halfCheckedKeys)
        
        dataScope(this.form).then(response => {
          this.$message.success('修改成功')
          this.openDataScope = false
          this.getList()
        })
      }
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        this.toggleTreeExpand(this.menuOptions, value, 'menu')
      } else if (type == 'dept') {
        this.toggleTreeExpand(this.deptOptions, value, 'dept')
      }
    },
    // 递归展开/折叠所有节点
    toggleTreeExpand(treeList, expanded, type) {
      const refName = type == 'menu' ? 'menu' : 'dept'
      const idKey = type == 'menu' ? 'menuId' : 'deptId'
      
      treeList.forEach(item => {
        this.$refs[refName].store.nodesMap[item[idKey]].expanded = expanded
        if (item.children && item.children.length > 0) {
          this.toggleTreeExpand(item.children, expanded, type)
        }
      })
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions : [])
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions : [])
      }
    }
  }
}
</script>
