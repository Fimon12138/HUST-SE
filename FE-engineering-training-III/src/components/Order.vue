<template>
  <div class="container-order">
    <span>Order ID:<span class="text-margin-left">{{ order.id }}</span></span>

    <el-divider></el-divider>

    <div class="order-info">
      <span>Name:<span class="text-margin-left">{{ order.name }}</span></span>
      <span>CreateTime:<span class="text-margin-left">{{ order.createTime }}</span></span>
      <span>Total:<span class="text-margin-left text-margin-right">{{ order.price }}</span>USD</span>
    </div>

    <el-divider></el-divider>

    <div>
      <el-button type="danger" @click="deleteOrder">Delete</el-button>
    </div>

    <!-- 右上角状态 -->
    <div class="order-triangle"></div>
    <span class="state">{{ order.state }}</span>
  </div>
</template>

<script>
export default {
  props: {
    order: {
      type: Object,
      required: true
    }
  },
  methods: {
    deleteOrder () {
      this.$http.post('/api/v1/order/delete', {
        id: this.order.id
      }).then(res => {
        if (res.status === 200) {
          this.$emit('deleteOrder')
        }
      }).catch(err => {
        console.log(err)
        alert('Error!')
      })
    }
  }
}
</script>

<style lang="less" scoped>
.container-order {
  padding: 10px 20px;
  width: 90%;

  border-radius: 7px;
  box-shadow: 0 0 7px #dddddd;

  font-family: 'Lineto-Brown-Bold';

  position: relative;
}

.order-info {
  display: flex;
  flex-direction: column;
}

.state {
  color: white;
  position: absolute;
  top: 15px;
  right: 5px;

  transform: rotate(23deg);
  -webkit-transform: rotate(23deg);

  z-index: 1;
}

.text-margin-left {
  margin-left: 10px;
}

.text-margin-right {
  margin-right: 10px;
}

.el-button {
  float: right;
  padding: 5px 20px;
}

.el-divider {
  margin-top: 10px;
  margin-bottom: 10px;
}

.order-triangle {
  position: absolute;
  top: 0;
  right: 0;

  width: 0;
  height: 0;

  border-width: 30px 70px 30px 70px;
  border-color: red red transparent transparent;
  border-style: solid;
  border-top-right-radius: 7px;
}
</style>
