<template>
  <div class="container-remarks">
    <RemarkProfile
      v-for="(remark, index) in remarkList" :key="index"
      :comment="remark"
      :class="{ 'distanceTop': index > 0 }"
    ></RemarkProfile>
  </div>
</template>

<script>
import RemarkProfile from '../../components/RemarkProfile'

export default {
  data () {
    return {
      remarkList: []
    }
  },
  components: {
    RemarkProfile
  },
  created () {
    this.$http.post('/api/v1/comment/list', {
      pageNo: 1,
      pageSize: 100,
      userId: window.sessionStorage.getItem('token')
    }).then(res => {
      if (res.status === 200 && res.data.totalCount > 0) {
        res.data.result.forEach(element => {
          const comment = {
            commentText: element.content,
            name: element.ticketName
          }
          this.$http.post('/api/v1/ticket/info', {
            id: element.ticketId
          }).then(res => {
            if (res.status === 200) {
              comment.logo = res.data.imageRow
              this.remarkList.push(comment)
            }
          }).catch(err => {
            console.log(err)
            alert('Error!')
          })
        })
      }
    }).catch(err => {
      console.log(err)
      alert('Error!')
    })
  }
}
</script>

<style lang="less" scoped>
.container-remarks {
  margin-top: 40px;
}

.distanceTop {
  margin-top: 30px;
}
</style>
