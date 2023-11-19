<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 220px" placeholder="please enter position name" suffix-icon="el-icon-search" v-model="name"></el-input>&nbsp;
      <el-select v-model="value"  placeholder="please select education">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>&nbsp;
      <el-select v-model="value2" placeholder="please select education experience">&nbsp;
        <el-option
            v-for="item in options2"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-button class="ml-5" type="primary" style="margin-left: 10px" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Yes'
          cancel-button-text='No'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure you want to delete these data?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <!--export-->
      <el-button type="primary" @click="exp" style="margin-left: 10px" class="ml-5">Export <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="70" sortable></el-table-column>
      <el-table-column prop="name" label="Position Name" width="110"></el-table-column>
      <el-table-column prop="user" label="Name"></el-table-column>
      <el-table-column prop="education" label="Education"></el-table-column>
      <el-table-column prop="experiences" label="Education Experience"></el-table-column>
      <el-table-column prop="isWork" label="AU Experience"></el-table-column>
      <el-table-column prop="isRecommend" label="Recommendation Letter"><template slot-scope="scope"><el-button type="primary" @click="download(scope.row.isRecommend)">Download</el-button></template></el-table-column>
      <el-table-column label="CV"><template slot-scope="scope"><el-button type="primary" @click="download(scope.row.file)">Download</el-button></template></el-table-column>
      <el-table-column prop="scores" label="Score"></el-table-column>
      <el-table-column prop="state" label="Successful Candidate"></el-table-column>
      <el-table-column prop="time" label="Interview Schedule" width="110"></el-table-column>
      <el-table-column prop="phone" label="Contact Information" width="110"></el-table-column>
      <el-table-column prop="department" label="Subject"></el-table-column>


      <el-table-column label="Operation"  width="480" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="cal(scope.row)" v-if="user.role === 'ROLE_ADMIN'">Score Calculation</el-button>
          <el-button type="success" @click="handleEdit(scope.row)"  v-if="user.role === 'ROLE_ADMIN'">Successful Candidate</el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Yes'
              cancel-button-text='No'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete?"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Successful Candidate" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="200px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
<!--        <el-form-item prop="scores" label="Score" v-if="user.role === 'ROLE_ADMIN'">-->
<!--          <el-input v-model="form.scores" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item prop="state" label="Set as Successful Candidate" v-if="user.role === 'ROLE_ADMIN'">
          <template>
            <el-radio v-model="form.state" label="Yes">Yes</el-radio>
            <el-radio v-model="form.state" label="No">No</el-radio>
          </template>
        </el-form-item>
        <el-form-item prop="time" label="Interview Schedule" v-if="user.role === 'ROLE_ADMIN'">
          <el-date-picker v-model="form.time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="select time"></el-date-picker>
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
  name: "Submit",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: 'please enter name', trigger: 'blur'}
        ],
      },
      options: [{
        value: 'Bachelor',
        label: 'Bachelor'
      }, {
        value: 'Master',
        label: 'Master'
      }, {
        value: 'PhD',
        label: 'PhD'
      }],
      value: '',
      options2: [{
        value: '1-2 years',
        label: '1-2 years'
      }, {
        value: '3-4 years',
        label: '3-4 years'
      }, {
        value: '5-6 years',
        label: '5-6 years'
      }, {
        value: '7-8 years',
        label: '7-8 years'
      }, {
        value: 'More than 9 years',
        label: 'More than 9 years'
      }],
      value2: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    cal(obj){
      this.request.post("/submit/score", obj).then(res => {
        if (res.code === '200') {
          this.$message.success("operation success")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load() {
      this.request.get("/submit/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          education: this.value,
          experiences: this.value2,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.request.post("/submit", this.form).then(res => {
              if (res.code === '200') {
                this.$message.success("save success")
                this.dialogFormVisible = false
                this.load()
              } else {
                this.$message.error(res.msg)
              }
            })
          }
        })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    del(id) {
      this.request.delete("/submit/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("delete success")
          this.load()
        } else {
          this.$message.error("delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("Please select the data you want to delete")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/submit/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("delete success")
          this.load()
        } else {
          this.$message.error("delete failed")
        }
      })
    },
    reset() {
      this.name = ""
      this.value = ""
      this.value2 = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/submit/export")
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
