import { ro } from 'element-plus/es/locale';
import { defineStore } from 'pinia';

interface ObjectList {
  [key: string]: string[];
}

export const usePermissStore = defineStore('permiss', {
  state: () => {
    const keys = localStorage.getItem('ms_keys');
	const roles = localStorage.getItem('ms_role_keys');
    return {
      key: keys ? JSON.parse(keys) : <string[]>[],
      defaultList: <ObjectList>{
		//默认都拥有首页
        // admin: ['1'],
        // user: ['1']
      },
	  role_val:roles ? roles : []
    };
  },
  actions: {
    handleSet(val: string[]) {
      this.key = val;
    },
    setPermission(role: string) {
		localStorage.setItem("ms_role_keys",role);
    }
  },
  getters: {
    getPermission(): string[] {
      return this.role_val;
    }
  }
});