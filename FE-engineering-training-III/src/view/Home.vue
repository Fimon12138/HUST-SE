<template>
  <div style="overflow: auto">
    <!-- header section -->
    <div class="header">
      <md-toolbar class="md-large md-transparent toolbar">
        <!-- the first line, including image, search input, two buttons and an icon -->
        <div class="md-toolbar-row">
          <!-- left: img & input -->
          <div class="md-toolbar-section-start">
            <img src="../assets/img/logo.png" alt="loading failed" class="logo-image">
            <md-field class="search-area">
              <md-icon :md-src="require('../assets/svg/search.svg')"></md-icon>
              <md-input v-model="searchPattern" placeholder="Search"></md-input>
            </md-field>
          </div>

          <!-- right: two buttons & icon -->
          <div class="md-toolbar-section-end">
            <span class="small-button-text" style="margin-right: 30px" @click="tickets">My Tickets</span>
            <div v-if="isLogin" style="display: flex" @click="login">
              <span class="small-button-text" style="margin-right: 5px; line-height: 30px">Log in</span>
              <md-icon :md-src="require('../assets/svg/account_circle.svg')" style="width: 30px; height: 30px"></md-icon>
            </div>
            <div v-else style="display: flex">
              <el-popover
                placement="bottom"
                trigger="hover">
                <div class="padel">
                  <span class="small-button-text" @click="profile">Profile</span>
                  <span class="small-button-text" style="margin-top: 10px" @click="logout">Log out</span>
                </div>
                <span class="small-button-text user-name" style="margin-right: 5px; line-height: 30px" slot="reference">{{ userName }}</span>
              </el-popover>
              <md-icon :md-src="require('../assets/svg/account_circle.svg')" style="width: 30px; height: 30px"></md-icon>
            </div>
          </div>
        </div>
      </md-toolbar>
    </div>

    <!-- body -->
    <div class="main">
      <router-view></router-view>
    </div>

    <el-divider></el-divider>
    <div class="footer">
      <span>A Work of ZhengJing Team</span>
      <img :src="teamLogo" alt="error">
    </div>
  </div>
</template>

<script>
import teamLogo from '../assets/img/teamlogo.png'

export default {
  data () {
    return {
      searchPattern: '',
      isLogin: true,
      userName: '',
      teamLogo
    }
  },
  methods: {
    login () {
      this.$router.push('/login')
    },
    profile () {
      this.$router.push('/profile/')
    },
    logout () {
      window.sessionStorage.removeItem('name')
      window.sessionStorage.removeItem('token')
      location.reload()
    },
    tickets () {
      this.$router.push('/tickets')
    }
  },
  created () {
    this.userName = window.sessionStorage.getItem('name')
    if (this.userName) {
      this.isLogin = false
    }
  }
}
</script>

<style lang="less" scoped>
.toolbar {
  padding-top: 20px;
  padding-left: 15%;
  padding-right: 15%;
}
.logo-image {
  width: 120px;
}
.search-area {
  margin-left: 20px;
  width: 300px;
}
.small-button-text {
  font-size: 15px;
  font-weight: bold;
  font-family: 'Quicksand-Bold';
  cursor: pointer;
}
.small-button-text:hover {
  color: #1bb1ba;
}
.main {
  margin-bottom: 50px;
}
.user-name {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.padel {
  display: flex;
  flex-direction: column;

  padding: 5px 10px;
  width: 100%;

  span {
    text-align: center;
  }
}

.footer {
  padding-top: 30px;
  padding-bottom: 20px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;

  span {
    font-family: 'Lineto-Brown-Bold';
    font-size: 15px;
  }

  img {
    margin-left: 10px;

    width: 50px;
  }
}
</style>
