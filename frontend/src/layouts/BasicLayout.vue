<template>
  <a-layout class="basic-layout">
    <GlobalHeader />
    <a-layout class="main-layout">
      <GlobalSider />
      <a-layout-content class="main-content">
        <router-view />
      </a-layout-content>
    </a-layout>
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from '@/components/GlobalSider.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-layout {
  margin-top: 64px;
  margin-bottom: 52px;
  min-height: calc(100vh - 64px - 52px);
  display: flex;
}

.main-content {
  margin-left: 220px;
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  min-height: calc(100vh - 64px - 52px);
}
</style>
