<template>
  <div class="container-all-activities">
    <ActivityForMore
      v-for="(activity, index) in activityList" :key="index"
      :activity="activity"
    ></ActivityForMore>
  </div>
</template>

<script>
import ActivityForMore from '../components/ActivityForMore'

export default {
  data () {
    return {
      activityList: []
    }
  },
  components: {
    ActivityForMore
  },
  created () {
    const type = this.$route.query.type
    this.$http.post('/api/v1/ticket/list', {
      pageNo: 1,
      pageSize: 100,
      type: type
    }).then(res => {
      if (res.status === 200 && res.data.totalCount > 0) {
        res.data.result.forEach(element => {
          const activity = {
            logo: element.imageColumn,
            date: element.startTime,
            name: element.name,
            location: element.location,
            id: element.id
          }

          this.activityList.push(activity)
        })
      } else {
        console.log(res)
      }
    }).catch(err => {
      console.log(err)
      alert('Error!')
    })
  }
}
</script>

<style lang="less" scoped>
.container-all-activities {
  width: 1350px;

  position: relative;
  left: 50%;
  transform: translateX(-50%);

  display: flex;
  flex-flow: row wrap;
  align-items: center;
  margin-top: 30px;
}
</style>
