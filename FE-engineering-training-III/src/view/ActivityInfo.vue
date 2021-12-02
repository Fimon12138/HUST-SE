<template>
  <div class="container-activity-info">
    <div class="left">
      <div class="top">
        <img :src="logo" alt="error">
        <div class="info">
          <span class="title">{{ title }}</span>
          <span class="other-info"
            style="margin-top: 30px"
          >Time: {{ time }}</span>
          <span class="other-info">Location: {{ location }}</span>
          <span class="other-info">Price: {{ price }} USD</span>
          <div class="quantity">
            <span style="padding-top: 8px">Quantity:</span>
            <div class="quantity-selector">
              <div :class="{'quantity-button': true, 'disable-button': disableRemove }" @click="removeTicket">
                <el-image class="quantity-img"
                  :src="remove"
                  fit="fit"
                ></el-image>
              </div>
              <el-input v-model="quantity"
                @input="modifyQuantity"
              ></el-input>
              <div :class="{'quantity-button': true, 'disable-button': disableAdd }" @click="addTicket">
                <el-image class="quantity-img"
                  :src="add"
                  fit="fit"
                ></el-image>
              </div>
            </div>
            <span class="left">
              <i class="el-icon-info"></i>
              {{ left }} tickets left.
            </span>
          </div>
          <span class="other-info">Total: {{ totalPrice }} USD</span>
          <el-button class="buy-button" type="danger" @click="buy">Buy Now</el-button>
        </div>
      </div>
      <div class="bottom">
        <el-tabs v-model="activeName">
          <el-tab-pane label="Details" name="details">
            <img :src="details" alt="error" style="width: 100%">
          </el-tab-pane>
          <el-tab-pane label="Terms" name="terms">
            <p>{{ terms }}</p>
          </el-tab-pane>
          <el-tab-pane label="Remarks" name="remarks">
            <div>
              <RemarkEditor
                :ticketId="activityId"
                @newRemark="getNewComment"
              ></RemarkEditor>
              <div class="remarks">
                <RemarkActivity
                  v-for="(remark, index) in commentList" :key="index"
                  :comment="remark"
                  :index="index"
                  class="remark"
                ></RemarkActivity>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <div class="right">
      <TicketTerms></TicketTerms>
    </div>
  </div>
</template>

<script>
import add from '../assets/svg/add.svg'
import remove from '../assets/svg/remove.svg'

import TicketTerms from '../components/TicketTerms'
import RemarkEditor from '../components/RemarkEditor'
import RemarkActivity from '../components/RemarkActivity'

export default {
  data () {
    return {
      add,
      remove,

      activityId: '',
      logo: '',
      title: '',
      time: '',
      location: '',
      price: 0,
      quantity: 1,
      quantity_temp: 1, // 为了防止输入非法字符，缓存上一次的正确输入
      left: 0,
      commentList: [],

      totalPrice: 0,
      disableRemove: true,
      disableAdd: true,
      activeName: 'details',
      details: '',
      terms: '  For abnormal ordering behavior, TicketHub.com reserves the right to cancel the corresponding order after the order is established or becomes effective.' +
        'Abnormal ordering behaviors include but are not limited to the following situations:' +
        '(1) Orders that exceed the limited number of orders through the same ID.' +
        '(2) The behavior of placing orders that are deemed to be non-real consumers after reasonable judgment, including but not limited to passing the same batch or fictitious payment account, receiving address (including the filling in when placing the order and the final actual receiving address), recipient, Phone number ordering orders exceeding the limit'
    }
  },
  components: {
    TicketTerms,
    RemarkEditor,
    RemarkActivity
  },
  methods: {
    removeTicket () {
      if (this.quantity > 1) {
        this.quantity -= 1
        if (this.quantity === 1) {
          this.disableRemove = true
        }
        if (this.quantity < this.left) {
          this.disableAdd = false
        }
        this.computeTotalPrice()
        this.quantity_temp = this.quantity // 同步一次合法输入
      }
    },
    addTicket () {
      if (this.quantity < this.left) {
        this.quantity += 1
        if (this.quantity === this.left) {
          this.disableAdd = true
        }
        if (this.quantity > 1) {
          this.disableRemove = false
        }
        this.computeTotalPrice()
        this.quantity_temp = this.quantity // 同步一次合法输入
      }
    },
    modifyQuantity (value) {
      const numReg = /^[1-9][0-9]*$/
      if (!numReg.test(value) || value > this.left) {
        this.quantity = this.quantity_temp
      } else {
        this.quantity = Number(this.quantity)
        this.quantity_temp = this.quantity
      }
      this.computeTotalPrice()

      this.disableAdd = false
      this.disableRemove = false
      if (this.quantity === 1) {
        this.disableRemove = true
      }
      if (this.quantity === this.left) {
        this.disableAdd = true
      }
    },
    computeTotalPrice () {
      this.totalPrice = this.quantity * this.price
      this.totalPrice = this.totalPrice.toFixed(2)
    },
    buy () {
      const requestUrl = '/confirmOrder?' +
        'ActivityId=' + this.activityId +
        '&Quantity=' + this.quantity
      this.$router.push(requestUrl)
    },
    getNewComment (comment) {
      this.commentList.unshift(comment)
    }
  },
  created () {
    this.activityId = this.$route.query.id

    console.log(this.activityId)

    this.$http.post('/api/v1/ticket/info', {
      id: this.activityId
    }).then(res => {
      // console.log(res)

      if (res.status === 200 && res.data) {
        const data = res.data
        this.title = data.name
        this.time = data.startTime
        this.location = data.location
        this.price = data.price
        this.left = data.count

        this.disableAdd = this.quantity === this.left
        this.computeTotalPrice()

        this.details = data.imageDetail
        this.logo = data.imageColumn
        // this.terms = data.
      } else {
        alert('Get Activity Info Fail!')
      }
    }).catch(err => {
      console.log(err)
      alert('Get Activity Info Fail!')
    })

    this.$http.post('/api/v1/comment/list', {
      pageNo: 1,
      pageSize: 100,
      ticketId: this.activityId
    }).then(res => {
      if (res.status === 200 && res.data.totalCount > 0) {
        const comments = res.data.result
        comments.forEach(element => {
          const comment = {}
          comment.commentText = element.content
          // this.commentList.push(comment)

          this.$http.post('/api/v1/user/info', {
            id: element.userId
          }).then(res => {
            if (res.status !== 200) {
              return
            }
            comment.nickname = res.data.nickname
            comment.avatar = res.data.avatar

            this.commentList.push(comment)
          }).catch(err => {
            console.log(err)
          })
        })
      }
    }).catch(err => {
      console.log(err)
    })
  }
}
</script>

<style lang="less" scoped>
.container-activity-info {
  width: 80%;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 50px;

  display: flex;
  .left {
    width: 75%;
  }
  .right {
    width: 25%;
    padding-left: 20px;
  }
}

.top {
  display: flex;
  img {
    width: 270px;
    height: 360px;
  }
  .info {
    display: flex;
    flex-direction: column;
    padding: 10px 20px;

    .title {
      font-family: 'Lineto-Brown-Bold';
      font-size: 25px;
      line-height: 26px;
    }
    .other-info {
      font-family: 'BalooTammudu-SemiBold';
      font-size: 18px;
      padding: 5px 0;
    }
  }
}

.quantity {
  display: flex;
  font-family: 'BalooTammudu-SemiBold';
  font-size: 18px;
  margin-top: 20px;
  margin-bottom: 30px;

  .left {
    color: grey;
    font-size: 15px;
    margin-left: 20px;
    padding-top: 8px;
  }
}
.quantity-selector {
  display: flex;
  margin-left: 20px;
  cursor: pointer;
}
.quantity-button {
  width: 21px;
  height: 28px;
  border-style: solid;
  border-width: 1px;
  border-color: #e5e5e5;
  border-radius: 3px;

  display: flex;
  align-items: center;
  justify-content: center;

  .quantity-img {
    width: 15px;
    height: 15px;
  }
}
.quantity-button:hover {
  background-color: #e5e5e5;
}
.disable-button {
  background-color: #e5e5e5;
}

.el-input {
  margin-left: 15px;
  margin-right: 15px;
  width: 40px;
  height: 28px;

  /deep/ .el-input__inner {
    height: 100%;
    line-height: 28px;
    padding: 0 5px;
    text-align: center;
  }
}
.buy-button {
  width: 250px;
  margin-top: 30px;
}

.bottom {
  margin-top: 20px;
  font-family: 'Lineto-Brown-Bold';
  font-size: 15px;

  .remarks {
    margin-top: 30px;

    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .remark {
    margin-top: 20px;
  }
}

</style>
