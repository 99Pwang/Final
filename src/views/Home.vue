<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>Hello！{{ user.nickname }}</b>
    </div>
    <el-card>
      Welcome to AU HR system :D
      <el-divider />
      <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">
        About
      </el-button>

      <el-drawer
          :visible.sync="drawer"
          :with-header="false">
        <div><h1>Supervisor: </h1></div>
        <div><h1>Ajmery Sultana</h1></div>
        <div><h1>— — — — — — — — —</h1></div>
        <div><h1>Author:</h1></div>
        <div><h1>Peilongyu Wang</h1></div>
        <div><h1>— — — — — — — — —</h1></div>
        <div><h1>Frontend:</h1></div>
        <div><h1>Html, Css, JavaScript</h1></div>
        <div><h1>Vue</h1></div>
        <div><h1>Element-UI</h1></div>
        <div><h1>— — — — — — — — —</h1></div>
        <div><h1>Backend:</h1></div>
        <div><h1>Java</h1></div>
        <div><h1>Spring Boot</h1></div>
        <div><h1>MyBatis-Plus</h1></div>
        <div><h1>JWT</h1></div>
        <div><h1>— — — — — — — — —</h1></div>
        <div><h1>Database:</h1></div>
        <div><h1>Mysql</h1></div>

      </el-drawer>
    </el-card>

    <el-calendar>
      <template
          slot="dateCell"
          slot-scope="{date, data}">
        <p :class="data.isSelected ? 'is-selected' : ''">
          {{ data.day.split('-').slice(1).join('-') }} {{ data.isSelected ? '✔️' : ''}}
        </p>
      </template>
    </el-calendar>

  </div>



</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      drawer: false,
    }
  }
}
</script>

<style>
.is-selected {
  color: #B3282D;
}
.el-drawer__body{

  background: rgb(236, 236, 236);

}
.el-drawer h1 {

  text-transform: uppercase;
  //padding-left: 5px;
  color: #fff;
  text-align: center;
  letter-spacing: 3px;
  padding-top: 10%;

  width: 100%;
  font-size: 20px;

  text-shadow:     0 1px 0 hsl(174,5%,80%),
  0 2px 0 hsl(174,5%,75%),
  0 3px 0 hsl(174,5%,70%),
  0 4px 0 hsl(174,5%,66%),
  0 5px 0 hsl(174,5%,64%),
  0 6px 0 hsl(174,5%,62%),
  0 7px 0 hsl(174,5%,61%),
  0 8px 0 hsl(174,5%,60%),

  0 0 5px rgba(0,0,0,.05),
  0 1px 3px rgba(0,0,0,.2),
  0 3px 5px rgba(0,0,0,.2),
  0 5px 10px rgba(0,0,0,.2),
  0 10px 10px rgba(0,0,0,.2),
  0 20px 20px rgba(0,0,0,.3);
}
</style>
