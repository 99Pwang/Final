<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="please enter username" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Search</el-button>
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
      <el-button type="primary" @click="exp" style="margin-left: 10px" class="ml-5">Export <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="Username" width="140"></el-table-column>
      <el-table-column label="Role">
        <template v-slot="scope">
          <span v-if="scope.row.role">{{ roles.find(v => v.flag === scope.row.role) ? roles.find(v => v.flag === scope.row.role).name : ''  }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="Name" width="120"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
      <el-table-column prop="phone" label="Phone"></el-table-column>
<!--      <el-table-column prop="address" label="Address"></el-table-column>-->
      <el-table-column prop="education" label="Education"></el-table-column>
      <el-table-column prop="experiences" label="Experience"></el-table-column>
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

    <el-dialog title="Candidate Information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="Username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="Role">-->
<!--          <el-select clearable v-model="form.role" placeholder="Please select role" style="width: 100%">-->
<!--            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="Name">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Address">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Education">
          <template>
            <el-radio v-model="form.education" label="Bachelor">Bachelor</el-radio>
            <el-radio v-model="form.education" label="Master">Master</el-radio>
            <el-radio v-model="form.education" label="PhD">PhD</el-radio>
            <el-radio v-model="form.education" label="">None</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="Experience">
          <template>
            <el-radio v-model="form.experiences" label="1-2 years">1-2 years</el-radio>
            <el-radio v-model="form.experiences" label="3-4 years">3-4 years</el-radio>
            <el-radio v-model="form.experiences" label="5-6 years">5-6 years</el-radio>
            <el-radio v-model="form.experiences" label="7-8 years">7-8 years</el-radio>
            <el-radio v-model="form.experiences" label="More than 9 years">More than 9 years</el-radio>
            <el-radio v-model="form.experiences" label="">None</el-radio>
          </template>
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
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username
        }
      }).then(res => {

        this.tableData = res.data.records.filter(v => v.role === 'ROLE_USER')
        this.total = res.data.total

      })

      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    save() {
      this.form.role = 'ROLE_USER'
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("Save success")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save failed")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete success")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete success")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    reset() {
      this.username = ""
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
    exp() {
      window.open("http://localhost:9090/user/export")
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
