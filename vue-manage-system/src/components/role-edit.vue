<template>
	<el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
		<el-form-item label="角色名" prop="name">
			<el-input v-model="form.name"></el-input>
		</el-form-item>
		<el-form-item label="角色描述" prop="name">
			<el-input v-model="form.description"></el-input>
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
	description: ''
};

let instance;
onMounted(() => {
	instance = getCurrentInstance();
})

function refresh() {
	instance.emit('refresh');
}

const form = ref({ ...(props.edit ? props.data : defaultData) });

const rules: FormRules = {
	name: [{ required: true, message: '角色', trigger: 'blur' }],
	description: [{ required: true, message: '描述', trigger: 'blur' }]
};
const formRef = ref<FormInstance>();
const saveEdit = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.validate(valid => {
		if (!valid) return false;
		props.update(form.value);
		ElMessage.success('保存成功！');
		//
		if (!props.edit) {
			const add = async () => {
				const res = await addRole();
				console.log("add role:" + res);
				//通知页面刷新
				refresh();
			}
			add();
		} else {
			//update
			const update = async () => {
				const res = await updateRole();
				console.log("add role:" + res);
				//通知页面刷新
				refresh();
			}
			update();
		}

	});
};

const addRole = () => {
	return request({
		url: '/admin/addRole',
		method: 'post',
		data: form.value
	});
};

const updateRole = () => {
	return request({
		url: '/admin/updateRole',
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
