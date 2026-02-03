import request from '@/utils/request'

export function getProfile() {
  return request({
    url: '/auth/profile',
    method: 'get'
  })
}

export function updateProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data
  })
}

export function updatePassword(oldPwd, newPwd) {
  return request({
    url: '/system/user/profile/password',
    method: 'put',
    params: { oldPwd, newPwd }
  })
}
