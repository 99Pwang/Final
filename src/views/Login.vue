<template>
  <div class="wrapper">
    <div style="height: 100vh; overflow: hidden; display: flex;align-items: center;justify-content: center;background-color:#f2f5de;">

      <!--background video-->
      <div id="video-background">
        <video autoplay muted loop>
          <source src="../assets/video.mp4" type="video/mp4">

        </video>
      </div>

      <!--cards-->
      <div class="cards-container">
        <div class="shell">
          <div class="card">
            <div class="wrapper">
              <img :src="coverImages[currentCoverImageIndex]" alt="" class="cover-image">
            </div>
            <img :src="titleImages[currentTitleImageIndex]" alt="" class="title">
            <img :src="characterImages[currentCharacterImageIndex]" alt="" class="character">
          </div>
        </div>
      </div>
<!--login box-->
    <div style="display: flex; background-color: #fff; width: 614px; height: 505px;box-shadow:rgb(0, 0, 0) 0px 0px 3px 0px inset;
    padding-right: 25px;box-sizing: border-box;border-radius: 20px ;overflow: hidden">
      <!--left part-->
      <div style="flex: 0.7;background-color:#B3282D;">
        <img src="../assets/AlgomaU_Logo2.jpg" style="width: 100%;">
        <div style="color: white;margin-left: 20px;font-style:italic">Hello ! &#128512</div>
        <div style="color: white;margin-left: 20px;margin-top: 10px;font-style:italic">Welcome to AU HR System !</div>
        <div style="color: white;margin-left: 20px;margin-top: 15px;font-style:italic">Are you interested in taking the first step in joining our team ?</div>
        <div style="color: white;margin-left: 20px;margin-top: 20px;font-style:italic">Let's get started ! &#128522</div>
      </div>
<!--      right part-->
      <div style="flex: 1;padding-top: 20px;padding-bottom: 20px;padding-left: 25px;padding-right: 25px">
        <div style="font-size: 20px;font-weight: bold;text-align: center;margin-bottom: 30px;margin-top: 25px">
          <a class="logo" href="https://algomau.ca/" target="_blank"><img src="../assets/logo-primary.svg" alt=""></a>
        </div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" placeholder="username" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" show-password placeholder="password" v-model="user.password"></el-input>
        </el-form-item>
        <!--        <el-form-item>-->
        <!--          <div style="display: flex">-->
        <!--            <el-input size="mid" v-model="code" style="width: 200px"></el-input>-->
        <!--            <span @click="refreshCode" style="cursor: pointer; flex: 1;">-->
        <!--              <Identify :identifyCode="identifyCode"></Identify>-->
        <!--           </span>-->
        <!--          </div>-->
        <!--        </el-form-item>-->

        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/register')">Register</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="login">Login</el-button>

        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="text" size="mid"  autocomplete="off" @click="handlePass">Forgot password?</el-button>
        </el-form-item>
      </el-form>
    </div>
    </div>


    <el-dialog title="Reset password" :visible.sync="dialogFormVisible" width="30%" :lock-scroll="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="Username">
          <el-input v-model="pass.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input v-model="pass.phone" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="passwordBack">Reset password</el-button>
      </div>
    </el-dialog>
    </div>
  </div>
</template>

<script>
import {resetRouter, setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      pass: {},
      code: '',
      dialogFormVisible: false,
      // // Image CAPTCHA
      // identifyCode: '',
      // // CAPTCHA rule
      // identifyCodes: '3456789ABCDEFGHGKMNPQRSTUVWXY',
      rules: {
        username: [
          { required: true, message: 'please enter username', trigger: 'blur' },
          { min: 3, max: 20, message: 'length should be between 3 and 20 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'please enter password', trigger: 'blur' },
          { min: 1, max: 20, message: 'length should be between 1 and 20 characters', trigger: 'blur' }
        ],
      },
      currentCoverImageIndex: 0,
      currentTitleImageIndex: 0,
      currentCharacterImageIndex: 0,
      coverImages: [
        require('../assets/01.png'),
        require('../assets/03.png'),
        require('../assets/05.png'),
      ],
      titleImages: [
        require('../assets/text-1.png'),
        require('../assets/text-2.png'),
        require('../assets/text-3.png'),
      ],
      characterImages: [
        require('../assets/02.png'),
        require('../assets/04.png'),
        require('../assets/06.png'),
      ],
    }
  },
  mounted() {
    // 重置路由
    resetRouter()
    // this.refreshCode()
    setInterval(() => {
      // console.log('check')
      this.currentCoverImageIndex = (this.currentCoverImageIndex + 1) % this.coverImages.length;
      this.currentTitleImageIndex = (this.currentTitleImageIndex + 1) % this.titleImages.length;
      this.currentCharacterImageIndex = (this.currentCharacterImageIndex + 1) % this.characterImages.length;
    }, 6000);
  },
  methods: {
    login() {
      // if (this.code !== this.identifyCode.toLowerCase()) {
      //   this.$message({
      //     type: "error",
      //     message: "Incorrect verification code"
      //   })
      //   return;
      // }
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            if(res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // save user data to browser
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // save user data to browser

              setRoutes()
              this.$router.push("/")
              this.$message.success("Login success")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    // // Change CAPTCHA
    // refreshCode() {
    //   this.identifyCode = ''
    //   this.makeCode(this.identifyCodes, 4)
    // },
    // // Generate random CAPTCHA
    // makeCode(o, l) {
    //   for (let i = 0; i < l; i++) {
    //     this.identifyCode += this.identifyCodes[Math.floor(Math.random() * (this.identifyCodes.length))]
    //   }
    // },
    handlePass() {
      this.dialogFormVisible = true
      this.pass = {}
    },
    passwordBack() {
      this.request.put("/user/reset", this.pass).then(res => {
        if (res.code === '200') {
          this.$message.success("Password reset successful, your new password is: 123. Please change your password as soon as possible")
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
div[style*="height: 100vh;"] {
  z-index: 0;
  background-color: transparent !important;

}


#video-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

#video-background video {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  min-width: 100%;
  min-height: 100%;
  width: auto;
  height: auto;
  filter: brightness(67%) contrast(80%);


}



.cards-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

}


.card {

  height: 504px;
  width: 250px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  padding: 0 25px;
  perspective: 2500px;
  transition: all 0.4s;
  margin: 0 10px;
}

.cover-image {
  width: 100%;
  height: 100%;
}

.wrapper {
  transition: all 0.3s;
  position: absolute;
  width: 100%;
  z-index: -1;
}

.card:hover .wrapper {
  transform: perspective(900px) translateY(-5%) rotateX(25deg) translateZ(0);
  box-shadow: 2px 35px 32px -8px rgba(0, 0, 0, 0.75);
}

.wrapper::before,
.wrapper::after {
  content: "";
  opacity: 0;
  width: 100%;
  height: 80px;
  transition: all 0.3s;
  position: absolute;
  left: 0;
}

.wrapper::before {
  top: 0;
  height: 100%;
  background-image: linear-gradient(to top,
  transparent 46%,
  rgba(12, 13, 19, 0.5) 68%,
  rgba(12, 13, 19) 97%);
}

.wrapper::after {
  bottom: 0;
  opacity: 1;
  background-image: linear-gradient(to bottom,
  transparent 46%,
  rgba(12, 13, 19, 0.5) 68%,
  rgba(12, 13, 19) 97%);
}

.card:hover .wrapper::before,
.wrapper::after {
  opacity: 1;
}

.card:hover .wrapper::after {
  height: 120px;
}

.title {
  width: 100%;
  transition: transform 0.3s;
}

.card:hover .title {
  transform: translate3d(0%, -50px, 100px);
}

.character {
  width: 100%;
  opacity: 0;
  transition: all 0.3s;
  position: absolute;
  z-index: -1;
}

.card:hover .character {
  opacity: 1;
  transform: translate3d(0%, -20%, 100px);
}

.card:hover {
  transform: translateY(40px);
}
</style>
