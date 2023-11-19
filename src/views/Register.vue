<template>
  <div class="wrapper1">
    <div style="height: 70px; line-height: 60px; font-size: 20px; padding-left: 10px; color: white;
      background-color: rgba(0,0,0,0.2)"><a class="logo" href="https://algomau.ca/" target="_blank">
      <img src="../assets/logo-primary.svg" alt="" style="margin-top: 12px;width: 10%"></a>
    </div>
    <div style="margin: 150px auto; background-color: #fff; width: 350px; height: 340px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>Register</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="please enter username" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="please enter your password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="please confirm your password" size="medium" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">back</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">Register</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          { required: true, message: 'please enter username', trigger: 'blur' },
          { min: 3, max: 20, message: 'length should be between 3 and 20 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'length should be between 1 and 20 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'length should be between 1 and 20 characters', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("The passwords entered do not match")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("Registration successful")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper1 {
  height: 100vh;
  background-image: url("../assets/bgc.jpg");
  background-size: cover;
  overflow: hidden;
}
</style>
