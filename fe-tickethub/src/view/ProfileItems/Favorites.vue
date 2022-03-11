<template>
  <div class="container-favorites">
    <Activity
      v-for="(favorite, index) in favoritesList" :key="index"
      :id="favorite.id"
      :logo="favorite.logo"
      :name="favorite.name"
      :location="favorite.location"
      :date="favorite.date"
      :star="favorite.star"
      @update="getFavorites"
      class="favorite"
    ></Activity>
  </div>
</template>

<script>
import Activity from '../../components/Activity'

export default {
  data () {
    return {
      favoritesList: []
    }
  },
  components: {
    Activity
  },
  methods: {
    getFavorites () {
      this.favoritesList = []

      this.$http.post('/api/v1/favorite/list', {
        userId: window.sessionStorage.getItem('token'),
        pageNo: 1,
        pageSize: 100
      }).then(res => {
        if (res.status === 200 && res.data.totalCount > 0) {
          res.data.result.forEach(element => {
            const favorite = {
              id: element.id,
              logo: element.imageColumn,
              name: element.name,
              location: element.location,
              date: element.startTime,
              star: true
            }
            this.favoritesList.push(favorite)
          })
        } else {
          console.log(res)
        }
      }).catch(err => {
        console.log(err)
        alert('Error!')
      })
    }
  },
  created () {
    this.getFavorites()
  }
}
</script>

<style lang="less" scoped>
.container-favorites {
  display: flex;
  flex-flow: row wrap;
  align-items: center;
  margin-top: 30px;
}

.favorite {
  margin: 10px 20px;
}
</style>
