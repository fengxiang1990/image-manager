<template>
	<el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
		<el-form-item label="权限名" prop="name">
			<el-input v-model="form.name"></el-input>
		</el-form-item>
		<el-form-item label="权限描述" prop="description">
			<el-input v-model="form.description"></el-input>
		</el-form-item>
		<el-form-item label="权限code" prop="code">
			<el-input v-model="form.code"></el-input>
		</el-form-item>

		<el-form-item label="父权限" prop="parent">
			<el-input v-model="form.parent">0</el-input>
		</el-form-item>

		<el-form-item label="页面路径" prop="url">
			<el-input v-model="form.url"></el-input>
		</el-form-item>

		<el-form-item label="level" prop="level">
			<el-input v-model="form.level">1</el-input>
		</el-form-item>


		<el-form-item>
			<el-button type="primary" @click="saveEdit(formRef)">保 存</el-button>
		</el-form-item>
	</el-form>
</template>

<script lang="ts" setup>
import { ElMessage, FormInstance, FormRules, UploadProps } from 'element-plus';
import { onMounted, ref, getCurrentInstance } from 'vue';
import request from '../utils/request';

const props = defineProps({
	data: {
		type: Object,
		required: true
	},
	edit: {
		type: Boolean,
		required: false
	},
	update: {
		type: Function,
		required: true
	}
});

const defaultData = {
	id: '',
	name: '',
	description: '',
	code: -1,
	level: 1,
	parent: 0,
	url: ""
};

let instance;
onMounted(() => {
	instance = getCurrentInstance();
})

function refresh() {
	instance.emit('refresh-perms');
}

const form = ref({ ...(props.edit ? props.data : defaultData) });

const rules: FormRules = {
	name: [{ required: true, message: '权限名称', trigger: 'blur' }],
	code: [{ required: true, message: '权限code', trigger: 'blur' }],
	description: [{ required: true, message: '权限描述', trigger: 'blur' }]
};
const formRef = ref<FormInstance>();
const saveEdit = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.validate(valid => {
		if (!valid) return false;
		props.update(form.value);
		ElMessage.success('保存成功！');
		//
		if (props.edit) {
			const update = async () => {
				const res = await updatePerm();
				console.log("add role:" + res);
				//通知页面刷新
				refresh();
			}
			update();
		} else {
			const add = async () => {
				const res = await addPerm();
				console.log("add role:" + res);
				//通知页面刷新
				refresh();
			}
			add();
		}

	});
};

const addPerm = () => {
	return request({
		url: '/admin/addPermission',
		method: 'post',
		data: form.value
	});
};

const updatePerm = () => {
	return request({
		url: '/admin/updatePermission',
		method: 'post',
		data: form.value
	});
};


</script>

<style>
.avatar-uploader .el-upload {
	border: 1px dashed var(--el-border-color);
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
	transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
	border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
	font-size: 28px;
	color: #8c939d;
	width: 178px;
	height: 178px;
	text-align: center;
}
</style>
