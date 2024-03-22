import axios, { AxiosInstance, AxiosError, AxiosResponse, InternalAxiosRequestConfig, formToJSON } from 'axios';
import router from '../router';
import { da } from 'element-plus/es/locale';

const BASE_URL = 'http://localhost:8081'; // 替换为您的全局域名  


const service: AxiosInstance = axios.create({
    baseURL: BASE_URL,
    timeout: 5000
});
service.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const uid = localStorage.getItem('ms_uid');
        const username = localStorage.getItem('ms_username');
        const ticket = localStorage.getItem('ms_ticket');
        console.log("uid local:"+uid);
        if (uid) {
          config.headers['uid'] = uid;
        }
        return config;
    },
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    (response: AxiosResponse) => {
        if (response.status === 200) {
            console.log(response)
            var data = response.data;
            if(data != null && data.ret == -1){
                if(data.err == 10000005){
                    console.log("login expiry,go to login page");
                    router.push('/login');
                }else if(data.err == 10000000){
                    console.log("not login,go to login page");
                    router.push('/login');
                }
                
              
            }
            return response;
        } else {
            if(response.status === 403){
                console.log("403");
                router.push('/403');
            }
            Promise.reject();
        }
    },
    (error: AxiosError) => {
        console.log(error);
        if(error.code === "ERR_BAD_REQUEST"){
            router.push('/403');
        }
        return Promise.reject();
    }
);

export default service;
