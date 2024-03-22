<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="sidebar.collapse"
            background-color="#324157" text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-sub-menu :index="item.index" :key="item.index">
                        <template #title>
                            <el-icon>
                                <component :is="item.icon"></component>
                            </el-icon>
                            <span>{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-sub-menu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template #title>{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-sub-menu>
                            <el-menu-item v-else :index="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-sub-menu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <el-icon>
                            <component :is="item.icon"></component>
                        </el-icon>
                        <template #title>{{ item.title }}</template>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onBeforeUnmount } from 'vue';
import { useSidebarStore } from '../store/sidebar';
import { useRoute } from 'vue-router';
import { onMounted, ref } from 'vue';
import service from '../utils/request';
import { da } from 'element-plus/es/locale';
import { events } from '../store/bus';
import { usePermissStore } from '../store/permiss';

// const items = [
// {
//     icon: 'Odometer',
//     index: '/dashboard',
//     title: '系统首页',
//     permiss: '1',
//     subs: []
// },
// {
//     icon: 'Calendar',
//     index: '1',
//     title: '表格相关',
//     permiss: '2',
//     subs: [
//         {
//             index: '/table',
//             title: '常用表格',
//             permiss: '2',
//         },
//         {
//             index: '/import',
//             title: '导入Excel',
//             permiss: '2',
//         },
//         {
//             index: '/export',
//             title: '导出Excel',
//             permiss: '2',
//         },
//     ],
// },
// {
//     icon: 'DocumentCopy',
//     index: '/tabs',
//     title: 'tab选项卡',
//     permiss: '3',
// },
// {
//     icon: 'Edit',
//     index: '3',
//     title: '表单相关',
//     permiss: '4',
//     subs: [
//         {
//             index: '/form',
//             title: '基本表单',
//             permiss: '5',
//         },
//         {
//             index: '/upload',
//             title: '文件上传',
//             permiss: '6',
//         },
//         {
//             index: '4',
//             title: '三级菜单',
//             permiss: '7',
//             subs: [
//                 {
//                     index: '/editor',
//                     title: '富文本编辑器',
//                     permiss: '8',
//                 },
//                 {
//                     index: '/markdown',
//                     title: 'markdown编辑器',
//                     permiss: '9',
//                 },
//             ],
//         },
//     ],
// },
// {
//     icon: 'Setting',
//     index: '/icon',
//     title: '自定义图标',
//     permiss: '10',
// },
// {
//     icon: 'PieChart',
//     index: '/charts',
//     title: 'schart图表',
//     permiss: '11',
// },
// {
//     icon: 'Setting',
//     index: '/table',
//     title: '用户管理',
//     permiss: '15',
// },
// {
//     icon: 'Warning',
//     index: '/permission',
//     title: '权限管理',
//     permiss: '13',
// },
// {
//     icon: 'CoffeeCup',
//     index: '/donate',
//     title: '支持作者',
//     permiss: '14',
// },

// {
//     "icon": "Odometer",
//     "index": "/dashboard",
//     "title": "系统首页",
//     "permiss": "1",
//     "subs": [

//     ]
// },
// {
//     "icon": "Setting",
//     "index": "1",
//     "title": "系统管理",
//     "permiss": "10",
//     "subs": [
//         {
//             "icon": "Setting",
//             "index": "/user",
//             "title": "用户管理",
//             "permiss": "11"
//         },
//         {
//             "icon": "Setting",
//             "index": "/role",
//             "title": "角色管理",
//             "permiss": "12"
//         },
//         {
//             "icon": "Setting",
//             "index": "/permission",
//             "title": "权限管理",
//             "permiss": "13"
//         }
//     ]
// },
// {
//     "icon": "Setting",
//     "index": "2",
//     "title": "内容审核",
//     "permiss": "20",
//     "subs": [
//         {
//             "icon": "Setting",
//             "index": "/image_review",
//             "title": "图片审核",
//             "permiss": "21"
//         },
//         {
//             "icon": "Setting",
//             "index": "/comment_review",
//             "title": "评论审核",
//             "permiss": "22"
//         }
//     ]
// },
// {
//     "icon": "Setting",
//     "index": "3",
//     "title": "配置管理",
//     "permiss": "30",
//     "subs": [
//         {
//             "icon": "Setting",
//             "index": "/city",
//             "title": "城市管理",
//             "permiss": "31"
//         },
//         {
//             "icon": "Setting",
//             "index": "/label",
//             "title": "标签管理",
//             "permiss": "32"
//         },
//         {
//             "icon": "Setting",
//             "index": "/topic",
//             "title": "话题管理",
//             "permiss": "33"
//         }
//     ]
// }
// ];

const items = ref<any[]>([]);

onBeforeUnmount(() => {
    events.off("refresh")
})

//请求菜单和权限列表
onMounted(() => {
    // 页面加载时执行的回调函数
    console.log('Menu loaded');
    loadMenu();
    events.on('refresh', (message) => {
        console.log("recv refresh event")
        loadMenu()
    })
});

const permissStore = usePermissStore();

const loadMenu = () => {
    var uid = localStorage.getItem('ms_uid');
    service.get('/admin/permissions/' + uid)
        .then(response => {
            console.log(response.data);
            var permission = response.data.userPermissionResult.sysRole.permissions;
            permissStore.setPermission(response.data.userPermissionResult.sysRole.name);
            const data = [];
            data.push({
                icon: 'Odometer',
                index: '/dashboard',
                title: '系统首页',
                permiss: '1',
                subs: []
            })

            var index = 1;

            permission.forEach(item => {
                const convertedItem = {
                    icon: 'Setting',
                    index: item.url == null ? "" : item.url,
                    title: item.name == null ? "" : item.name,
                    permiss: item.code.toString(),
                };

                if (item.parent === 0) {
                    //level == 2 parent == 0 的认为是一级菜单 并且可以直接打开页面
                    if(item.level === 1){
                        convertedItem.index = index + "";
                    }
                    data.push(convertedItem);
                    console.log("1111:" + item['name'] + " index:" + index)
                    index++;
                } else {
                    const parentItem = data.find(parent => parent.permiss === item.parent.toString());
                    console.log("findparent->" + parentItem.title + " " + parentItem.permiss)
                    if (parentItem) {
                        if (!parentItem.subs) {
                            parentItem.subs = [];
                        }
                        parentItem.subs.push(convertedItem);
                    }
                }
            });
            items.value = data
            console.log("data->" + JSON.stringify(data))
        })
        .catch(error => {
            console.error(error);

        });

}


const route = useRoute();
const onRoutes = computed(() => {
    return route.path;
});

const sidebar = useSidebarStore();

</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
    width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
    width: 250px;
}

.sidebar>ul {
    height: 100%;
}
</style>
