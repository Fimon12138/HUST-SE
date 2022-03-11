<template>
  <div class="container-activity-more" @click="linkTo">
    <img :src="activity.logo" alt="error" class="logo">
    <div class="activity-info">
      <div class="bottom">
        <p class="date">{{ activity.date }}</p>
        <p class="name">{{ activity.name }}</p>
        <div class="divider"></div>
        <p class="location">{{ activity.location }}</p>
      </div>
    </div>

    <div class="mask"></div>
  </div>
</template>

<script>
export default {
  props: {
    activity: {
      type: Object,
      required: true
    }
  },
  methods: {
    linkTo () {
      const reqUrl = '/activityInfo?id=' + this.activity.id
      this.$router.push(reqUrl)
    }
  },
  created () {
    console.log(this.activity)
  }
}
</script>

<style lang="less" scoped>
.container-activity-more {
  height: 280px;
  width: 350px;
  margin: 20px 50px;

  position: relative;
  background-color: white;

  display: flex;
  flex-direction: row;

  p {
    margin: 0;
    padding-right: 10px;
  }
}

.activity-info {
  margin-left: -80px;
  z-index: 2;

  .bottom {
    position: absolute;
    bottom: 0;
    right: 0;

    width: 200px;
  }

  .date {
    color: #dbb283;
    font-family: 'BalooTammudu-SemiBold';
    font-size: 17px;
    text-align: right;
  }

  .name {
    font-family: 'Lineto-Brown-Bold';
    font-size: 14px;

    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    overflow: hidden;
    /*! autoprefixer: off */
    -webkit-box-orient: vertical;
  }

  .location {
    font-size: 13px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin-top: 5px;
  }
}

.logo {
  width: 210px;
  height: 280px;
}

.mask {
  width: 0;
  height: 0;

  border-style: solid;
  border-width: 280px 81px 0 130px;
  border-color: transparent white transparent transparent;

  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
}
</style>
