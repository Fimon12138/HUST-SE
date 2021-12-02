<template>
  <div class="outer">
    <el-button-group>
      <el-button icon="el-icon-circle-plus" @click="add"></el-button>
      <el-button icon="el-icon-remove" :disabled="item.disableRemove" @click="remove"></el-button>
      <el-button icon="el-icon-caret-top" :disabled="item.disableUp" @click="up"></el-button>
      <el-button icon="el-icon-caret-bottom" :disabled="item.disableDown" @click="down"></el-button>
    </el-button-group>
    <br>
    <span class="label">{{ item.label }}</span>
    <span class="required" v-show="item.required">(required)</span>
    <el-input v-model="item.data" @input="modify"></el-input>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  methods: {
    add () {
      this.$parent.add(this.item)
    },
    remove () {
      this.$parent.remove(this.item)
    },
    up () {
      this.$parent.up(this.item)
    },
    down () {
      this.$parent.down(this.item)
    },
    modify (value) {
      this.$emit('input')
    }
  }
}
</script>

<style lang="less" scoped>
.outer {
  margin-top: 20px;
}
.el-button {
  padding: 5px 10px;
  font-size: 15px;
}
.label {
  font-weight: 700;
}
.required {
  margin-left: 5px;
  font-style: italic;
  color: #8f8f8f;
  font-weight: 300;
}
</style>

<style lang="less">
.el-form {
  .el-form-item__label {
    padding-bottom: 0 !important;
  }
}
</style>
