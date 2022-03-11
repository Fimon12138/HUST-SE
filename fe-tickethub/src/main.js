import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import axios from 'axios'
import './plugins/vue-material.js'
import './assets/global.less'
import './assets/fonts/index.less'

Vue.config.productionTip = false

// TODO
Vue.prototype.$http = axios

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
