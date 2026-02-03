import store from '@/store'

function checkPermission(el, binding) {
  const { value } = binding
  const permissions = store.getters && store.getters.permissions

  if (value && value instanceof Array && value.length > 0) {
    const needPerms = value
    const hasSuper = permissions && permissions.includes('*:*')
    const hasPerm = permissions && needPerms.some(p => permissions.includes(p))
    if (!hasSuper && !hasPerm) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error("need perms! Like v-permission=\"['system:user:add']\"")
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
