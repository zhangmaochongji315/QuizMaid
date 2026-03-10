<template>
  <a-layout-header class="global-header">
    <div class="header-left">
      <div class="logo">
        <img src="../assets/logo.svg" alt="logo" />
        <span class="title">AI 智能题库管理系统</span>
      </div>
    </div>
    <div class="header-right">
      <a-dropdown>
        <div class="user-info">
          <a-avatar :size="32">{{ loginUser.loginUser?.username?.charAt(0) || 'U' }}</a-avatar>
          <span class="nickname">{{ loginUser.loginUser?.username || '用户' }}</span>
        </div>
        <template #overlay>
          <a-menu>
            <a-menu-item key="logout">
              <a-button type="link" @click="handleLogout">退出登录</a-button>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { logout } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const loginUser = useLoginUserStore()

onMounted(() => {
  loginUser.fetchLoginUser()
})

const handleLogout = async () => {
  try {
    const res = await logout()
    if (res.data.code === 0) {
      loginUser.setLoginUser({ username: '未登录' })
      message.success('已退出登录')
      router.push('/user/login')
    } else {
      message.error('退出失败：' + res.data.message)
    }
  } catch (error) {
    message.error('退出请求失败')
  }
}
</script>

<style scoped>
.global-header {
  background: #ffffff;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo img {
  width: 36px;
  height: 36px;
}

.logo .title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.nickname {
  color: #666;
  font-size: 14px;
}
</style>
