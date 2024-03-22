<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="username">
                        <template #prepend>
                            <el-button :icon="User"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="param.password"
                        @keyup.enter="submitForm(login)">
                        <template #prepend>
                            <el-button :icon="Lock"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm(login)">登录</el-button>
                </div>
                <el-checkbox class="login-tips" v-model="checked" label="记住密码" size="large" />
                <p class="login-tips">Tips : 密码123456</p>
                <p class="login-tips">admin   所有权限 </p>
                <p class="login-tips">admin1  部分权限 </p>
                <p class="login-tips">admin2  部分权限 </p>
                <p class="login-tips">admin3  无权限展示403页面 </p>
            </el-form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useTagsStore } from '../store/tags';
import { usePermissStore } from '../store/permiss';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { Lock, User } from '@element-plus/icons-vue';
import axios from 'axios';
import service from '../utils/request';
import { dataType } from 'element-plus/es/components/table-v2/src/common';
import { da } from 'element-plus/es/locale';

interface LoginInfo {
    username: string;
    password: string;
}

const lgStr = localStorage.getItem('login-param');
const defParam = lgStr ? JSON.parse(lgStr) : null;
const checked = ref(lgStr ? true : false);

const router = useRouter();
const param = reactive<LoginInfo>({
    username: defParam ? defParam.username : '',
    password: defParam ? defParam.password : '',
});

const rules: FormRules = {
    username: [
        {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
        },
    ],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};
const permiss = usePermissStore();
const login = ref<FormInstance>();
const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.validate((valid: boolean) => {
        if (valid) {
            const loginData = {
                username: param.username,
                password: param.password
            };

            service.post('/admin/login', loginData)
                .then(response => {
                    console.log(response.data);
                    var baseResponse = response.data;
                    if(baseResponse.ret != 0){
                        console.error(baseResponse.err+" "+baseResponse.msg);
                        ElMessage.error('登录失败');
                        return;
                    }
                    var login = baseResponse.data;
                    console.log("login result:"+JSON.stringify(login));
                    ElMessage.success('登录成功');
                    localStorage.setItem('ms_uid', login.uid);
                    localStorage.setItem('ms_username', param.username);
                    localStorage.setItem('ms_ticket',login.ticket);
                    localStorage.setItem('role_id',login.sysRole.id);
                    // console.log("login.ticket:"+JSON.stringify(login.ticket));
                    // //todo 这里用于存储登录成功用户的权限

                    // const keys = permiss.defaultList[param.username == 'admin' ? 'admin' : 'user'];
                    // permiss.handleSet(keys);
                    // localStorage.setItem('ms_keys', JSON.stringify(keys));
                    permiss.setPermission(login.sysRole.name);
                    router.push('/');
                    if (checked.value) {
                        localStorage.setItem('login-param', JSON.stringify(param));
                    } else {
                        localStorage.removeItem('login-param');
                    }
                })
                .catch(error => {
                    console.error(error);
                    ElMessage.error('登录失败');
                });

        } else {
            ElMessage.error('登录失败');
            return false;
        }
    });
};

const tags = useTagsStore();
tags.clearTags();
</script>

<style scoped>
.login-wrap {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background-image: url(../assets/img/login-bg.jpg);
    background-size: 100%;
}

.ms-title {
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #333;
    font-weight: bold;
    padding-top: 10px;
}

.ms-login {
    width: 350px;
    border-radius: 5px;
    background: #fff;
}

.ms-content {
    padding: 10px 30px 30px;
}

.login-btn {
    text-align: center;
}

.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}

.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #333;
}
</style>
