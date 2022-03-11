<template>
  <div class="container-ticket-terms">
    <div v-for="(term, index) in terms" :key="index">
      <div class="title">
        <md-icon :md-src="term.logo" class="icon-size"></md-icon>
        <span>{{ term.title }}</span>
      </div>
      <div class="text">
        <span>{{ term.text }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import yes from '../assets/svg/check_red.svg'
import no from '../assets/svg/cancel_red.svg'

export default {
  data () {
    return {
      terms: [
        {
          logo: no,
          title: 'Does not support withdrawal',
          text: 'Tickets are negotiable securities, non-ordinary commodities, and the cultural services carried behind them are time-sensitive and scarce, and they are not refundable.'
        },
        {
          logo: yes,
          title: 'Real-name performance',
          text: 'This project requires real-name ticket purchase and admission. Please bring your ID to verify admission when you watch the show.'
        },
        {
          logo: yes,
          title: 'e-Ticket',
          text: 'Electronic ticket: You can directly enter the venue through the QR code or ID card in the ticket holder (the specific ticket holder information after the order shall prevail)'
        },
        {
          logo: yes,
          title: 'Paper invoice',
          text: 'The project supports the issuance of paper invoices. Please make up for it through the order page before the performance starts. The invoice will be provided by the organizer about one month after the performance ends.'
        }
      ]
    }
  }
}
</script>

<style lang="less" scoped>
.container-ticket-terms {
  background-color: #f2f2f2;
  border-radius: 2px;
  border-width: 1px;
  border-style: solid;
  border-color: #ebebeb;
  padding: 5px 0;
}

.title {
  display: flex;
  flex-direction: row;
  padding: 5px 10px;

  .md-icon {
    margin: 0;
    width: 20px;
    height: 20px;
  }

  span {
    font-family: 'Quicksand-SemiBold';
    font-size: 14px;
    padding-top: 2px;
    margin-left: 3px;
  }
}

.text {
  padding: 0 5px 0 15px;

  span {
    font-family: 'NotoSansSC-Regular';
    color: grey;
    font-size: 12px;
  }
}
</style>
