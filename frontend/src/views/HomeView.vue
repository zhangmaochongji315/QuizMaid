<script setup lang="ts">
import { healthCheck } from '@/api/userController.ts'

healthCheck().then((res) => {
  console.log(res)
})
</script>

<template>
  <div class="home-view">
    <h1>欢迎使用 AI 题库管理系统</h1>
  </div>
</template>

<style scoped>
.home-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.home-view h1 {
  font-size: 28px;
  color: #333;
  font-weight: 500;
}
</style>
