<template>
	<div>
		<div class="container">
			<el-button type="warning" :icon="CirclePlusFilled" @click="visible = true">新增</el-button>
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
				<el-table-column prop="name" label="名称" align="center"></el-table-column>
				<el-table-column prop="code" label="权限代码" align="center"></el-table-column>
                <el-table-column prop="description" label="描述" align="center"></el-table-column>
				<el-table-column prop="parent" label="父权限" align="center"></el-table-column>
				<el-table-column prop="level" label="权限级别" align="center"></el-table-column>
				<el-table-column label="操作" width="280" align="center">
					<template #default="scope">
						<!-- <el-button type="warning" size="small" :icon="View" @click="handleView(scope.row)">
							查看
						</el-button> -->
						<el-button
							type="primary"
							size="small"
							:icon="Edit"
							@click="handleEdit(scope.$index, scope.row)"
						>
							编辑
						</el-button>
						<el-button
							type="danger"
							size="small"
							:icon="Delete"
							@click="handleDelete(scope.$index)"
						>
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination
					background
					layout="total, prev, pager, next"
					:current-page="query.pageIndex"
					:page-size="query.pageSize"
					:total="pageTotal"
					@current-change="handlePageChange"
				></el-pagination>
			</div>
		</div>
		<el-dialog
			:title="idEdit ? '编辑用户' : '新增用户'"
			v-model="visible"
			width="500px"
			destroy-on-close
			:close-on-click-modal="false"
			@close="closeDialog"
		>
			<PermissionEdit :data="rowData" :edit="idEdit" :update="updateData" @refresh-perms="handleSaveSuccess"/>
		</el-dialog>
		<!-- <el-dialog title="查看用户详情" v-model="visible1" width="700px" destroy-on-close>
			<TableDetail :data="rowData" />
		</el-dialog> -->
	</div>
</template>

<script setup lang="ts" name="basetable">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Edit, Search, CirclePlusFilled, View } from '@element-plus/icons-vue';
import PermissionEdit from '../components/permission-edit.vue';
import request from '../utils/request';

interface TableItem {
	id: number;
	name: string;
	thumb: string;
	money: number;
	state: string;
	date: string;
	address: string;
}

const query = reactive({
	address: '',
	name: '',
	pageIndex: 1,
	pageSize: 100
});
const tableData = ref<TableItem[]>([]);
const pageTotal = ref(0);

const fetchData = () => {
    return request({
        url: '/admin/permissions',
        method: 'get'
    });
};

const handleSaveSuccess = ()=> {  
    console.log("handleSaveSuccess")
    getData()
}  

// 获取表格数据
const getData = async () => {
	const res = await fetchData();
    console.log("res:"+JSON.stringify(res));
	tableData.value = res.data.sysPermissionList;
    console.log("size:"+res.data.sysPermissionList.length);
	pageTotal.value = res.data.sysPermissionList.length;
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
const handleDelete = (index : number) => {
	// 二次确认删除
	ElMessageBox.confirm('确定要删除吗？', '提示', {
		type: 'warning'
	})
		.then(() => {
            const id  = tableData.value[index].id
            console.log("deleteFunc id->"+id);
            const deleteFunc= async (id:number) => {
                const res = await deletePerm(id);
                ElMessage.success('删除成功');
                getData();
            }
            deleteFunc(id);
        
			// tableData.value.splice(index, 1);
		})
		.catch(() => {});
};


const deletePerm = (id: number) => {
	return request({
		url: '/admin/deletePermission/'+id,
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
