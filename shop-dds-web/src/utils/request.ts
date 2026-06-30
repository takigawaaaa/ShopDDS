import axios, { type AxiosInstance } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截：附带 token
request.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})

// 响应拦截：统一处理 Result 结构
request.interceptors.response.use(
  (resp) => {
    const result = resp.data
    if (result && typeof result === 'object' && 'code' in result) {
      if (result.code === 200) {
        return result.data === undefined ? result : result
      }
      // 业务错误
      ElMessage.error(result.msg || '操作失败')
      return Promise.reject(new Error(result.msg || '操作失败'))
    }
    return result
  },
  (err) => {
    if (err.response) {
      const status = err.response.status
      if (status === 401) {
        const auth = useAuthStore()
        auth.logout()
        router.push('/login')
        ElMessage.error('未登录或登录已过期')
      } else if (status === 403) {
        ElMessage.error('权限不足')
      } else {
        const msg = err.response.data?.msg || err.message
        ElMessage.error(msg)
      }
    } else {
      ElMessage.error('网络异常：' + err.message)
    }
    return Promise.reject(err)
  }
)

export default request