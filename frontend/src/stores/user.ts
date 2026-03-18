import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export interface UserInfo {
  id: string
  nickname: string
  avatar?: string
  role: 'admin' | 'user'
  username: string
}

const getStoredUserInfo = (): UserInfo | null => {
  const stored = localStorage.getItem('userInfo')
  return stored ? JSON.parse(stored) : null
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(getStoredUserInfo())

  const isLoggedIn = computed(() => !!token.value)

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const login = (userToken: string, info: UserInfo) => {
    setToken(userToken)
    setUserInfo(info)
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    setToken,
    setUserInfo,
    logout,
    login
  }
})
