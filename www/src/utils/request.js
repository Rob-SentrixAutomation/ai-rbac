import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      // 添加Bearer前缀
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    // 如果返回的状态码不是200，则判断为错误
    if (res.code !== 200) {
      // 401: Token过期，直接回到登录页
      if (res.code === 401) {
        store.dispatch('user/resetToken').then(() => {
          window.location.href = `/login?redirect=${window.location.pathname}`
        })
        return Promise.reject(new Error(res.message || res.msg || 'Unauthorized'))
      }

      Message({
        message: res.message || res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    // 捕获非200响应（如401/403）
    if (error.response && error.response.status === 401) {
      store.dispatch('user/resetToken').then(() => {
        window.location.href = `/login?redirect=${window.location.pathname}`
      })
      return Promise.reject(error)
    }
    console.log('err' + error)
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
