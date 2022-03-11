<template>
  <div class="container-personal-info">
    <div class="user-avatar">
      <el-popconfirm
        confirmButtonText="Confirm"
        cancelButtonText="Cancel"
        hideIcon
        title="Upload new avatar."
        @onConfirm="changeAvatar"
      >
        <img :src="initAvatarUrl" alt="error" slot="reference">
      </el-popconfirm>
    </div>
    <div class="user-info" v-if="edit">
      <el-form ref="formRef" :model="formData" :rules="rules">
        <el-form-item v-for="item in formSchema" :key="item.field"
          :label="item.label" :prop="item.field" label-position="top">
          <FormItem
            @input="modifyForm"
            :renderData="{ schema: item, form: formData }"
          ></FormItem>
        </el-form-item>
      </el-form>
      <div class="buttons">
        <el-button @click="cancelEdit">Cancel</el-button>
        <el-button @click="submitEdit">Submit</el-button>
      </div>
    </div>
    <div v-else class="profile-view">
      <span class="title">Name</span>
      <span class="text">{{ formData.name }}</span>
      <span class="title">Telephone</span>
      <span class="text">{{ formData.phone }}</span>
      <span class="title">Description</span>
      <span class="text">{{ formData.desc }}</span>
      <span class="title">ZJPay Balance</span>
      <div class="balance">
        <div ref="animationContainer" class="animationContainer"
          @click="changeBalanceStatus"></div>
        <span>{{ balance }}</span>
      </div>
    </div>

    <el-button class="edit-button"
      icon="el-icon-s-tools"
      @click="edit = true">Edit</el-button>

    <!-- 上传用户头像的对话框 -->
    <el-dialog :visible.sync="showDialog" title="Select Profile Photo">
      <el-upload
        class="avatar-uploader"
        action="''"
        :http-request="uploadAvatar"
        :show-file-list="false"
        :before-upload="beforeAvatarUpload"
        accept="image/jpeg,image/jpg,image/png">
        <img v-if="newAvatarUrl" :src="newAvatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <div class="avatar-submit-button">
        <el-button @click="cancelAvatar">Cancel</el-button>
        <el-button type="success"
          :disabled="disableAvatarSubmit" @click="submitAvatar">Submit</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import FormItem from '../../components/formItem'
import { client } from '../../plugins/aliOSS'

import visibilityJSON from '../../assets/lottie_json/visibility.json'
import Lottie from 'lottie-web'

export default {
  data () {
    return {
      showDialog: false,
      initAvatarUrl: '',
      newAvatarUrl: '',
      avatarFileName: '',
      disableAvatarSubmit: true,

      edit: false,
      balance: '***',
      showBalance: false,
      animation: undefined,

      formData: {
        name: '',
        phone: '',
        desc: ''
      },
      formSchema: [
        { label: 'Name', field: 'name', type: 'input' },
        { label: 'Telephone', field: 'phone', type: 'input' },
        {
          label: 'Description',
          field: 'desc',
          type: 'input',
          props: {
            type: 'textarea'
          },
          attrs: {
            rows: 5
          }
        }
      ],
      rules: {

      }
    }
  },
  components: {
    FormItem
  },
  methods: {
    modifyForm (schema, value) {
      this.$set(this.formData, schema.field, value)
    },
    changeAvatar () {
      this.showDialog = true
    },
    beforeAvatarUpload (item) {
      const acceptTypes = ['image/jpeg', 'image/jpg', 'image/png']
      const isIMAGE = acceptTypes.includes(item.type)
      // console.log(isIMAGE)
      return isIMAGE
    },
    uploadAvatar (item) {
      const originName = item.file.name
      const type = originName.substring(originName.lastIndexOf('.'))
      const fileName = new Date().getTime() + type

      client().multipartUpload(fileName, item.file).then(res => {
        console.log(res)
        if (res.res.status === 200) {
          this.newAvatarUrl = 'https://cloudmarkdown.oss-cn-beijing.aliyuncs.com/' +
            fileName
          this.disableAvatarSubmit = false
        }
      }).catch(err => {
        alert(err)
      })
    },

    cancelAvatar () {
      this.newAvatarUrl = ''
      this.disableAvatarSubmit = true
      this.showDialog = false
    },
    submitAvatar () {
      this.$http.post('/api/v1/user/update', {
        avatar: this.newAvatarUrl,
        id: window.sessionStorage.getItem('token')
      }).then(res => {
        if (res.status === 200) {
          this.initAvatarUrl = this.newAvatarUrl
          this.cancelAvatar()
          location.reload()
        } else {
          console.log(res)
        }
      }).catch(err => {
        alert('Error!')
        console.log(err)
      })
    },

    cancelEdit () {
      this.$refs.formRef.resetFields()
      console.log(this.formData)
      this.edit = false
      this.loadAnima()
    },
    submitEdit () {
      this.$http.post('/api/v1/user/update', {
        id: window.sessionStorage.getItem('token'),
        nickname: this.formData.name,
        telephone: this.formData.phone,
        description: this.formData.desc
      }).then(res => {
        if (res.status === 200) {
          window.sessionStorage.setItem('name', this.formData.name)
          location.reload()
          this.edit = false
          this.loadAnima()
        } else {
          console.log(res)
        }
      }).catch(err => {
        console.log(err)
        alert('Error!')
      })
    },
    changeBalanceStatus () {
      if (this.showBalance) {
        this.showBalance = false
        this.balance = '***'
        this.animation.playSegments([0, 24], true)
      } else {
        this.showBalance = true
        this.animation.playSegments([24, 0], true)
        this.$http.post('/api/v1/user/info', {
          id: window.sessionStorage.getItem('token')
        }).then(res => {
          if (res.status === 200) {
            this.$http.post('/api/v1/zjpay/info', {
              id: res.data.payId
            }).then(res => {
              if (res.status === 200) {
                this.balance = 'USD ' + res.data.Money
              }
            })
          }
        }).catch(err => {
          console.log(err)
          alert('Error!')
        })
      }
    },
    loadAnima () {
      this.$nextTick(() => {
        this.showBalance = false
        this.animation = Lottie.loadAnimation({
          container: this.$refs.animationContainer,
          renderer: 'svg',
          loop: false,
          autoplay: false,
          animationData: visibilityJSON
        })
        this.animation.goToAndStop(24, true)
      })
    }
  },
  created () {
    this.$http.post('/api/v1/user/info', {
      id: window.sessionStorage.getItem('token')
    }).then(res => {
      if (res.status === 200) {
        this.initAvatarUrl = res.data.avatar
        this.formData.name = res.data.nickname
        this.formData.phone = res.data.telephone
        this.formData.desc = res.data.description
      }
    })
  },
  mounted () {
    this.loadAnima()
  }
}
</script>

<style lang="less" scoped>
.container-personal-info {
  display: flex;
  flex-direction: row;

  padding: 50px 100px;
  width: 100%;

  position: relative;
}

.user-avatar {
  width: 120px;
  padding: 20px 10px 10px 0;

  img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }
}

.user-info {
  padding: 0 20px;
  width: 60%;
}

.buttons {
  float: right;
  margin-top: 20px;
}

.avatar-uploader {
  /deep/ .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  /deep/ .el-upload:hover {
    border-color: #409EFF;
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-submit-button {
  margin-top: -20px;
  float: right;
}

.profile-view {
  display: flex;
  flex-direction: column;

  padding: 30px 20px;

  .title {
    font-family: 'BalooTammudu-SemiBold';
    font-size: 19px;
  }

  .text {
    color: #909090;
    font-family: 'NotoSerifJP-SemiBold';
    font-size: 14px;
    padding: 5px 0 30px 10px;
  }
}

.edit-button {
  position: absolute;
  top: 50px;
  right: 20%;
}

.balance {
  display: flex;
  flex-direction: row;

  .animationContainer {
    width: 20px;
  }

  span {
    margin-left: 10px;
    padding-top: 3px;
  }
}
</style>
