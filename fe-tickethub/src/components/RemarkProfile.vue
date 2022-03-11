<template>
  <div class="container-remark-profile">
    <img :src="comment.logo" alt="error" class="logo">
    <div class="comment-text">
      <p class="comment">{{ comment.commentText }}</p>
      <span>《 {{ comment.name }} 》</span>
    </div>

    <div class="remark-profile-mask"></div>
  </div>
</template>

<script>
export default {
  props: {
    comment: {
      type: Object,
      required: true
    }
  }
}
</script>

<style lang="less" scoped>
.container-remark-profile {
  width: 90%;
  height: 80px;

  background-color: white;
  box-shadow: 0 0 5px #dddddd;

  position: relative;
  display: flex;
  flex-direction: row;
}

.logo {
  height: 80px;
  width: 240px;
}

.comment-text {
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

.remark-profile-mask {
  border-style: solid;
  border-width: 80px 60px 0 180px;
  border-color: transparent white transparent transparent;

  width: 0;
  height: 0;

  position: absolute;
  top: 0;
  left: 0;

  z-index: 1;
}
</style>
