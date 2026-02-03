<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      <h1>欢迎使用 RBAC权限管理系统</h1>
      <p>当前用户：{{ name }}</p>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
