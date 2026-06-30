import { defineStore } from 'pinia'
import type { LoginResponse, Role } from '@/types'

const TOKEN_KEY = 'shopdds_token'
const USER_KEY = 'shopdds_user'

interface StoredUser {
  userId: string
  name: string
  role: Role
  facilityId: string | null
  facilityName: string | null
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) || '',
    user: JSON.parse(localStorage.getItem(USER_KEY) || 'null') as StoredUser | null
  }),
  getters: {
    isLoggedIn: (s) => !!s.token,
    role: (s) => s.user?.role || null,
    isHeadAdmin: (s) => s.user?.role === 'HEAD_ADMIN',
    isWarehouseAdmin: (s) => s.user?.role === 'WAREHOUSE_ADMIN',
    isSupermarketAdmin: (s) => s.user?.role === 'SUPERMARKET_ADMIN'
  },
  actions: {
    setLogin(resp: LoginResponse) {
      this.token = resp.token
      this.user = {
        userId: resp.userId,
        name: resp.name,
        role: resp.role,
        facilityId: resp.facilityId,
        facilityName: resp.facilityName
      }
      localStorage.setItem(TOKEN_KEY, this.token)
      localStorage.setItem(USER_KEY, JSON.stringify(this.user))
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
    }
  }
})