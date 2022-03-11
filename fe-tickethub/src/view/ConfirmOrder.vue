<template>
  <div class="container-confirm-order">
    <md-steppers :md-active-step.sync="activeStep" md-linear>
      <md-step id="first" md-label="Choose Products" :md-editable="false" @click="clickStepFirst"></md-step>
      <md-step id="second" md-label="Confirm the Order" :md-editable="false" @click="clickStepSecond">
        <div class="delivery">
          <span class="sub-title title">Delivery Method</span>
          <div class="code">
            <img :src="qrCode" alt="error">
          </div>
          <span class="e-ticket">e-Ticket</span>
        </div>

        <el-divider></el-divider>

        <div class="contact">
          <span class="sub-title">Contact Information</span>
          <el-form ref="contactForm" :rules="contactRules" :model="contactForm" label-width="60px">
            <el-form-item label="Name:" prop="name">
              <el-input v-model="contactForm.name"></el-input>
            </el-form-item>
            <el-form-item label="Email:" prop="email">
              <el-input v-model="contactForm.email"></el-input>
            </el-form-item>
          </el-form>
        </div>

        <el-divider></el-divider>

        <div class="pay">
          <span class="sub-title">Pay Method</span>
          <div class="selected">
            <div class="method">
              <img :src="ZJPay" alt="error">
              <span>ZJ Pay</span>
            </div>

            <!-- 右下角的打勾效果 -->
            <div class="tick"></div>
            <img :src="select" alt="error" class="tick-img">
          </div>
        </div>

        <el-divider></el-divider>

        <div class="order">
          <span class="sub-title">Confirm Order Details</span>
          <el-table
            :data="orderParams"
            style="width: 100%">
            <el-table-column
              label="Activity Info"
              width="350px">
              <template slot-scope="scope">
                <Ticket
                  :logo="scope.row.Logo"
                  :name="scope.row.Activity"
                  :location="scope.row.Location"
                  :date="scope.row.Time"
                ></Ticket>
              </template>
            </el-table-column>
            <el-table-column
              prop="Price"
              label="Price (USD)">
            </el-table-column>
            <el-table-column
              prop="Quantity"
              label="Quantity">
            </el-table-column>
            <el-table-column
              prop="Total"
              label="Total (USD)">
            </el-table-column>
          </el-table>
        </div>

        <el-divider></el-divider>

        <div class="button-area">
          <el-checkbox v-model="checked" @change="setWarningStatus">Agree to terms of TicketHub</el-checkbox>
          <span style="color: red; font-size: 10px" v-show="showWarning">You need to agree to our terms before submitting your order!</span>
          <el-button type="danger" @click="submit">Submit</el-button>
        </div>
      </md-step>
      <md-step id="third" md-label="Pay" :md-editable="false">
        <div class="step-third">
          <div v-if="playAnimation" ref="animationContainer" class="animation"></div>
          <img v-else :src="mall" alt="error" class="mall-img">
          <span>USD<span class="money">{{ orderParams[0].Total }}</span></span>
          <el-button type="danger" class="third-pay" @click="pay"
            :disabled="disablePay">Pay</el-button>
        </div>
      </md-step>
      <md-step id="fourth" md-label="Get Your Tickets" :md-editable="false" class="fourthStep">
        <div class="fourthStep">
          <span>You have booked the tickets successfully!</span>
          <div style="text-align: center">
            <el-button type="success" @click="tohome">Back To Home</el-button>
          </div>
        </div>
      </md-step>
    </md-steppers>
  </div>
</template>

<script>
import yes from '../assets/svg/check_circle_green.svg'
import qrCode from '../assets/svg/qr_code.svg'
import select from '../assets/svg/done-white.svg'
import mall from '../assets/svg/local_mall.svg'
import ZJPay from '../assets/img/zjpay.png'
import Ticket from '../components/Ticket'

import Lottie from 'lottie-web'

const jsonPath = 'https://assets2.lottiefiles.com/private_files/lf30_nrnx3s.json'

export default {
  data () {
    return {
      yes,
      ZJPay,
      qrCode,
      select,
      mall,

      activeStep: 'second',
      contactForm: {
        userName: '',
        email: ''
      },
      contactRules: {},

      orderParams: [],
      orderId: '',
      checked: true,
      showWarning: false,
      playAnimation: false,
      animation: undefined,
      disablePay: false
    }
  },
  components: {
    Ticket
  },
  methods: {
    submit () {
      if (!this.checked) {
        this.showWarning = true
        return
      }

      this.$refs.contactForm.validate(valid => {
        if (valid) {
          const params = {
            ticketId: this.orderParams[0].ActivityId,
            userId: window.sessionStorage.getItem('token'),
            count: Number(this.orderParams[0].Quantity),
            price: this.orderParams[0].Total
          }
          console.log(params)

          this.$http.post('/api/v1/order', params).then(res => {
            console.log(res)

            if (res.status === 200) {
              this.orderId = res.data.id
              this.activeStep = 'third'
            } else {
              alert('Error!')
            }
          }).catch(err => {
            console.log(err)
            alert('Error!')
          })
        }
      })
    },
    pay () {
      this.$http.post('/api/v1/user/info', {
        id: window.sessionStorage.getItem('token')
      }).then(res => {
        console.log(res)

        let payId
        if (res.status === 200 && res.data.payId) {
          payId = res.data.payId
        } else {
          alert('Error!')
          return
        }

        // 查余额
        this.$http.post('/api/v1/zjpay/info', {
          id: payId
        }).then(res => {
          console.log(res)

          if (res.status !== 200) {
            alert('Error!')
            return
          } else if (res.data.Money < this.orderParams[0].Total) {
            alert('Insufficient balance!')
            return
          }

          // 付款
          const params = {
            zjpayId: payId,
            orderId: this.orderId
          }
          console.log(params)

          this.$http.post('/api/v1/order/pay', params).then(res => {
            console.log(res)
            if (res.status !== 200) {
              return
            }

            this.disablePay = true
            this.playAnimation = true
            this.$nextTick(() => {
              this.animation = Lottie.loadAnimation({
                container: this.$refs.animationContainer,
                renderer: 'svg',
                loop: false,
                autoplay: true,
                path: jsonPath
              })
              this.animation.addEventListener('complete', () => {
                setTimeout(this.gotoStepFour(), 1000)
              })
            })
          })
        })
      }).catch(err => {
        console.log(err)
        alert('Error!')
      })
    },
    setWarningStatus () {
      if (this.checked) {
        this.showWarning = false
      }
    },
    gotoStepFour () {
      this.activeStep = 'fourth'
      this.animation.destroy()
    },
    clickStepFirst () {
      console.log('first')
    },
    clickStepSecond () {
      console.log('second')
    },
    clickStepThird () {
      console.log('third')
    },
    tohome () {
      this.$router.push('/home')
    }
  },
  created () {
    const params = this.$route.query
    console.log(params)

    this.$http.post('/api/v1/ticket/info', {
      id: params.ActivityId
    }).then(res => {
      console.log(res)

      const order = {
        Logo: res.data.imageColumn,
        Activity: res.data.name,
        Location: res.data.location,
        Time: res.data.startTime,
        Price: res.data.price,
        Quantity: params.Quantity,
        Total: res.data.price * params.Quantity,
        ActivityId: params.ActivityId
      }

      this.orderParams.push(order)
    }).catch(err => {
      console.log(err)
      alert('Error!')
    })
  }
}
</script>

<style lang="less" scoped>
.container-confirm-order {
  width: 75%;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);

  padding-top: 30px;
  padding-bottom: 50px;

  font-family: 'Lineto-Brown-Bold';

  background-color: white;
}

.md-steppers {
  /deep/ .md-steppers-navigation {
    box-shadow: none;
  }
}

.el-divider {
  margin-top: 20px;
  margin-bottom: 20px;
}

.delivery {
  display: flex;
  flex-direction: column;
  padding-left: 25px;

  .code {
    background-color: #ff1268;
    border-radius: 50%;
    width: 80px;
    height: 80px;

    img {
      width: 60px;
      height: 60px;

      margin-left: 10px;
      margin-top: 10px;
    }
  }
  .e-ticket {
    font-family: 'NotoSansSC-Regular';
    color: #ff1268;
    font-size: 13px;

    margin-top: 10px;
    margin-left: 15px;
  }
}

.contact {
  display: flex;
  flex-direction: column;
  padding-left: 25px;

  .el-form {
    .el-input {
      width: 200px;
    }
  }
}

.pay {
  display: flex;
  flex-direction: column;
  padding-left: 25px;

  .selected {
    border-style: solid;
    border-width: 2px;
    border-radius: 5px;
    border-color: #ff1369;

    padding: 20px 30px;
    width: 160px;

    overflow: hidden;
    position: relative;

    .method {
      display: flex;
      flex-direction: column;

      img {
        width: 100px;
      }

      span {
        color: grey;
        margin-top: 3px;
        margin-left: 25px;
      }
    }

    .tick {
      position: absolute;
      right: -20px;
      bottom: 0;

      width: 0;
      height: 0;

      border-style: solid;
      border-width: 20px;
      border-color: transparent transparent #ff1268 transparent;
    }

    .tick-img {
      position: absolute;
      right: 0;
      bottom: 0;

      width: 14px;
      height: 14px;

      z-index: 1;
    }
  }
}

.order {
  display: flex;
  flex-direction: column;
  padding-left: 25px;
}

.button-area {
  display: flex;
  flex-direction: column;
  padding-right: 20px;
  text-align: right;

  margin-top: 30px;

  .el-button {
    margin-top: 30px;
    width: 200px;
    margin-left: auto;
  }
}

.sub-title {
  font-size: 20px;
  margin-bottom: 20px;
}

.step-third {
  display: flex;
  flex-direction: column;
  align-items: center;

  .mall-img {
    border-radius: 50%;
    width: 60px;
    height: 60px;
    padding: 10px;
    background-color: #ee4d2d;
    margin-bottom: 20px;
  }

  .animation {
    width: 60px;
    height: 60px;
    margin-bottom: 20px;
  }

  .money {
    margin-left: 10px;
  }

  .third-pay {
    margin-top: 20px;
    width: 200px;
  }
}

.fourthStep {
  display: flex;
  flex-direction: column;

  span {
    text-align: center;

    font-family: 'Lineto-Brown-Bold';
    font-size: 25px;

    margin-top: 30px;
  }

  .el-button {
    width: 200px;
    margin-top: 30px;
  }
}
</style>
