/**
 * 判断是否为外部链接
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 用户名校验
 */
export function validUsername(str) {
  return str.trim().length >= 2
}

/**
 * 密码校验
 */
export function validPassword(str) {
  return str.length >= 6
}

/**
 * 手机号校验
 */
export function validPhone(phone) {
  const reg = /^1[3-9]\d{9}$/
  return reg.test(phone)
}

/**
 * 邮箱校验
 */
export function validEmail(email) {
  const reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
  return reg.test(email)
}
