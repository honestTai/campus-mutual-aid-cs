import Vue from 'vue';
import Router from 'vue-router';

//路由
Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/home',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/home/Home.vue'),
                    meta: { title: '列表' }
                },
                {
                    path: '/addUser',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/user/AddUser.vue'),
                    meta: { title: '添加/修改管理员用户' }
                },
                {
                    path: '/userList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/user/userList.vue'),
                    meta: { title: 'APP用户列表' }
                },
                {
                    path: '/adminList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/admin/adminList.vue'),
                    meta: { title: '后台用户列表' }
                },
                {
                    path: '/commentList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/comment/commentList.vue'),
                    meta: { title: '评论列表' }
                },{
                    path: '/sensitiveList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/sensitiveList.vue'),
                    meta: { title: '敏感词管理' }
                },{
                    path: '/logList',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/logList.vue'),
                    meta: { title: '日志查看' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/login/Login.vue'),
            meta: { title: '登录' }
        }
    ]
});
