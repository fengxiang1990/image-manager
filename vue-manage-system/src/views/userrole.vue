<template>
    <div>
        <div class="container">
            <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
                <el-table-column prop="id" label="ID" width="400" align="center"></el-table-column>
                <el-table-column prop="username" label="用户" align="center"></el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
                <el-table-column prop="updateTime" label="更新时间" align="center"></el-table-column>
                <el-table-column label="操作" width="280" align="center" v-if="isAdmin">
                    <template #default="scope">
                        <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(scope.$index, scope.row)">
                            授权
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>
        <el-dialog :title="idEdit ? '授权' : '新增'" v-model="visible" width="500px" destroy-on-close
            :close-on-click-modal="false" @close="closeDialog">
            <UserGrantRole :data="rowData" :edit="idEdit" :update="updateData" @refresh-perms="handleSaveSuccess" />
        </el-dialog>
    </div>
</template>

<script setup lang="ts" name="basetable">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Edit, Search, CirclePlusFilled, View } from '@element-plus/icons-vue';
import UserGrantRole from '../components/usergrantrole.vue';
import request from '../utils/request';
import { usePermissStore } from '../store/permiss';

const isAdmin = ref(false);

onMounted(() => {
  const permissStore = usePermissStore();
  const role = permissStore.getPermission;
  isAdmin.value = role === 'admin';
  console.log("role->"+role);
  console.log("isAdmin->"+isAdmin.value);
  
});


interface TableItem {
    id: number;
    username: string;
    createTime: string;
    updateTime: string;
}

const query = reactive({
    username: '',
    pageIndex: 1,
    pageSize: 100
});
const tableData = ref<TableItem[]>([]);
const pageTotal = ref(0);

const fetchData = () => {
    return request({
        url: '/admin/users',
        method: 'get'
    });
};


const handleSaveSuccess = () => {
    console.log("handleSaveSuccess")
    getData()
}

/**
 * 日期格式化
 */
function formatDate(dateArray: number[]): string {
    const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5]);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
}


// 获取表格数据
const getData = async () => {
    const res = await fetchData();
    console.log("res:" + JSON.stringify(res));
    for (let i = 0; i < res.data.sysUserModelList.length; i++) {
        res.data.sysUserModelList[i].updateTime = formatDate(res.data.sysUserModelList[i].updateTime)
        res.data.sysUserModelList[i].createTime = formatDate(res.data.sysUserModelList[i].createTime)
    }
    tableData.value = res.data.sysUserModelList;
    console.log("size:" + res.data.sysUserModelList.length);
    pageTotal.value = res.data.sysUserModelList.length;
};
getData();

// 查询操作
const handleSearch = () => {
    query.pageIndex = 1;
    getData();
};
// 分页导航
const handlePageChange = (val: number) => {
    query.pageIndex = val;
    getData();
};

// 删除操作
const handleDelete = (index: number) => {
    // 二次确认删除
    ElMessageBox.confirm('确定要删除吗？', '提示', {
        type: 'warning'
    })
        .then(() => {
            const id = tableData.value[index].id
            console.log("deleteFunc id->" + id);
            const deleteFunc = async (id: number) => {
                const res = await deletePerm(id);
                ElMessage.success('删除成功');
                getData();
            }
            deleteFunc(id);

            // tableData.value.splice(index, 1);
        })
        .catch(() => { });
};


const deletePerm = (id: number) => {
    return request({
        url: '/admin/deletePermission/' + id,
        method: 'delete',
    });
};

const visible = ref(false);
let idx: number = -1;
const idEdit = ref(false);
const rowData = ref({});
const handleEdit = (index: number, row: TableItem) => {
    idx = index;
    rowData.value = row;
    idEdit.value = true;
    visible.value = true;
};
const updateData = (row: TableItem) => {
    idEdit.value ? (tableData.value[idx] = row) : tableData.value.unshift(row);
    console.log(tableData.value);
    closeDialog();
};

const closeDialog = () => {
    visible.value = false;
    idEdit.value = false;
};

const visible1 = ref(false);
const handleView = (row: TableItem) => {
    rowData.value = row;
    visible1.value = true;
};
</script>

<style scoped>
.search-box {
    margin-bottom: 20px;
}

.search-input {
    width: 200px;
}

.mr10 {
    margin-right: 10px;
}

.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
