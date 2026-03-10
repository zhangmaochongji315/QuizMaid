<template>
   <div id="userLoginPage">
     <h2 class="title">AI 智能题库管理系统 - 用户登录</h2>
     <div class="desc">高效管理题库，轻松考试</div>
     <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
       <a-form-item name="username" :rules="[{ required: true, message: '请输入账号' }]">
         <a-input v-model:value="formState.username" placeholder="请输入账号" />
       </a-form-item>
       <a-form-item
         name="userPassword"
         :rules="[
           { required: true, message: '请输入密码' },
           { min: 6, message: '密码不能小于 6 位' },
         ]"
       >
         <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
       </a-form-item>
       <div class="tips">
         没有账号？
         <a href="/user/register">去注册</a>
       </div>
       <a-form-item>
         <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
       </a-form-item>
     </a-form>
   </div>
 </template>

 <script setup lang="ts">
 import { reactive } from 'vue'
 import { useRouter } from 'vue-router'
 import { message } from 'ant-design-vue'
 import { login } from '@/api/userController'
 import { useLoginUserStore } from '@/stores/loginUser'

 const router = useRouter()
 const loginUserStore = useLoginUserStore()

 const formState = reactive({
   username: '',
   userPassword: '',
 })

 const handleSubmit = async (values: any) => {
   try {
     const res = await login(values)
     if (res.data.code === 0 && res.data.data) {
       await loginUserStore.fetchLoginUser()
       message.success('登录成功')
       router.push('/')
     } else {
       message.error('登录失败：' + res.data.message)
     }
   } catch (error) {
     message.error('登录请求失败')
   }
 }
 </script>

 <style scoped>
 #userLoginPage {
   max-width: 360px;
   margin: 100px auto;
   padding: 40px;
   background: #fff;
   border-radius: 8px;
   box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
 }

 .title {
   text-align: center;
   font-size: 24px;
   color: #1890ff;
   margin-bottom: 8px;
 }

 .desc {
   text-align: center;
   color: #999;
   margin-bottom: 24px;
 }

 .tips {
   text-align: right;
   margin-bottom: 24px;
   font-size: 14px;
   color: #666;
 }

 .tips a {
   color: #1890ff;
 }
 </style>
