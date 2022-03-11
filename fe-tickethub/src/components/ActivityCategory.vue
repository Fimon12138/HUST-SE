<template>
  <div class="container-activity-category">
    <div class="tab">
      <span class="title">{{ category }}</span>
      <div @click="linkTo" style="cursor:pointer">
        <span>more</span>
        <img :src="rightArrow" alt="error" class="rightArrow">
      </div>
    </div>
    <div class="activities">
      <div class="main-activity">
        <MainActivity
          :id="main.id"
          :imgUrl="main.imgUrl"
          :date="main.date"
          :name="main.name"
          :location="main.location"
          :star="main.star"
        ></MainActivity>
      </div>
      <div class="sub-activities">
        <Activity
          v-for="(activity, index) in sub" :key="index"
          :id="activity.id"
          :logo="activity.logo"
          :name="activity.name"
          :location="activity.location"
          :date="activity.date"
          :star="activity.star"
          class="sub-activity"
        ></Activity>
      </div>
    </div>
  </div>
</template>

<script>
import Activity from './Activity'
import MainActivity from './MainActivity'
import rightArrow from '../assets/svg/arrow_right.svg'

export default {
  props: {
    category: {
      type: String,
      required: true
    },
    type: {
      type: String,
      required: true
    },
    main: {
      type: Object,
      required: true
    },
    sub: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      rightArrow
    }
  },
  components: {
    Activity,
    MainActivity
  },
  methods: {
    linkTo () {
      const reqUrl = '/allactivities?type=' + this.type
      this.$router.push(reqUrl)
    }
  }
}
</script>

<style lang="less" scoped>
.container-activity-category {
  width: 1350px;
  padding: 10px 20px;
  box-shadow: 0 0 5px #dddddd;
  border-radius: 6px;

  margin-top: 20px;
  margin-bottom: 20px;
}
.tab {
  display: flex;
  width: 100%;
  justify-content: space-between;
  .title {
    font-family: 'Lineto-Brown-Bold';
    font-size: 25px;
  }
  .rightArrow {
    margin-left: -5px;
  }
}
.activities {
  margin-top: 15px;
  width: 100%;
  height: 100%;
  display: flex;
  .sub-activities {
    justify-content: flex-end;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    .sub-activity {
      margin-left: 10px;
      margin-right: 10px;
    }
  }
}
</style>
