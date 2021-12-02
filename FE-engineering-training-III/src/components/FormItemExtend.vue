<template>
  <div class="container">
    <div class="label-area">
      <div class="left">
        <md-icon v-if="useIcon" :md-src="iconUrl" class="icon"></md-icon>
        <span class="label">{{ label }}</span>
      </div>
      <div class="right">
        <md-icon v-if="useOk" :md-src="require('../assets/svg/check_circle_green.svg')" v-show="showOk"></md-icon>
      </div>
    </div>
    <el-tooltip effect="dark" placement="top-end" :disabled="disableNote">
      <div slot="content" v-html="note"></div>
      <FormItem @input="upSend" :renderData="renderData"></FormItem>
    </el-tooltip>
  </div>
</template>

<script>
import FormItem from './formItem'

export default {
  props: {
    useIcon: {
      type: Boolean,
      default: false
    },
    iconUrl: String,
    label: {
      type: String,
      required: true
    },
    useOk: {
      type: Boolean,
      default: false
    },
    renderData: {
      type: Object,
      required: true
    },
    note: {
      type: String,
      default: ''
    },
    disableNote: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      showOk: false
    }
  },
  components: {
    FormItem
  },
  methods: {
    upSend (schema, value) {
      // console.log('upSend: ', schema.field, value)
      this.$emit('input', schema.field, value)
    },
    updateOk (show) {
      this.showOk = show
    }
  }
}
</script>

<style lang="less" scoped>
.label-area {
  display: flex;
  justify-content: space-between;
  height: 30px;
  line-height: 30px;
  .left {
    display: flex;
  }
  .right {
    display: flex;
  }
}
.icon {
  margin-right: 10px;
}
.label {
  font-size: 14px;
}
.input {
  margin-top: 5px;
}
</style>
