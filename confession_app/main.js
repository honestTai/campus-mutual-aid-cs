import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import uniLoadMore from '@/uni_modules/uni-load-more/components/uni-load-more/uni-load-more.vue';
Vue.component('uniLoadMore',uniLoadMore)
import uniFab from '@/uni_modules/uni-fab/components/uni-fab/uni-fab.vue';
Vue.component('uni-fab',uniFab)
import uniIcons from '@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue';
Vue.component('uni-icons',uniIcons)
import fabCom from '@/pages/fab/fab.vue';
Vue.component('fab-com',fabCom)
import datePicker from '@/uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.vue';
Vue.component('uni-datetime-picker',datePicker)
import apiRequest from 'common/apiRequest.js'
Vue.prototype.$apiRequest = apiRequest 
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif