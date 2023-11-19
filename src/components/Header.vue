<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1;">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>

      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <el-breadcrumb-item :to="'/'">Home</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 150px; cursor: pointer; text-align: right;">
      <div style="display: inline-block;">
        <img :src="user.avatarUrl" alt=""
             style="width: 30px; border-radius: 20%; position: relative; top: 5px; right: 5px">
        <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 150px; text-align: center">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/password">Change password</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <router-link to="/person">Personal information</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <div style="text-decoration: none"  @click="logout">Log out</div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    user: Object
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　
    }
  },
  data() {
    return {

    }
  },
  methods: {
    collapse() {
      // this.$parent.$parent.$parent.$parent.collapse()  //
      this.$emit("asideCollapse")
    },
    logout() {
      this.$store.commit("logout")
      this.$message.success("Logout successful")
    }
  }
}
</script>

<style scoped>
</style>
