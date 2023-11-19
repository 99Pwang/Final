<template>
  <div style="padding: 10px">
    <el-button style="margin-bottom: 20px" type="primary" @click="$router.push('/card')">back</el-button>
    <el-row :gutter="10">
      <el-col :span="24">
        <el-card style="height: 120px">
          <div class="item1" style="margin: 0 0 0 25px">
            <div style="margin-top: 10px">
              <span style="font-size: 23px;font-weight:bold;color: #E6A23C">{{ jobs.name }}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <span style="color: #666666">Expected Recruitment Number</span>&nbsp;&nbsp;:&nbsp;&nbsp;<span style="color: #222">{{jobs.nums }}</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span style="color: #666666">Number of Applicants</span>&nbsp;&nbsp;:&nbsp;&nbsp;<span style="color: #222">{{jobs.submitNums }}</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span style="color: #666666">Subject</span>&nbsp;&nbsp;:&nbsp;&nbsp;<span style="color: #222">{{jobs.department }}</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span style="color: #666666">Publish Time</span>&nbsp;&nbsp;:&nbsp;&nbsp;<span style="color: #222">{{jobs.time }}</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span style="color: #666666">
                        <el-button type="primary" @click="handle">Apply</el-button>
              </span>&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row  :gutter="10" style="margin-top: 10px">
      <el-col :span="24">
        <el-card style="height: 550px;">
          <img :src="jobs.img" :preview-src-list="[jobs.img]"
               style="height: 505px;width: 100%"></img>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 15px">
      <div style="padding: 10px 0; border-bottom: 1px solid #ccc; color: #1E90FF; font-size: 20px">
        Position Description
      </div>

      <div style="margin-top: 15px" v-html="jobs.content"></div>
    </el-card>

    <el-dialog title="Candidate Information" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="180px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item label="Education">
          <template>
            <el-radio v-model="form.education" label="Bachelor">Bachelor</el-radio>
            <el-radio v-model="form.education" label="Master">Master</el-radio>
            <el-radio v-model="form.education" label="PhD">PhD</el-radio>
<!--            <el-radio v-model="form.education" label="More">More</el-radio>-->
          </template>
        </el-form-item>
        <el-form-item label="Experience">
          <template>
            <el-radio v-model="form.experiences" label="1-2 years">1-2 years</el-radio>
            <el-radio v-model="form.experiences" label="3-4 years">3-4 years</el-radio>
            <el-radio v-model="form.experiences" label="5-6 years">5-6 years</el-radio>
            <el-radio v-model="form.experiences" label="7-8 years">7-8 years</el-radio>
            <el-radio v-model="form.experiences" label="More than 9 years">More than 9 years</el-radio>
          </template>
        </el-form-item>
        <el-form-item prop="isWork" label="AU Experience">
          <template>
            <el-radio v-model="form.isWork" label="No">No</el-radio>
            <el-radio v-model="form.isWork" label="1-2 semesters">1-2 semesters</el-radio>
            <el-radio v-model="form.isWork" label="More than 1 year">More than 1 year</el-radio>
          </template>
        </el-form-item>
        <el-form-item prop="phone" label="Contact Information">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="isRecommend" label="Recommendation Letter">
          <el-upload action="http://localhost:9090/file/upload" ref="isRecommend" :on-success="handleRecommendationUploadSuccess">
            <el-button size="small" type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="file" label="CV">
          <el-upload action="http://localhost:9090/file/upload" ref="file" :on-success="handleFileUploadSuccess">
            <el-button size="small" type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Detail",
  data() {
    return {
      id: this.$route.query.id,
      jobs: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
      form: {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/jobs/" + this.id).then(res => {
        this.jobs = res.data
      })
    },
    handle(){
      this.dialogFormVisible = true;
      this.form = {}
    },
    handleFileUploadSuccess(res) {
      this.form.file = res

    },
    handleRecommendationUploadSuccess(res) {
      this.form.isRecommend = res;
    },
    save(){
      this.form.department = this.jobs.department
      this.form.name = this.jobs.name
      this.form.user = this.user.username
      this.request.post("/submit", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("apply success")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.item1 {
  padding: 10px;
  color: #666;
}
</style>