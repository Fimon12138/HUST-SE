<template>
  <div class="container">
    <div class="sign-up-box" v-show="!isSuccess">
      <div>
        <img src="../assets/img/logo.png" alt="loading failed" class="logo-image">
      </div>
      <div class="note">
        Join now - it's free!
      </div>
      <el-form ref="formRef" :model="formData" :rules="rules">
        <el-form-item v-for="item in formSchema" :key="item.label"
          :prop="item.data.field">
          <FormItemExtend :useIcon="false"
            :label="item.label" :useOk="true"
            :renderData="{ schema: item.data, form: formData }"
            :note="item.note" :disableNote="item.disable"
            @input="modifyForm" :ref="item.data.field"
          ></FormItemExtend>
        </el-form-item>
      </el-form>
      <el-button type="success" :disabled="disable" @click="signup">Join</el-button>
      <div ref="loading" v-show="showLoading" class="loading"></div>
      <div class="login">
        Already have an account? <a href="/#/login">Log in</a>
      </div>
    </div>
    <div class="success-reminder" v-show="isSuccess">
      <div ref="successAnimation" class="success"></div>
      <span>You have successfully registered!</span>
      <div style="text-align: center">
        <el-button type="success" @click="jumpToLogin">Login Now !</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import Lottie from 'lottie-web'
import FormItemExtend from '../components/FormItemExtend'
import sha512 from 'js-sha512'

const loadingJSONPath = 'https://assets9.lottiefiles.com/packages/lf20_F7WfWB.json'
const successJSONPath = 'https://assets2.lottiefiles.com/private_files/lf30_nrnx3s.json'

export default {
  data () {
    return {
      isSuccess: false,
      showLoading: false,
      disable: false,
      animation: undefined,
      animationSuccess: undefined,
      formData: {
        phone: '',
        name: '',
        password: '',
        confirm: ''
      },
      formSchema: [
        {
          label: 'Telephone',
          disable: true,
          data: {
            field: 'phone',
            type: 'input',
            attrs: {
              placeholder: 'Telephone'
            }
          }
        },
        {
          label: 'User Name',
          note: '1. Only numbers and letters are allowed;' +
                '<br/>2. The length should be between 3 and 16;' +
                '<br/>3. The first character cannot be a number;',
          data: {
            field: 'name',
            type: 'input',
            attrs: {
              placeholder: 'Name'
            }
          }
        },
        {
          label: 'Password',
          note: '1. Only numbers and letters are allowed;' +
                '<br/>2. The length should be between 6 and 16;' +
                '<br/>3. At least two of lower case letters, upper case letters<br/>' +
                '&nbsp'.repeat(4) + 'and numbers should be included;',
          data: {
            field: 'password',
            type: 'input',
            attrs: {
              placeholder: 'Password',
              'show-password': true
            }
          }
        },
        {
          label: 'Confirm Password',
          disable: true,
          data: {
            field: 'confirm',
            type: 'input',
            attrs: {
              'show-password': true
            }
          }
        }
      ],
      rules: {
        phone: [
          {
            validator: (rules, value, callback) => {
              const child = this.$refs.phone[0]
              if (value === '') {
                child.updateOk(false)
                return callback(new Error('This field is required!'))
              }
              const phoneReg = /^1[3579][456789]\d{8}$/
              if (!phoneReg.test(value)) {
                child.updateOk(false)
                return callback(new Error('Format error!'))
              }
              child.updateOk(true)
              callback()
            }
          }
        ],
        name: [
          {
            validator: (rules, value, callback) => {
              const child = this.$refs.name[0]
              if (value === '') {
                child.updateOk(false)
                return callback(new Error('This field is required!'))
              }
              if (value.length < 3) {
                child.updateOk(false)
                return callback(new Error('The minimum length is 3!'))
              }
              if (value.length > 16) {
                child.updateOk(false)
                return callback(new Error('The maximum length is 16!'))
              }
              const nameReg = /^[a-zA-Z]+[a-zA-Z0-9]*$/
              if (!nameReg.test(value)) {
                child.updateOk(false)
                return callback(new Error('Format error!'))
              }
              child.updateOk(true)
              callback()
            }
          }
        ],
        password: [
          {
            validator: (rules, value, callback) => {
              const child = this.$refs.password[0]
              if (value === '') {
                child.updateOk(false)
                return callback(new Error('This field is required!'))
              }
              if (value.length < 6) {
                child.updateOk(false)
                return callback(new Error('The minimum length is 6!'))
              }
              if (value.length > 16) {
                child.updateOk(false)
                return callback(new Error('The maximum length is 16!'))
              }
              if (!/^[a-zA-Z0-9]*$/.test(value)) {
                child.updateOk(false)
                return callback(new Error('Invalid character!'))
              }
              const noUpper = /^[a-z0-9]*$/.test(value)
              const noLower = /^[A-Z0-9]*$/.test(value)
              const noNumber = /^[a-zA-Z]*$/.test(value)
              if ((noUpper && noLower) || (noUpper && noNumber) || (noLower && noNumber)) {
                child.updateOk(false)
                return callback(new Error('Format error!'))
              }
              child.updateOk(true)
              callback()
            }
          }
        ],
        confirm: [
          {
            validator: (rules, value, callback) => {
              const child = this.$refs.confirm[0]
              if (!this.formData.password) {
                child.updateOk(false)
                return callback()
              }
              if (value !== this.formData.password) {
                child.updateOk(false)
                return callback(new Error('Inconsistent passwords!'))
              }
              child.updateOk(true)
              callback()
            }
          }
        ]
      }
    }
  },
  components: {
    FormItemExtend
  },
  methods: {
    modifyForm (field, value) {
      this.$set(this.formData, field, value)
    },
    updateOk (field, show) {
      this.$refs[field][0].updateOk(show)
    },
    signup () {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          this.disable = true
          this.showLoading = true
          this.animation.play()

          const params = {
            telephone: this.formData.phone,
            name: this.formData.name,
            password: sha512(this.formData.password),
            type: 'user'
          }
          console.log(params)
          this.$http.post('/api/v1/account/signup', params).then(res => {
            console.log(res)

            if (res.status === 200) {
              this.isSuccess = true
              this.animationSuccess.play()
            } else {
              alert('Login Fail!')
            }
          }).catch(err => {
            console.log(err)
            alert('Login Fail!')

            this.animation.stop()
            this.showLoading = false
            this.disable = false
          })
        }
      })
    },
    jumpToLogin () {
      this.$router.push('/login')
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.animation = Lottie.loadAnimation({
        container: this.$refs.loading,
        renderer: 'svg',
        loop: true,
        autoplay: false,
        path: loadingJSONPath
      })
      this.animationSuccess = Lottie.loadAnimation({
        container: this.$refs.successAnimation,
        renderer: 'svg',
        loop: false,
        autoplay: false,
        path: successJSONPath
      })
    })
  }
}
</script>

<style lang="less" scoped>
.container {
  height: 100%;
}
.sign-up-box {
  width: 500px;
  padding: 20px 50px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid white;
  border-radius: 7px;
  box-shadow: 0 0 5px #ddd;
}
.logo-image {
  width: 170px;
  margin-left: -15px;
}
.note {
  margin-top: 20px;
  margin-bottom: 10px;
  font-size: 15px;
  font-weight: 600;
}
.el-button {
  width: 80%;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 15px;
  margin-top: 15px;
  font-weight: 700;
  font-size: 15px;
}
.login {
  text-align: center;
  margin-top: 25px;
  font-weight: 400;
}
.loading {
  height: 50px;
  margin-bottom: -30px;
}

.success-reminder {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  .success {
    width: 60px;
  }

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
