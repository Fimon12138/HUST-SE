<template>
  <div class="container-remark-activity">
    <img v-if="direction" :src="comment.avatar" alt="error" class="user-avatar">
    <div class="comment-text">
      <p>{{ comment.commentText }}</p>
      <span>———— {{ comment.nickname }}</span>
    </div>
    <img v-if="!direction" :src="comment.avatar" alt="error" class="user-avatar">
  </div>
</template>

<script>
export default {
  props: {
    comment: {
      type: Object,
      required: true
    },
    index: {
      type: Number,
      required: true
    }
  },
  data () {
    return {
      direction: this.index % 2 === 0
    }
  }
}
</script>

<style lang="less" scoped>
.container-remark-activity {
  position: relative;
  min-height: 80px;
  width: 90%;

  display: flex;
  flex-direction: row;
  justify-content: right;

  box-shadow: 0 0 5px #dddddd;
}

.user-avatar {
  width: 60px;
  height: 60px;

  border-radius: 50%;

  margin-top: 10px;
  margin-left: 20px;
}

.comment-text {
  margin-left: 20px;
  padding: 10px;

  display: flex;
  flex-direction: column;

  font-family: 'NotoSerifJP-SemiBold';

  p {
    margin: 0;
    word-wrap: break-word;
  }

  span {
    text-align: right;
    margin-top: 10px;
  }
}
</style>
