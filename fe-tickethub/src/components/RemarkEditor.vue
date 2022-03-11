<template>
  <div class="container-remark-editor">
    <el-form ref="formRef" :rules="rules" :model="formData">
      <el-form-item label="Share your opinions:" prop="remark">
        <el-input
          type="textarea"
          :rows="2"
          placeholder="Only 100 characters are allowed."
          v-model="formData.remark"
          @input="clearValidation">
        </el-input>
      </el-form-item>
    </el-form>
    <div class="footer">
      <el-button type="success" @click="submit">Submit</el-button>
      <span class="success" v-show="showSuccess">Comment Posted Successfully!</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    ticketId: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      formData: {
        remark: ''
      },
      rules: {
        remark: [
          {
            validator: (rules, value, callback) => {
              if (!value) {
                return callback(new Error('No comment yet.'))
              }
              if (value.length > 100) {
                return callback(new Error('The length of your comment should be less than 100.'))
              }
              callback()
            },
            trigger: 'never'
          }
        ]
      },
      showSuccess: false
    }
  },
  methods: {
    submit () {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          console.log(this.formData.remark)
          this.$http.post('/api/v1/comment', {
            userId: window.sessionStorage.getItem('token'),
            ticketId: this.ticketId,
            content: this.formData.remark
          }).then(res => {
            console.log(res)

            if (res.status === 200 && res.data) {
              const comment = {
                commentText: this.formData.remark
              }
              this.$http.post('/api/v1/user/info', {
                id: window.sessionStorage.getItem('token')
              }).then(res => {
                if (res.status !== 200) {
                  return
                }
                comment.nickname = res.data.nickname
                comment.avatar = res.data.avatar

                this.$emit('newRemark', comment)

                this.formData.remark = ''

                this.showSuccess = true
                setTimeout(() => {
                  this.showSuccess = false
                }, 1000)
              })
            } else {
              alert('Post Comment Fail!')
            }
          }).catch(err => {
            console.log(err)
            alert('Post Comment Fail!')
          })
        }
      })
    },
    clearValidation () {
      this.$refs.formRef.clearValidate()
    }
  }
}
</script>

<style lang="less" scoped>
.el-button {
  padding: 6px 10px;
}

.footer {
  display: flex;
  flex-direction: column;
  align-items: flex-end;

  padding-right: 20px;
}

.success {
  color: #67c23a;
  font-size: 14px;

  margin-top: 5px;
}
</style>
