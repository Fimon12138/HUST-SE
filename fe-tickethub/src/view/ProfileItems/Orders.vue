<template>
  <div class="container-orders">
    <Order
      v-for="(order, index) in OrderList" :key="index"
      :order="order"
      :class="{ 'distanceTop': index > 0 }"
      @deleteOrder="getList"
    ></Order>
  </div>
</template>

<script>
import Order from '../../components/Order'

export default {
  data () {
    return {
      OrderList: []
    }
  },
  components: {
    Order
  },
  methods: {
    getList () {
      this.OrderList = []

      this.$http.post('/api/v1/order/list', {
        pageNo: 1,
        pageSize: 100,
        userId: window.sessionStorage.getItem('token')
      }).then(res => {
        if (res.status === 200 && res.data.totalCount > 0) {
          res.data.result.forEach(element => {
            const order = {
              name: element.ticketName,
              id: element.id,
              price: element.price,
              createTime: element.createTime,
              state: element.status
            }
            this.OrderList.push(order)
          })
        }
      }).catch(err => {
        console.log(err)
        alert('Error!')
      })
    }
  },
  created () {
    this.getList()
  }
}
</script>

<style lang="less" scoped>
.container-orders {
  margin-top: 40px;
}

.distanceTop {
  margin-top: 30px;
}
</style>
