import { createRouter, createWebHistory } from "vue-router";
import BasicLayout from "@/layouts/BasicLayout.vue";

const routes = [
  {
    path: "/user/login",
    component: () => import("@/views/UserLogin.vue")
  },
  {
    path: "/user/register",
    component: () => import("@/views/UserRegister.vue")
  },
  {
    path: "/",
    component: BasicLayout,
    children: [
      { path: "", redirect: "/home" },
      { path: "home", component: () => import("@/views/HomeView.vue") },
      { path: "question", component: () => import("@/views/QuestionView.vue") },
      { path: "paper", component: () => import("@/views/PaperView.vue") },
      { path: "exam", component: () => import("@/views/ExamView.vue") },
      { path: "error-book", component: () => import("@/views/ErrorBookView.vue") },
      { path: "profile", component: () => import("@/views/ProfileView.vue") },
      { path: "system", component: () => import("@/views/SystemView.vue") }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router
