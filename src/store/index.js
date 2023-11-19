import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentPathName: ''
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        },
        logout() {
            // clear cache
            localStorage.removeItem("user")
            localStorage.removeItem("menus")
            router.push("/login")
            // reset router
            resetRouter()
        }
    }
})

export default store
