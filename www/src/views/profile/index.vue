<template>
  <div class="app-container profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="profile-card">
          <div class="avatar-wrapper">
            <el-avatar :size="96" :src="user.avatar || defaultAvatar">
              {{ user.nickname || user.username || '用户' }}
            </el-avatar>
            <h3 class="username">{{ user.nickname || user.username }}</h3>
          </div>
          <el-descriptions :column="1" border label-class-name="desc-label">
            <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ user.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属角色">{{ (user.roles || []).join(', ') || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建日期">{{ user.createTime || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="base">
              <el-form :model="profileForm" label-width="80px" :rules="rules" ref="profileForm">
                <el-form-item label="姓名" prop="nickname">
                  <el-input v-model="profileForm.nickname" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="性别">
                  <el-select v-model="profileForm.sex" placeholder="请选择性别" style="width: 100%">
                    <el-option label="男" value="0" />
                    <el-option label="女" value="1" />
                    <el-option label="未知" value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSaveProfile">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="password">
              <el-form :model="pwdForm" label-width="100px" :rules="pwdRules" ref="pwdForm">
                <el-form-item label="旧密码" prop="oldPwd">
                  <el-input v-model="pwdForm.oldPwd" type="password" autocomplete="off" />
                </el-form-item>
                <el-form-item label="新密码" prop="newPwd">
                  <el-input v-model="pwdForm.newPwd" type="password" autocomplete="off" />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPwd">
                  <el-input v-model="pwdForm.confirmPwd" type="password" autocomplete="off" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onChangePwd">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getProfile, updateProfile, updatePassword } from '@/api/profile'

export default {
  name: 'Profile',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.pwdForm.newPwd) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      activeTab: 'base',
      profileForm: {
        nickname: '',
        phone: '',
        email: '',
        sex: '2'
      },
      pwdForm: {
        oldPwd: '',
        newPwd: '',
        confirmPwd: ''
      },

      rules: {
        nickname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
        email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }]
      },
      pwdRules: {
        oldPwd: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
        newPwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur' }
        ],
        confirmPwd: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      },
      defaultAvatar: 'https://via.placeholder.com/96'
    }
  },
  computed: {
    ...mapGetters(['name', 'avatar', 'roles', 'permissions', 'token']),
    user() {
      return {
        username: this.name,
        avatar: this.avatar,
        roles: this.roles,
        permissions: this.permissions,
        nickname: this.profileForm.nickname,
        phone: this.profileForm.phone,
        email: this.profileForm.email,
        sex: this.profileForm.sex,
        createTime: this.profileForm.createTime
      }
    }
  },
  created() {
    this.initProfile()
  },
  methods: {
    initProfile() {
      getProfile().then(res => {
        const data = res.data || {}
        this.profileForm.nickname = data.nickname || this.name
        this.profileForm.phone = data.phone
        this.profileForm.email = data.email
        this.profileForm.sex = data.sex || '2'
        this.profileForm.createTime = data.createTime
      })
    },
    onSaveProfile() {
      this.$refs.profileForm.validate(valid => {
        if (!valid) return
        updateProfile(this.profileForm).then(() => {
          this.$message.success('已保存')
          this.initProfile()
        })
      })
    },
    onChangePwd() {
      this.$refs.pwdForm.validate(valid => {
        if (!valid) return
        if (this.pwdForm.newPwd !== this.pwdForm.confirmPwd) {
          this.$message.error('两次输入的密码不一致')
          return
        }
        updatePassword(this.pwdForm.oldPwd, this.pwdForm.newPwd).then(() => {
          this.$message.success('密码已修改，请重新登录')
          this.$store.dispatch('user/resetToken').then(() => {
            window.location.href = '/login'
          })
        })
      })
    }
  }
}

</script>

<style lang="scss" scoped>
.profile-container {
  .profile-card {
    min-height: 360px;
  }
  .avatar-wrapper {
    text-align: center;
    margin-bottom: 20px;
    .username {
      margin-top: 12px;
    }
  }
  .desc-label {
    width: 90px;
  }
}
</style>
