<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()

const route = useRoute()
const showLayout = computed(() => {
  return !route.path.startsWith('/user/') && !route.path.startsWith('/oauth/')
})
</script>

<template>
  <BasicLayout v-if="showLayout" />
  <router-view v-else />
</template>

<style>
</style>
