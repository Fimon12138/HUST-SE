<template>
  <div class="container">
    <div class="tool">
      <div :class="{ 'tab': true, 'select-item': activePart === 'upcoming' }"
        @click="getUpcoming">Upcoming</div>
      <div :class="{ 'tab': true, 'select-item': activePart === 'past' }"
        style="margin-left: 5px" @click="getPast">Past</div>
    </div>
    <div class="main">
      <Ticket
        v-for="(ticket, index) in ticketList" :key="index"
        :id="ticket.id"
        :logo="ticket.logo"
        :name="ticket.name"
        :location="ticket.location"
        :date="ticket.date"
        class="ticket"
      ></Ticket>
    </div>
  </div>
</template>

<script>
import Ticket from '../../components/Ticket'

export default {
  data () {
    return {
      activePart: '',
      ticketList: []
    }
  },
  components: {
    Ticket
  },
  methods: {
    updateTicketList (params) {
      this.ticketList = []

      this.$http.post('/api/v1/order/finished', params).then(res => {
        if (res.status === 200 && res.data.result) {
          res.data.result.forEach(element => {
            const ticket = {
              id: element.id,
              logo: element.imageColumn,
              name: element.name,
              location: element.location,
              date: element.startTime
            }
            this.ticketList.push(ticket)
          })
        } else {
          console.log(res)
        }
      }).catch(err => {
        console.log(err)
        alert('Error!')
      })
    },
    getUpcoming () {
      this.activePart = 'upcoming'

      this.updateTicketList({
        pageNo: 1,
        pageSize: 100,
        outOfDate: 'yes',
        userId: window.sessionStorage.getItem('token')
      })
    },
    getPast () {
      this.activePart = 'past'

      this.updateTicketList({
        pageNo: 1,
        pageSize: 100,
        outOfDate: 'no',
        userId: window.sessionStorage.getItem('token')
      })
    }
  },
  created () {
    this.getUpcoming()
  }
}
</script>

<style lang="less" scoped>
.container {
  margin-top: 60px;
}
.tool {
  display: flex;
  flex-direction: row;
  margin-left: 20px;
  .tab {
    text-align: center;
    width: 150px;
    padding-top: 10px;
    padding-bottom: 10px;
    font-family: 'Lineto-Brown-Bold';
    font-size: 17px;
    background-color: #e6e9eb;
  }
  .tab:hover {
    background-color: #cfd4d9;
  }
}
.select-item {
  border-bottom: 2px solid #00b5b5;
  background-color: white !important;
}
.main {
  display: flex;
  flex-flow: row wrap;
  align-items: center;
  margin-top: 30px;
}
.ticket {
  margin: 10px 20px;
}
.ticket:hover {
  box-shadow: 0 0 10px #dddddd;
  margin-top: 5px;
  margin-bottom: 15px;
  -webkit-transition-property: margin-top, margin-bottom;
  transition-property: margin-top, margin-bottom;
  transition-duration: .3s;
  -webkit-transition-duration: .3s;
}
</style>
