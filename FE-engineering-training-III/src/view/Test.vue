<template>
  <div class="outer">
    <Ticket
      :logo="logo"
      :name="name"
      :location="location"
      :date="date"
    >
    </Ticket>
    <Activity
      :logo="logo"
      :name="name"
      location="Wuhan Center, Hongshan District, Wuhan, China"
      date="1 Jan, 2020"
      style="margin-top: 30px"
    ></Activity>
    <MainActivity
      :imgUrl="logo"
      :name="name"
      location="Wuhan Center, Hongshan District, Wuhan, China"
      date="1 Jan, 2020"
      style="margin-top: 30px"
    ></MainActivity>
    <ActivityCategory
      category="Concerts"
      :main="main"
      :sub="sub"
      style="margin-top: 30px;"
    ></ActivityCategory>

    <Order
      :order="order"
      style="margin-top: 30px"
    ></Order>

    <RemarkActivity
      :comment="comment"
      :index="0"
      style="margin-top: 30px"
    ></RemarkActivity>

    <RemarkActivity
      :comment="comment"
      :index="1"
      style="margin-top: 30px"
    ></RemarkActivity>

    <RemarkProfile
      :comment="commentP"
      style="margin-top: 30px"
    ></RemarkProfile>

    <ActivityForMore
      :activity="activity"
      style="margin-top: 30px"
    ></ActivityForMore>

    <RemarkEditor
      style="margin-top: 30px"
    ></RemarkEditor>
  </div>
</template>

<script>
import Ticket from '../components/Ticket'
import Activity from '../components/Activity'
import MainActivity from '../components/MainActivity'
import ActivityCategory from '../components/ActivityCategory'
import Order from '../components/Order'
import RemarkActivity from '../components/RemarkActivity'
import RemarkProfile from '../components/RemarkProfile'
import ActivityForMore from '../components/ActivityForMore'
import RemarkEditor from '../components/RemarkEditor'

import logo from '../assets/img/vue-logo.png'
import activityPic from '../assets/img/activity.png'
// import activityVertical from '../assets/img/activityVertical.jpg'

export default {
  data () {
    return {
      logo,
      name: 'Liu Bo\'s Personal Concert - Wuhan Station',
      location: 'Wuhan Center',
      date: '2020-1-1 19:00',
      activity: {},
      order: {
        name: 'Liu Bo\'s Personal Concert - Wuhan Station',
        id: '0021',
        price: '30',
        createTime: '2020-1-1',
        state: 'processing'
      },
      comment: {
        avatar: logo,
        nickname: 'yihang lu',
        commentText: 'Test '.repeat(20)
      },
      commentP: {
        logo: activityPic,
        name: 'Liu Bo\'s Personal Concert - Wuhan Station',
        commentText: 'Test '.repeat(20)
      },
      main: {
        imgUrl: logo,
        name: 'Liu Bo\'s Personal Concert - Wuhan Station',
        location: 'Wuhan Center',
        date: '2020-1-1 19:00'
      },
      sub: [
        {
          logo,
          name: 'Liu Bo\'s Personal Concert - Wuhan Station',
          location: 'Wuhan Center',
          date: '2020-1-1 19:00'
        },
        {
          logo,
          name: 'Liu Bo\'s Personal Concert - Wuhan Station',
          location: 'Wuhan Center',
          date: '2020-1-1 19:00'
        },
        {
          logo,
          name: 'Liu Bo\'s Personal Concert - Wuhan Station',
          location: 'Wuhan Center',
          date: '2020-1-1 19:00'
        },
        {
          logo,
          name: 'Liu Bo\'s Personal Concert - Wuhan Station',
          location: 'Wuhan Center',
          date: '2020-1-1 19:00'
        },
        {
          logo,
          name: 'Liu Bo\'s Personal Concert - Wuhan Station',
          location: 'Wuhan Center',
          date: '2020-1-1 19:00'
        },
        {
          logo,
          name: 'Liu Bo\'s Personal Concert - Wuhan Station',
          location: 'Wuhan Center',
          date: '2020-1-1 19:00'
        }
      ]
    }
  },
  components: {
    Ticket,
    Activity,
    MainActivity,
    ActivityCategory,
    Order,
    RemarkActivity,
    RemarkProfile,
    ActivityForMore,
    RemarkEditor
  },
  created () {
    // this.$http.get('https://run.mocky.io/v3/0e0b47c6-6310-4b81-b6f1-ebf11968b751').then(res => {
    //   console.log(res)
    //   console.log(res.data)
    //   console.log(typeof res.data)

    //   this.$set(this.activity, 'logo', res.data.imageColumn)
    //   this.$set(this.activity, 'name', res.data.ticketName)
    //   this.$set(this.activity, 'location', res.data.location)
    //   this.$set(this.activity, 'date', res.data.startTime)

    //   console.log(this.activity)
    // }).catch(err => {
    //   console.log(err)
    // })
    const params = {
      nickname: 'test1',
      telephone: '17607100643'
    }

    this.$http.post('/api/v1/user', params).then(res => {
      console.log(res)
    }).catch(err => {
      console.log(err)
    })
  }
}
</script>

<style lang="less" scoped>
.outer {
  padding-top: 50px;
  padding-left: 30px;
  padding-bottom: 500px;
}
</style>
