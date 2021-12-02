const OSS = require('ali-oss')

export function client () {
  var client = new OSS({
    region: 'oss-cn-beijing', // 地域
    // endpoint:"oss-cn-hangzhou.aliyuncs.com",//访问域名
    accessKeyId: 'LTAI4G1te5TSnZasXy7cisio',
    accessKeySecret: 'eVc5Qg38xybe2fh95R6k4yhx15RNRw',
    bucket: 'cloudmarkdown' // oss上你的存储空间名称
    // secure: true
  })
  return client
}
