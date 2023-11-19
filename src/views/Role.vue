<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="please enter role name" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--      <el-input style="width: 200px" placeholder="please enter email" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>-->
<!--      <el-input style="width: 200px" placeholder="please enter address" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>-->
      <el-button style="margin-left: 10px" class="ml-5" type="primary" @click="load">Search</el-button>
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
<!--      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">import <i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary" @click="exp" class="ml-5">export <i class="el-icon-top"></i></el-button>-->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="Role"></el-table-column>
      <el-table-column prop="flag" label="Unique Identifier"></el-table-column>
      <el-table-column prop="description" label="Description"></el-table-column>
      <el-table-column label="Operation"  width="400" align="center">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">Menu allocation <i class="el-icon-menu"></i></el-button>
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

    <el-dialog title="Role Information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="125px" size="small">
        <el-form-item label="Role">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Unique identifier">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Menu allocation" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">Cancel</el-button>
        <el-button type="primary" @click="saveRoleMenu">Submit</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: '',
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/menu/ids").then(r => {
        this.ids = r.data
      })

    },
    save() {
      this.request.post("/role", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("save success")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("save failed")
        }
      })
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '200') {
          this.$message.success("bind success")
          this.menuDialogVis = false

          // need login again
          if (this.roleFlag === 'ROLE_ADMIN') {
            this.$store.commit("logout")
          }

        } else {
          this.$message.error(res.msg)
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
      this.request.delete("/role/" + id).then(res => {
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
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/role/del/batch", ids).then(res => {
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
    async selectMenu(role) {
      this.roleId = role.id
      this.roleFlag = role.flag

      // request menu data
      this.request.get("/menu").then(res => {
        this.menuData = res.data

        // process the menu data returned from the backend into an array of IDs
        this.expends = this.menuData.map(v => v.id)
      })

      this.menuDialogVis = true
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
        this.$refs.tree.setCheckedKeys(res.data)
        this.ids.forEach(id => {
          if (!this.checks.includes(id)) {
            this.$refs.tree.setChecked(id, false)
          }
        })
      })
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
