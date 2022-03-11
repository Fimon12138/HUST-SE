<template>
  <div class="container">
    <md-button class="md-icon-button md-raised left vertical"
      @click="prePage">
      <md-icon :md-src="leftArrow"></md-icon>
    </md-button>
    <el-carousel id="carousel" :interval="5000" arrow="never" :height="bannerHeight + 'px'" ref="carousel"  @click.native="linkTo">
      <el-carousel-item v-for="(item, index) in itemList" :key="index" @click="detail(index)">
        <img :src="item" alt="loading fail" class="image">
      </el-carousel-item>
    </el-carousel>
    <md-button class="md-icon-button md-raised right vertical"
      @click="nextPage">
      <md-icon :md-src="rightArrow"></md-icon>
    </md-button>
  </div>
</template>

<script>
import leftArrow from '../assets/svg/arrow_left.svg'
import rightArrow from '../assets/svg/arrow_right.svg'

export default {
  props: {
    itemList: {
      type: Array,
      required: true
    },
    idList: {
      type: Array,
      required: true
    },
    widthRatio: {
      type: Number,
      default: 0.8
    },
    picRatio: {
      type: Number,
      default: 0.333
    }
  },
  data () {
    return {
      leftArrow,
      rightArrow,
      bannerHeight: 0
    }
  },
  methods: {
    setSize () {
      this.bannerHeight = this.screenWidth * this.widthRatio * this.picRatio
    },
    prePage () {
      this.$refs.carousel.prev()
    },
    nextPage () {
      this.$refs.carousel.next()
    },
    linkTo () {
      const activeIndex = this.$refs.carousel.activeIndex
      console.log(activeIndex)
      const reqUrl = '/activityInfo?id=' + this.idList[activeIndex]
      this.$router.push(reqUrl)
    }
  },
  mounted () {
    this.screenWidth = window.innerWidth
    this.setSize()

    window.onresize = () => {
      this.screenWidth = window.innerWidth
      this.setSize()
    }
  }
}
</script>

<style lang="less" scoped>
.el-carousel {
  box-shadow: 0 0 5px #ddd;
  border-radius: 7px;
}
.vertical {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}
.left {
  left: -25px;
}
.right {
  right: -25px;
}

.image {
  width: 100%;
  height: 100%;
}
</style>
