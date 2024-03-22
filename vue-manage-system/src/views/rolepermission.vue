<template>
    <div class="container">
        <!-- <div class="plugins-tips">通过 v-permiss 自定义指令实现权限管理，使用非 admin 账号登录，可查看效果。</div> -->
        <div class="mgb20">
            <span class="label">角色：</span>
            <el-select v-model="role" @change="handleChange">
                <!-- <el-option label="超级管理员" value="admin"></el-option> -->
                <!-- <el-option label="普通用户" value="user"></el-option>  -->

                <el-option v-for="roleOption in roles" :key="roleOption.id" :label="roleOption.description"
                    :value="roleOption.id"></el-option>

            </el-select>

        </div>
        <div class="mgb20 tree-wrapper">
            <el-tree ref="tree" :data="dataref" node-key="id" default-expand-all show-checkbox
                :default-checked-keys="checkedKeys" :check-strictly="true"/>
        </div>
        <el-button type="primary" @click="onSubmit">保存权限</el-button>
    </div>
</template>

<script setup lang="ts" name="permission">
import { getCurrentInstance, onMounted, ref } from 'vue';
import { ElMessage, ElTree } from 'element-plus';
import request from '../utils/request';
import service from '../utils/request';
import { events } from '../store/bus';

const role = ref<string>();

const roles = ref<any[]>([]);

onMounted(() => {
    getData();
})

const refreshEvent= () => {
    // eventBus.$emit('refresh', 'refresh slidebar')
    events.emit('refresh','refresh slidebar')
}

const fetchData = () => {
    return request({
        url: '/admin/roles',
        method: 'get'
    });
};


// 获取表格数据
const getData = async () => {
    const res = await fetchData();
    roles.value = res.data.sysRoleList
    role.value = res.data.sysRoleList[0].description

    //查询角色已有的权限
    loadRolePermission(roles.value[0].id);

};


const dataref = ref<any[]>()


const loadAllPermission = () => {
    // 返回所有权限
    service.get('/admin/permissions')
        .then(response => {
            console.log(response.data);
            var permission = response.data.sysPermissionList;
            const data = [];

            permission.forEach(item => {
                const convertedItem = {
                    id: item.id == null ? -1 : item.id,
                    label: item.name == null ? "" : item.name,
                    permiss: item.code,
                    children: []
                };

                if (item.parent === 0) {
                    data.push(convertedItem);
                } else {
                    const parentItem = data.find(parent => parent.permiss === item.parent);
                    if (parentItem) {
                        if (!parentItem.children) {
                            parentItem.children = [];
                        }
                        parentItem.children.push(convertedItem);
                    }
                }
            });
            dataref.value = data
        })
        .catch(error => {
            console.error(error);

        });

};
loadAllPermission();


const loadRolePermission = (role_id: number) => {
    currentRoleId = role_id;
    // 返回所有权限
    service.get('/admin/rolePermissions/' + role_id)
        .then(response => {
            console.log(response.data);
            var permission = response.data.sysPermissionList;
            const data = [];
            if (permission == null || permission.length == 0) {
                checkedKeys.value = []
                tree.value!.setCheckedKeys(checkedKeys.value);
                return;
            }
            permission.forEach(item => {
                data.push(item.id);
            });
            console.log("checkedKeys.value->" + JSON.stringify(data))
            checkedKeys.value = data
            tree.value!.setCheckedKeys(checkedKeys.value);
        })
        .catch(error => {
            console.error(error);
        });

};


// 获取当前权限
const checkedKeys = ref<number[]>([]);

// 保存权限
const tree = ref<InstanceType<typeof ElTree>>();
const onSubmit = () => {
    // 获取选中的权限
    console.log(tree.value!.getCheckedKeys(false));

    service.post('/admin/updateRolePerm',{
        roleId : currentRoleId,
        perms:tree.value!.getCheckedKeys(false)
    })
        .then(response => {
            console.log(response.data);
            loadRolePermission(currentRoleId);
            ElMessage.success('保存成功');
            //通知其他组件刷新
            refreshEvent();
            
        })
        .catch(error => {
            console.error(error);
        });

};
let currentRoleId;
const handleChange = (val: string[]) => {
    console.log("handleChange role change:" + JSON.stringify(val))
    loadRolePermission(Number(val));
};
</script>

<style scoped>
.tree-wrapper {
    max-width: 500px;
}

.label {
    font-size: 14px;
}
</style>
