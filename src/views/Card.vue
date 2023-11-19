<template>
  <div style="color: #666;font-size: 14px;">

    <div style="margin: 20px 0">
      <div style="margin: 10px 0">
        <div style="padding: 10px 0; font-weight: bold; border-bottom: 1px solid #ccc; color: orangered; font-size: 20px">
          Position Open
        </div>
        <div style="display: flex;align-items: center;">
          <p style="margin-right: 10px;color:#B3282D ">Only display</p>
            <el-select v-model="form.department" placeholder="Please select subject" @change="filterBySubject">
              <el-option v-for="item in departments" :key="item.id" :label="item.name" :value="item.name">
              </el-option>
            </el-select>
        </div>

        <el-row :gutter="10" style="margin: 10px 0">
          <el-col :span="6" v-for="item in tableData" :key="item.id" style="margin-bottom: 10px">
            <div style="border: 1px solid #ccc; padding-bottom: 10px; border-radius: 10px; overflow: hidden">
              <img :src="item.img" alt="" style="width: 100%;height: 300px; cursor: pointer">
              <div style="padding: 5px; font-size: 18px;font-weight: bold; cursor: pointer;color: #494949;" @click="$router.push('/detail?id=' + item.id)">
                {{ item.name }}
              </div>
              <div style="padding: 5px; font-size: 15px;cursor: pointer;color: #494949;">
                Expected Recruitment Number：{{item.nums}}
              </div>
              <div style="padding: 5px; font-size: 15px;cursor: pointer;color: #494949;">
                Number of Applicants：{{item.submitNums}}
              </div>
              <div style="padding: 5px; font-size: 15px;cursor: pointer;color: #494949;">
                Subject：{{item.department}}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div style="padding: 10px 0" v-if="tableData.length">
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
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      pageNum: 1,
      pageSize: 5,
      total: 0,
      tableData: [],
      originalTableData: [],
      form: {},
      departments: [],
      selectedSubject: ''
    }
  },
  created() {
    this.load()
  },
  methods:{
    load() {
      console.log(this.type)
      this.request.get("/jobs/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        this.originalTableData = res.data.records;
        this.tableData = [...this.originalTableData];
        this.total = res.data.total
      })
      this.request.get("/department").then(res => {
        const departmentMap = {};
        res.data.forEach(item => {
          departmentMap[item.name] = item;
        });
        this.departments = Object.values(departmentMap);
      })

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
    filterBySubject() {
      const selectedSubject = this.form.department;

      if (!selectedSubject) {
        this.tableData = [...this.originalTableData];
      } else {
        this.tableData = this.originalTableData.filter(item => item.department === selectedSubject);
      }
    },
  }
}
</script>
