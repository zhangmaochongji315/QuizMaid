<template>
  <div class="layout-container">
    <header style="background: #fff; padding: 0 24px; display: flex; align-items: center; justify-content: space-between; height: 64px; border-bottom: 1px solid #f0f0f0; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06); position: fixed; top: 0; left: 0; right: 0; z-index: 100;">
      <div class="header-left" style="display: flex; align-items: center; gap: 24px;">
        <div class="logo" style="display: flex; align-items: center; gap: 12px;">
          <img src="../assets/logo.svg" alt="logo" style="width: 36px; height: 36px;" />
          <span class="title" style="font-size: 18px; font-weight: 600; color: #1890ff; letter-spacing: 0.5px;">AI 智能题库管理系统</span>
        </div>
        <a-menu
          mode="horizontal"
          :selectedKeys="[currentRoute]"
          @click="handleMenuClick"
          style="border-bottom: none;"
        >
          <a-menu-item key="home">首页</a-menu-item>
          <a-menu-item key="question">试题管理</a-menu-item>
          <a-menu-item key="paper">试卷管理</a-menu-item>
          <a-menu-item key="exam">考试中心</a-menu-item>
          <a-menu-item key="error-book">错题本</a-menu-item>
          <a-menu-item key="profile">个人中心</a-menu-item>
          <a-menu-item key="system">系统管理</a-menu-item>
        </a-menu>
      </div>
      <div class="header-right" style="display: flex; align-items: center;">
        <a-dropdown>
          <div class="user-info" style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
            <a-avatar :size="32">{{ loginUser.loginUser?.nickname?.charAt(0) || 'U' }}</a-avatar>
            <span class="nickname" style="color: #666; font-size: 14px;">{{ loginUser.loginUser?.nickname || '用户' }}</span>
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
    </header>
    <main style="padding: 0 50px; margin-top: 64px; margin-bottom: 52px; min-height: calc(100vh - 64px - 52px);">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb>
      <div :style="{ background: '#fff', padding: '24px', minHeight: '280px' }">
        <router-view />
      </div>
    </main>
    <footer style="text-align: center; background: #ffffff; padding: 16px 24px; border-top: 1px solid #f0f0f0; color: #666; font-size: 14px; position: fixed; bottom: 0; left: 0; right: 0; z-index: 100;">
      AI 智能题库管理系统 © 2026 公共教育学习平台
    </footer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { logout } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const route = useRoute()
const loginUser = useLoginUserStore()

onMounted(() => {
  loginUser.fetchLoginUser()
})

const currentRoute = computed(() => {
  const path = route.path.replace('/', '')
  return path || 'home'
})

const handleMenuClick = ({ key }: { key: string }) => {
  router.push(`/${key}`)
}

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
.site-layout-content {
  min-height: 280px;
  padding: 24px;
  background: #fff;
}

[data-theme='dark'] .site-layout-content {
  background: #141414;
}
</style>
