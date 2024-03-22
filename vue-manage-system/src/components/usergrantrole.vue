<template>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户" prop="username">
            <el-input v-model="form.username" :readonly="true"></el-input>
        </el-form-item>

        <el-form-item label="角色" prop="rolename">
            <!-- <el-input v-model="form.rolename"></el-input> -->
            <!--如果当前登录的是系统管理员 admin用户 不允许修改角色-->
            <template v-if="isAdmin">
                <el-input v-model="role" :readonly="true"></el-input>
            </template>
            <template v-else>
                <el-select v-model="role" @change="handleChange">
                    <el-option v-for="roleOption in roles" :key="roleOption.id" :label="roleOption.name+'('+roleOption.description+')'"
                        :value="roleOption.id"></el-option>
                </el-select>
            </template>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" :disabled="isAdmin" @click="saveEdit(formRef)">保 存</el-button>
        </el-form-item>
    </el-form>
</template>

<script lang="ts" setup>
import { ElMessage, FormInstance, FormRules, UploadProps } from 'element-plus';
import { onMounted, ref, watch } from 'vue';
import request from '../utils/request';
import { usePermissStore } from '../store/permiss';
import { events } from '../store/bus';
const permissStore = usePermissStore();
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

let selectRoleId;

const handleChange = (val: string[]) => {
    console.log("handleChange role change:" + val)
    selectRoleId = val
    console.log("props.data.id->" + props.data.id);
};

const defaultData = {
    id: '',
    username: '',
    rolename: '',
    roledescription: '',
};

const form = ref({ ...(props.edit ? props.data : defaultData) });
console.log("username:" + props.data.username);
const isAdmin = ref(false)
isAdmin.value = props.data.username === 'admin'

const rules: FormRules = {
    // username: [{ required: true, message: '权限名称', trigger: 'blur' }],
    // rolename: [{ required: true, message: '权限code', trigger: 'blur' }],
    // description: [{ required: true, message: '权限描述', trigger: 'blur' }]
};

const formRef = ref<FormInstance>();
const saveEdit = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.validate(valid => {
        if (!valid) return false;
        props.update(form.value);
        ElMessage.success('保存成功！');
        if (props.edit) {
            const update = async () => {
                const res = await updateUserRole();
                //通知页面刷新
                refreshEvent();
            }
            update();
        }

    });
};

const updateUserRole = () => {
    return request({
        url: '/admin/updateUserRole',
        method: 'post',
        data: {
            uid: props.data.id,
            roleId: selectRoleId
        }
    });
}

/**
 * 查询指定用户现有的角色
 */
const getUserRole = () => {
    return request({
        url: '/admin/getUserRole',
        method: 'post',
        data: {
            uid: props.data.id
        }
    });
}

const role = ref<any>();

const roles = ref<any[]>([]);

onMounted(() => {
    getData();
    const permissStore = usePermissStore();
    const role_local = permissStore.getPermission;
    role.value = role_local
    console.log("user role dialog->" + role_local + "  selectRoleId:" + selectRoleId);

    const getSelectUserRole = async () => {
        const res = await getUserRole();
        console.log("getSelectUserRole:" + JSON.stringify(res));
        selectRoleId = res.data.sysRole.id;
        console.log("getSelectUserRole default role id:" + selectRoleId);
        role.value = res.data.sysRole.name;
    }
    getSelectUserRole();

})

const refreshEvent = () => {
    events.emit('refresh', 'refresh slidebar after role grant to user')
}

// 获取全部角色
const getData = async () => {
    const res = await request({
        url: '/admin/roles',
        method: 'get'
    });
    roles.value = res.data.sysRoleList
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
