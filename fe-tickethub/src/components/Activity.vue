<template>
  <div class="container-activity">
    <div class="container-heart">
      <div class="triangle-mask"></div>
      <div ref="heart" class="heart" @click="like"></div>
    </div>
    <div class="info-area" @click="detail">
      <img :src="logo" alt="error" class="logo">
      <div class="info">
        <p class="date">{{ date }}</p>
        <p class="name">{{ name }}</p>
        <div class="divider"></div>
        <p class="location bottom">{{ location }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import lottie from 'lottie-web'
import likeAnimationData from '../assets/lottie_json/like_white.json'

export default {
  props: {
    id: {
      type: String,
      required: true
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
    date: String,
    star: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      isLike: this.star,
      animation: undefined
    }
  },
  methods: {
    like () {
      if (this.isLike) {
        // 取消收藏
        this.$http.post('/api/v1/favorite/delete', {
          userId: window.sessionStorage.getItem('token'),
          ticketId: this.id
        }).then(res => {
          if (res.status === 200) {
            this.animation.playSegments([9, 0], true)
            this.isLike = false
            this.$emit('update')
          }
        }).catch(err => {
          console.log(err)
          alert('Error!')
        })
      } else {
        // 添加收藏
        this.$http.post('/api/v1/favorite', {
          userId: window.sessionStorage.getItem('token'),
          ticketId: this.id
        }).then(res => {
          if (res.status === 200) {
            this.animation.playSegments([0, 9], true)
            this.isLike = true
            this.$emit('update')
          } else {
            console.log(res)
          }
        }).catch(err => {
          console.log(err)
          alert('Error!')
        })
      }
    },
    detail () {
      const reqUrl = '/activityInfo?id=' + this.id
      this.$router.push(reqUrl)
    }
  },
  mounted () {
    this.$nextTick(() => {
      // console.log(this.$refs)
      this.animation = lottie.loadAnimation({
        container: this.$refs.heart,
        renderer: 'svg',
        name: this.name,
        loop: false,
        autoplay: false,
        animationData: likeAnimationData
      })
      // console.log(animation)
      this.animation.goToAndStop(this.isLike ? 9 : 0, true)
    })
  }
}
</script>

<style lang="less" scoped>
.container-activity {
  padding: 10px 20px;
  width: 335px;
  border-radius: 10px;
  position: relative;
  overflow: hidden;
}

.info-area {
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
  p {
    margin: 0;
  }
  .date {
    color: #dbb283;
    font-family: 'BalooTammudu-SemiBold';
    font-size: 17px;
    margin-top: 10px;
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
    width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .bottom {
    position: absolute;
    bottom: 0px;
  }
}
.container-heart {
  position: absolute;
  top: -40px;
  right: -60px;
  width: 60px;
  height: 40px;
  border-top-right-radius: 10px;
  background: linear-gradient(to top right, rgba(191, 191, 191, 0.1), rgba(0, 0, 0, 0.5));

  transition-duration: 1s;
  -webkit-transition-duration: 1s;
  .triangle-mask {
    border-style: solid;
    border-width: 20px 30px 20px 30px;
    border-color: transparent transparent #fafafa #fafafa;
    width: 0;
    height: 0;
  }
  .heart {
    z-index: 1;
    position: absolute;
    top: -2px;
    right: 2px;
    width: 30px;
    height: 30px;
  }
}
.container-activity:hover {
  .container-heart {
    top: 0;
    right: 0;
    -webkit-transition-property: top, right;
    transition-property: top, right;
    transition-duration: .3s;
    -webkit-transition-duration: .3s;
  }
}
</style>
