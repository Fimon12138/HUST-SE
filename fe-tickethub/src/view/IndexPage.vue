<template>
  <div class="container-index-page">
    <CarouselWithArrow
      :itemList="recommendations"
      :idList="idList"
      class="carousel"
    ></CarouselWithArrow>
    <div class="activities-area">
      <ActivityCategory
        v-for="(category, index) in categories" :key="index"
        :category="category.title"
        :type="category.type"
        :main="category.main"
        :sub="category.sub"
      ></ActivityCategory>
    </div>
  </div>
</template>

<script>
import CarouselWithArrow from '../components/CarouselWithArrow'
import ActivityCategory from '../components/ActivityCategory'

export default {
  data () {
    return {
      recommendations: [],
      idList: [],
      categories: []
    }
  },
  components: {
    CarouselWithArrow,
    ActivityCategory
  },
  created () {
    // 拉取推荐位数据
    this.$http.get('/api/v1/recommendation').then(res => {
      console.log(res)

      const activities = res.data.ticket_ids

      activities.forEach(element => {
        this.recommendations.push(element.imageRow)
        this.idList.push(element.id)
      })
    }).catch(err => {
      console.log(err)
    })

    const types = [
      {
        label: 'Concert',
        type: 'concert'
      },
      {
        label: 'Sports',
        type: 'sport'
      },
      {
        label: 'Exhibition',
        type: 'exhibition'
      },
      {
        label: 'Opera',
        type: 'opera'
      }
    ]

    types.forEach(element => {
      this.$http.post('/api/v1/ticket/list', {
        pageNo: 1,
        pageSize: 7,
        type: element.type,
        userId: window.sessionStorage.getItem('token')
      }).then(res => {
        if (res.status === 200 && res.data.totalCount > 0) {
          const activities = res.data.result
          const mainActivity = {
            id: activities[0].id,
            imgUrl: activities[0].imageColumn,
            name: activities[0].name,
            location: activities[0].location,
            date: activities[0].startTime,
            star: activities[0].isSubscribed === 'yes'
          }
          const subActivities = []
          for (let i = 1; i < res.data.totalCount; i++) {
            const activity = {
              id: activities[i].id,
              logo: activities[i].imageColumn,
              name: activities[i].name,
              location: activities[i].location,
              date: activities[i].startTime,
              star: activities[i].isSubscribed === 'yes'
            }
            subActivities.push(activity)
          }

          const category = {
            title: element.label,
            type: element.type,
            main: mainActivity,
            sub: subActivities
          }
          this.categories.push(category)
        }
      }).catch(err => {
        console.log(err)
      })
    })
  }
}
</script>

<style lang="less" scoped>
.carousel {
  width: 80%;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 50px;
}
.activities-area {
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
