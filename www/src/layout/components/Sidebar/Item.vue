<template>
  <div v-if="icon || title" class="menu-wrapper">
    <i v-if="icon" :class="icon" class="sub-el-icon" />
    <span v-if="title" slot="title">{{ title }}</span>
  </div>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
.menu-wrapper {
  display: inline-flex;
  align-items: center;
}
.sub-el-icon {
  margin-right: 8px;
}
</style>
