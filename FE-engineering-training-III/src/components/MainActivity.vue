<template>
  <div class="container-main-activity">
    <img :src="imgUrl" alt="error" class="logo" @click="detail">

    <div class="info">
      <div class="heart" ref="heart" @click="like"></div>
      <p class="date">{{ date }}</p>
      <p class="name">{{ name }}</p>
      <p class="location bottom">{{ location }}</p>
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
    imgUrl: {
      type: String,
      required: true
    },
    date: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    location: {
      type: String,
      required: true
    },
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
    // console.log(likeAnimationData)
    // const animationContainer = document.getElementsByName('heart')[0]
    this.$nextTick(() => {
      // console.log(this.$refs.heart)
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
.container-main-activity {
  width: 210px;
  height: 280px;
  border-radius: 7px;
  position: relative;
  overflow: hidden;
}
.logo {
  width: 100%;
  height: 100%;
}
.info {
  position: absolute;
  left: 0;
  bottom: -110px;
  background-color: black;
  z-index: 1;
  width: 100%;
  height: 110px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3));
  color: white;
  padding: 10px 5px;

  transition-duration: 1s;
  -webkit-transition-duration: 1s;
  p {
    margin: 0;
  }
  .date {
    font-family: 'BalooTammudu-SemiBold';
    font-size: 17px;
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
    margin-top: 10px;
  }
  .location {
    font-size: 13px;
    width: 180px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .bottom {
    position: absolute;
    bottom: 0px;
  }
  .heart {
    position: absolute;
    top: 2px;
    right: 2px;
    width: 32px;
    height: 32px;
    z-index: 2;
  }
}
.container-main-activity:hover {
  .info {
    bottom: 0;
    -webkit-transition-property: bottom;
    transition-property: bottom;
    transition-duration: .3s;
    -webkit-transition-duration: .3s;
  }
}
</style>
