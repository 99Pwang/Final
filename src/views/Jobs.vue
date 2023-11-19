<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 220px" placeholder="please enter position name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" style="margin-left: 10px" type="primary" @click="load">Search</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">Add <i class="el-icon-circle-plus-outline"></i></el-button>
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
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="name" label="Position Name"></el-table-column>
      <el-table-column prop="nums" label="Expected recruitment number" ></el-table-column>
      <el-table-column prop="content" label="Description">
        <template v-slot="scope">
          <el-button type="primary" @click="view(scope.row.content)">View Description</el-button>
        </template>
      </el-table-column>
      <el-table-column label="Cover" ><template slot-scope="scope"><el-image style="width: 100px; height: 100px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image></template></el-table-column>
      <el-table-column prop="time" label="Publish Time"></el-table-column>
      <el-table-column prop="department" label="Subject"></el-table-column>

      <el-table-column label="Operation"  width="220" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
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

    <el-dialog title="Position Information" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal="false">
      <el-form label-width="215px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="name" label="Position Name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="nums" label="Expected recruitment number">
          <el-input v-model="form.nums" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item prop="content" label="Position Description">-->
<!--          <el-input type="textarea" v-model="form.content" autocomplete="off" :rows="20"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="Position Description">
          <div id="richText"></div>
        </el-form-item>
        <el-form-item prop="img" label="Cover">
          <el-upload action="http://localhost:9090/file/upload" ref="img" :on-success="handleImgUploadSuccess">
            <el-button size="small" type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
<!--        <el-form-item prop="time" label="Add time">-->
<!--          <el-date-picker v-model="form.time" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间"></el-date-picker>-->
<!--        </el-form-item>-->
        <el-form-item prop="department" label="Subject">
          <template>
            <el-select v-model="form.department" placeholder="Please select subject">
              <el-option v-for="item in departments" :key="item.id" :label="item.name" :value="item.name">
              </el-option>
            </el-select>
          </template>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Position Description" :visible.sync="dialogFormVisible1" width="60%" :close-on-click-modal="false">
      <el-card>
        <div v-html="content"></div>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import i18next from 'i18next';
import E from "wangeditor"
let editor


export default {
  name: "Jobs",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      dialogFormVisible1: false,
      content: '',
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: 'please enter name', trigger: 'blur'}
        ],
      },
      departments: []
    }
  },
  created() {
    editor = ''
    this.load()
  },
  methods: {
    view(data){
      this.content = data
      this.dialogFormVisible1 = true;
    },
    load() {
      this.request.get("/jobs/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/department").then(res => {
        this.departments = res.data
      })
    },
    save() {
      this.form.content = editor.txt.html()
      this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.request.post("/jobs", this.form).then(res => {
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
        if (!editor){
          editor  = new E("#richText")
          editor.i18next = i18next;
          editor.config.lang = 'en' ;
          // 配置 server 接口地址
          editor.config.uploadImgServer = 'http://localhost:9090/file/uploadImg'
          editor.config.uploadFileName = 'file'
          editor.create()
        }
        editor.txt.html('')

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
         if (!editor){
           editor = new E("#richText")
           editor.config.uploadImgServer = 'http://localhost:9090/file/uploadImg'
           editor.config.uploadFileName = 'file'
           editor.create()
         }
         editor.txt.html(this.form.content)

         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    del(id) {
      this.request.delete("/jobs/" + id).then(res => {
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
      this.request.post("/jobs/del/batch", ids).then(res => {
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
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
