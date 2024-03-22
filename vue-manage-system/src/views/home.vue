<template>
	<v-header />
	<v-sidebar />
	<div class="content-box" :class="{ 'content-collapse': sidebar.collapse }">
		<v-tags></v-tags>
		<div class="content">
			<router-view v-slot="{ Component }">
				<transition name="move" mode="out-in">
					<keep-alive :include="tags.nameList">
						<component :is="Component"></component>
					</keep-alive>
				</transition>
			</router-view>
		</div>
	</div>
</template>
<script setup lang="ts">
import { useSidebarStore } from '../store/sidebar';
import { useTagsStore } from '../store/tags';
import vHeader from '../components/header.vue';
import vSidebar from '../components/sidebar.vue';
import vTags from '../components/tags.vue';
import { onMounted } from 'vue';
import service from '../utils/request';
const sidebar = useSidebarStore();
const tags = useTagsStore();

onMounted(() => {
  // 页面加载时执行的回调函数
  console.log('Page loaded');
  test();
});

const test = ()=>  {
	service.get('/admin/test_login')
                .then(response => {
                    console.log(response.data);
                })
                .catch(error => {
                    console.error(error);
                   
                });
}
</script>
