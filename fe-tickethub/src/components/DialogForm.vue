<template>
  <div class="container">
    <el-dialog :title="title" :visible.sync="showStatus"
               @close="close" style="font-weight: 700" width="30%">
      <el-divider></el-divider>
      <el-form ref="formRef" v-model="formData" label-position="top" label-width="100%">
        <el-form-item v-for="item in formSchema" :key="item.field"
                      :label="item.label" :prop="item.field"
        >
          <form-item @input="modifyForm" :renderData="{ schema: item, form: formData }"></form-item>
        </el-form-item>
      </el-form>
      <el-divider></el-divider>
      <div class="action-btn-group">
        <el-button v-for="item in actionSchema" :key="item.label" @click="() => item.onClick()"
                   :type="item.type">{{ item.label }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import FormItem from 'formItem.js'

export default {
  props: {
    show: {
      type: Boolean,
      default: false,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    formData: {
      type: Object,
      required: true
    },
    formSchema: {
      type: Array,
      required: true
    },
    actionSchema: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      showStatus: this.show
    }
  },
  components: {
    FormItem
  },
  methods: {
    modifyForm (schema, value) {
      this.$set(this.formData, schema.field, value)
    },
    close () {
      this.$emit('closeDialog')
    }
  },
  watch: {
    show (value) {
      this.showStatus = value
    }
  }
}
</script>

<style lang="less" scoped>
.el-divider--horizontal {
  margin: 0;
}
.el-form {
  padding-top: 10px;
  padding-left: 20px;
  padding-right: 20px;
  padding-bottom: 20px;
  .el-form-item {
    margin-bottom: 5px;
  }
  .el-select {
    width: 100%;
  }
}
.action-btn-group {
  padding: 10px;
  display: flex;
  justify-content: flex-end;
}
</style>

<style lang="less">
.container {
  .el-dialog__body {
    padding: 0;
  }
  .el-form {
    .el-form-item__label {
      padding-bottom: 0 !important;
    }
  }
}
</style>
