<template>
  <div class="container">
    <span class="title">{{ title }}</span>
    <el-divider></el-divider>
    <div class="body">
      <el-button icon="el-icon-circle-plus" v-show="showAddButton" class="add-new-button"
                 @click="addNewItem">Add New Item</el-button>
      <div class="main">
        <DynamicFormItem
          v-for="item in itemList" :key="item.key"
          :item="item" @input="modifyData"
        ></DynamicFormItem>
      </div>
    </div>
  </div>
</template>

<script>
import DynamicFormItem from 'DynamicFormItem.vue'

export default {
  props: {
    title: {
      type: String,
      required: true
    },
    minItems: {
      type: Number,
      default: 0
    },
    initArray: {
      type: Array,
      default: () => { return [] }
    },
    label: String
  },
  data () {
    return {
      showAddButton: false,
      itemList: []
    }
  },
  components: {
    DynamicFormItem
  },
  watch: {
    itemList (value) {
      this.showAddButton = this.itemList.length === 0
      this.$emit('syncData', this.getData())
    }
  },
  methods: {
    modifyData () {
      this.$emit('syncData', this.getData())
    },
    add (item) {
      const index = this.itemList.indexOf(item)
      if (index !== -1) {
        const obj = {
          label: this.label,
          data: '',
          key: Date.now()
        }
        this.itemList.splice(index + 1, 0, obj)
        this.updateItemList()
      }
    },
    remove (item) {
      const index = this.itemList.indexOf(item)
      if (index !== -1) {
        this.itemList.splice(index, 1)
      }
      this.updateItemList()
    },
    up (item) {
      const index = this.itemList.indexOf(item)
      if (index !== -1) {
        const temp = this.itemList[index]
        this.$set(this.itemList, index, this.itemList[index - 1])
        this.$set(this.itemList, index - 1, temp)
      }
      this.updateItemList()
    },
    down (item) {
      const index = this.itemList.indexOf(item)
      if (index !== -1) {
        const temp = this.itemList[index]
        this.$set(this.itemList, index, this.itemList[index + 1])
        this.$set(this.itemList, index + 1, temp)
      }
      this.updateItemList()
    },
    initItem (item, index, length) {
      const obj = {
        label: this.label,
        data: item,
        key: Date.now()
      }
      this.updateItemStatus(obj, index, length)
      return obj
    },
    updateItemList () {
      this.itemList.forEach((cur, index, arr) => {
        this.updateItemStatus(cur, index, arr.length)
      })
    },
    updateItemStatus(item, index, length) {
      this.$set(item, 'required', length <= this.minItems)
      this.$set(item, 'disableRemove', length <= this.minItems)
      this.$set(item, 'disableUp', index === 0)
      this.$set(item, 'disableDown', index === length - 1)
    },
    addNewItem () {
      const obj = this.initItem('', 0, 1)
      this.$set(this.itemList, 0, obj)
    },
    getData () {
      // 父组件调用此方法获取数组
      return this.itemList.map(cur => {
        return cur.data
      })
    }
  },
  created () {
    if (this.minItems > 0) {
      this.showAddButton = false
    }
    this.itemList = this.initArray.map((cur, index, arr) => this.initItem(cur, index, arr.length))
  }
}
</script>

<style lang="less" scoped>
.container {
  border: 1px solid #ebebeb;
  border-radius: 7px;
  padding: 20px 10px;
}
.add-new-button {
  margin-top: 20px;
}
.title {
  font-weight: 700;
  font-size: 20px;
  color: black;
}
.el-divider--horizontal {
  margin: 0;
}
</style>
