<template xmlns="http://www.w3.org/1999/html">
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="mgb20" style="height: 252px">
          <div class="user-info">
            <el-avatar :size="120" :src="imgurl"/>
            <div class="user-info-cont">
              <div class="user-info-name">{{ name }}</div>
              <div>{{ role }}</div>
            </div>
          </div>
          <div class="user-info-list">
            上次登录时间：
            <span>2022-10-01</span>
          </div>
          <div class="user-info-list">
            上次登录地点：
            <span>东莞</span>
          </div>
        </el-card>
        <el-card shadow="hover" style="height: 252px">
          <template #header>
            <div class="clearfix">
              <span>图库分类详情</span>
            </div>
          </template>
          风景
          <el-progress :percentage="79.4" color="#42b983"></el-progress>
          人像
          <el-progress :percentage="14" color="#f1e05a"></el-progress>
          写实
          <el-progress :percentage="5.6"></el-progress>
          山川
          <el-progress :percentage="1" color="#f56c6c"></el-progress>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-1">
                <el-icon class="grid-con-icon">
                  <User/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">1234</div>
                  <div>用户访问量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-2">
                <el-icon class="grid-con-icon">
                  <ChatDotRound/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">321</div>
                  <div>系统消息</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" :body-style="{ padding: '0px' }">
              <div class="grid-content grid-con-3">
                <el-icon class="grid-con-icon">
                  <Goods/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">5000</div>
                  <div>图片总数</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-card shadow="hover" style="height: 100%">
          <template #header>
            <div class="clearfix">
              <span>待审核内容</span>
              <el-button @click="review" style="float: right; padding: 3px 0" text>审核</el-button>
            </div>
          </template>
          <el-table :show-header="true" :data="todoList" class="table" ref="multipleTable"
                    header-cell-class-name="table-header">
            <el-table-column width="40">
              <template #default="scope">
                <el-checkbox v-model="scope.row.index"
                             @change="handleCheckboxChange(scope.row.index,scope.row.id)"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="标题-内容">
              <template #default="scope">
                <span>   {{ scope.row.title }}</span>
                <br>
                <span> {{ scope.row.content }}</span>
              </template>
            </el-table-column>
            <el-table-column label="图片" align="center">
              <template #default="scope">
                <div @click="showImage(scope.row.url)">
                  <el-image :src="scope.row.url" style="width: 50px; height: 50px"></el-image>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="审核状态" width="300" align="center">
              <template #default="scope">
                 <span :class="getStatusClass(scope.row.status)">
                   {{ getStatusText(scope.row.status) }}
                 </span>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
                :current-page="currentPage"
                :page-size="pageSize"
                :total="pageTotal"
                @current-change="handlePageChange"
                layout="total, prev, pager, next"/>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
        v-model="dialogVisible"
        width="500px"
        destroy-on-close
        :modal="false"
        :close-on-click-modal="true">
      <el-image-viewer v-if="imageViewerVisible" :url-list="imageList" :initial-index="initialIndex"
                       @close="closeViewer"></el-image-viewer>
      <!--      <el-image :src="currentImageUrl"></el-image>-->
    </el-dialog>
    <!-- <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <schart ref="bar" class="schart" canvasId="bar" :options="options"></schart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <schart ref="line" class="schart" canvasId="line" :options="options2"></schart>
        </el-card>
      </el-col>
    </el-row> -->
  </div>
</template>

<script setup lang="ts" name="dashboard">
import Schart from 'vue-schart';
import {onMounted, reactive, ref} from 'vue';
import imgurl from '../assets/img/img.jpg';
import request from "../utils/request";
import PermissionEdit from "../components/permission-edit.vue";
import {formToJSON} from "axios";
import {ElMessage, ElMessageBox} from "element-plus";

const name = localStorage.getItem('ms_username');
const role: string = name === 'admin' ? '超级管理员' : '普通用户';

const currentPage = ref(1);
const pageSize = ref(6);
const pageTotal = ref(0);
const todoList = ref([]);
const initialIndex = ref(0)
const imageViewerVisible = ref(false)

const authDialogVisible = ref(false);
const dialogVisible = ref(false);
const currentImageUrl = ref('');
const imageList = ref([]);

const checkedContents = ref([])

const handleCheckboxChange = (checked: boolean, id: number) => {
  console.log("handleCheckboxChange id:" + id + " checked:" + checked);
  if (checked) {
    checkedContents.value.push(id)
  } else {
    const index = checkedContents.value.indexOf(id);
    if (index !== -1) {
      checkedContents.value.splice(index, 1);
    }
  }
}
const review = () => {
  console.log("review cli->" + checkedContents.value)
  if (checkedContents.value.length == 0) {
    alert("没有选中数据")
    return;
  }
  ElMessageBox.confirm('内容审核', '提示', {
    type: 'info',
    confirmButtonText: '审核通过',
    cancelButtonText: '审核不通过'
  })
      .then(() => {
         request({
          url: '/admin/content/review',
          method: 'post',
           data: {"ids":checkedContents.value}
        }).then(response=>{
           console.log("review response->"+response.data.result)
           loadTodoList()
         }).catch(e=>{

         });
      })
      .catch(() => {
        ElMessageBox.confirm('您确定要将当前内容设置为审核不通过吗？', '提示', {
          type: 'warning',
        }).then(()=>{
           request({
            url: '/admin/content/reject',
            method: 'post',
            data: {"ids":checkedContents.value}
          }).then(response=>{
             console.log("reject response->"+response.data.result)
             loadTodoList()
           }).catch(e=>{

           });;
        }).catch(()=>{

        })
      });

}
const showImage = (url: string) => {
  console.log("showImage cli->" + url)
  currentImageUrl.value = url;
  dialogVisible.value = true;
  imageList.value.push(url)
  imageViewerVisible.value = true
};

const closeViewer = () => {
  // showViewer = false
  console.log("closeViewer")
  imageViewerVisible.value = false
  dialogVisible.value = false;
}
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '待审核';
    case 1:
      return '自动审核通过，待人工审核';
    case 2:
      return '自动审核不通过，待人工审核';
    case 3:
      return '人工审核通过';
    case 4:
      return '人工审核不通过';
    default:
      return '';
  }
};

const getStatusClass = (status) => {
  switch (status) {
    case 0:
      return 'status-pending';
    case 1:
      return 'status-passed';
    case 2:
      return 'status-failed';
    case 3:
      return 'status-approved';
    case 4:
      return 'status-rejected';
    default:
      return '';
  }
};

const fetchData = () => {
  return request({
    url: '/admin/content/wait_review',
    params: {
      page: currentPage.value,
      pageSize: pageSize.value
    },
    method: 'get'
  });
};

const loadTodoList = () => {
  fetchData().then(response => {
    todoList.value = response.data.hashMap.data;
    pageTotal.value = response.data.hashMap.total;
    todoList.value =  todoList.value.map(item => {
      item.index = false; // 重置复选框状态为未选中
      return item;
    });
  }).catch(error => {
    console.error('Error loading todo list:', error);
  });
}
// 在组件加载时自动加载数据
onMounted(() => {
  loadTodoList();
});

// 分页事件处理方法
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  loadTodoList(); // 加载当前页数据
};

const handlePageChange = (val: number) => {
  currentPage.value = val;
  loadTodoList()
};


const options = {
  type: 'bar',
  title: {
    text: '最近一周各品类销售图'
  },
  xRorate: 25,
  labels: ['周一', '周二', '周三', '周四', '周五'],
  datasets: [
    {
      label: '家电',
      data: [234, 278, 270, 190, 230]
    },
    {
      label: '百货',
      data: [164, 178, 190, 135, 160]
    },
    {
      label: '食品',
      data: [144, 198, 150, 235, 120]
    }
  ]
};
const options2 = {
  type: 'line',
  title: {
    text: '最近几个月各品类销售趋势图'
  },
  labels: ['6月', '7月', '8月', '9月', '10月'],
  datasets: [
    {
      label: '家电',
      data: [234, 278, 270, 190, 230]
    },
    {
      label: '百货',
      data: [164, 178, 150, 135, 160]
    },
    {
      label: '食品',
      data: [74, 118, 200, 235, 90]
    }
  ]
};

</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}

.status-pending {
  color: gray;
}

.status-passed {
  color: darkseagreen;
}

.status-failed {
  color: red;
}

.status-approved {
  color: green;
}

.status-rejected {
  color: darkred;
}

</style>
