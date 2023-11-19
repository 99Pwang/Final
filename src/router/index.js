import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },

]

const router = new VueRouter({
  mode: 'history',
  routes
})

// reset route
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    routes
  })
}

// refreshing the page will reset the page route
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    // assembling dynamic routes
    const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
        { path: 'person', name: 'Personal information', component: () => import('../views/Person.vue')},
        { path: 'password', name: 'Change password', component: () => import('../views/Password.vue')},
        { path: 'card', name: 'Position Open', component: () => import('../views/Card.vue')},
        { path: 'detail', name: 'Positions', component: () => import('../views/Detail.vue')},
      ] }
    const menus = JSON.parse(storeMenus)
    menus.forEach(item => {
      if (item.path) {  // set the route only when the path is not empty
        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
        manageRoute.children.push(itemMenu)
      } else if(item.children.length) {
        item.children.forEach(item => {
          if (item.path) {
            let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
            manageRoute.children.push(itemMenu)
          }
        })
      }
    })

    // get the array of names of the current route objects
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      router.addRoute(manageRoute)
    }
  }
}

// every time refresh the page need to reset the route
setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // set the current route name
  store.commit("setPath")
  if (!to.matched.length) {
    const menus = localStorage.getItem("menus")
    if (!menus) {
      next("/login")
    } else {
      next("/404")
    }
  } else {
    next()
  }
})

export default router
