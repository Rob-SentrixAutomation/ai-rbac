import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'normalize.css/normalize.css'
import '@/styles/index.scss'

import '@/permission' // 权限控制
import permissionDirective from '@/directive/permission' // 自定义按钮权限指令
import * as filters from './filters' // 全局过滤器

Vue.use(ElementUI)
Vue.use(permissionDirective)


// 注册全局过滤器
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
