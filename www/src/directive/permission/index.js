import permission from './permission'

const install = function(Vue) {
  Vue.directive('permission', permission)
}

if (window && window.Vue) {
  window.permission = permission
  Vue.use(install) // eslint-disable-line
}

permission.install = install
export default permission
