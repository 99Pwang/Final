<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" style="padding-bottom: 20px" />
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user" />
      </el-header>

      <el-main>
<!--        child routes of the current page will be displayed within <router-view />.-->
        <router-view @refreshUser="getUser" />
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      user: {}
    }
  },
  components: {
    Aside,
    Header
  },
  created() {
    // get latest User data from backend
    this.getUser()
  },
  methods: {
    collapse() {  // click to collapse
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // collapse
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // expand
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        this.request.get("/user/username/" + username).then(res => {
          // refresh the latest User data from the backend
          this.user = res.data
        })
      }
    }
  }
}
</script>

