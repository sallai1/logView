import request from '@/utils/request'
// const baseUrl = "http://localhost:8080";
export function login(data) {
  return request({
    url: '/logView/user/login',
    method: 'post',
    params:data,
    // baseUrl: baseUrl
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
