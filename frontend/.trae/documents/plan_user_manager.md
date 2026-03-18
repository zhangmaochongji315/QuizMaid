# 计划：将系统设置改为只有admin能看到，并添加用户管理功能

## 目标
1. 将"系统管理"菜单项改为只有admin角色可见
2. 在系统管理页面中添加用户管理组件 (UserManager)

## 实现步骤

### 步骤1: 修改 GlobalHeader.vue，限制系统管理菜单项显示
- 在 GlobalHeader.vue 中获取当前登录用户角色
- 根据角色判断是否显示"系统管理"菜单项
- 仅当用户角色为 'admin' 时显示

### 步骤2: 修改路由权限 (可选)
- 在路由配置中添加权限标识，或在组件中检查权限

### 步骤3: 创建 UserManager 组件
- 创建文件: `src/components/UserManager.vue`
- 功能包括:
  - 用户列表展示 (使用 listUserByPage API)
  - 添加用户 (使用 addUser API)
  - 删除用户 (使用 deleteUser API)
  - 编辑用户 (使用 updateUserByAdmin API)
- 使用 Ant Design Vue 表格组件展示用户数据
- 添加分页功能

### 步骤4: 修改 SystemView.vue
- 引入并使用 UserManager 组件
- 保留原有的系统管理内容结构

## 使用的 API (来自 @/api/userController.ts)
- `listUserByPage` - 分页获取用户列表
- `addUser` - 添加用户
- `deleteUser` - 删除用户
- `updateUserByAdmin` - 管理员更新用户

## 注意事项
- 需要从 loginUserStore 获取当前用户角色信息
- 用户角色存储在 loginUser.loginUser.role 中
- 角色类型: 'admin' | 'teacher' | 'student'
