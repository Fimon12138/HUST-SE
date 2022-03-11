<template>
  <div class="container-ticket" @click="linkTo">
    <img :src="logo" alt="error" class="logo">
    <div class="info">
      <p class="name">{{ name }}</p>
      <div class="divider" style="margin-top: 3px"></div>
      <div class="bottom">
        <div v-if="date" class="line">
          <md-icon :md-src="dateIcon" class="icon-size"></md-icon>
          <p class="text note">{{ date }}</p>
        </div>
        <div v-if="location" class="line">
          <md-icon :md-src="locationIcon" class="icon-size"></md-icon>
          <p class="text note">{{ location }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import locationIcon from '../assets/svg/location.svg'
import dateIcon from '../assets/svg/date.svg'

export default {
  props: {
    id: {
      required: true,
      type: String
    },
    logo: {
      required: true,
      type: String
    },
    name: {
      required: true,
      type: String
    },
    location: String,
    date: String
  },
  data () {
    return {
      locationIcon,
      dateIcon
    }
  },
  methods: {
    linkTo () {
      const reqUrl = '/activityInfo?id=' + this.id
      this.$router.push(reqUrl)
    }
  }
}
</script>

<style lang="less" scoped>
.container-ticket {
  padding: 10px 20px;
  width: 335px;
  border-radius: 10px;
  display: flex;
  flex-direction: row;
}
.logo {
  width: 75px;
  height: 100px;
}
.info {
  margin-left: 20px;
  height: 100px;
  position: relative;
  .text {
    width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin: 0;
  }
  .name {
    font-family: 'Lineto-Brown-Bold';
    font-size: 16px;
    margin: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    overflow: hidden;
    /*! autoprefixer: off */
    -webkit-box-orient: vertical;
  }
  .bottom {
    position: absolute;
    bottom: 0px;
  }
  .line {
    display: flex;
    height: 20px;
  }
  .note {
    font-family: 'BalooTammudu-SemiBold';
    font-size: 14px;
    height: 100%;
    padding: 6px 0 6px 5px;
  }
  .icon-size {
    width: 20px;
    height: 20px;
  }
}
</style>
