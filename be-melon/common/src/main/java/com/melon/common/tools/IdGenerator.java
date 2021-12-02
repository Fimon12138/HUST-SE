package com.melon.common.tools;

/**
 * 全局唯一id生成器
 * Snowflake算法
 *
 * @author: Fimon
 **/

public class IdGenerator {
  // 基准值
  private static final long epoch = 1596211200000L;

  private static final long workerIdBits = 5L;

  private static final long datacenterIdBits = 5L;

  private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);

  private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

  private static final long sequenceBits = 12L;

  private static final long workerIdShift = sequenceBits;

  private static final long datacenterShift = sequenceBits + workerIdBits;

  private static final long timestampShift = sequenceBits + workerIdBits + datacenterIdBits;

  private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

  // 私有变量
  private long workerId;

  private long dataCenterId;

  private long sequence;

  private long lastTimestamp;

  public IdGenerator(long dataCenterId, long workerId) {
    this.dataCenterId = dataCenterId;
    this.workerId = workerId;

    this.sequence = 0L;
    this.lastTimestamp = -1L;
  }
  

  /** 
  * 生成一个id
  * 
  * @param: [] 
  * @return: java.lang.String 
  * @author: Fimon
  */
  public synchronized String nextId() {

    long timestamp = System.currentTimeMillis();

    // 时钟回拨
    if (timestamp < lastTimestamp) {
      throw new IllegalStateException("Clock moved backwards, refuse to generate id.");
    }

    // 同一毫秒内并发
    if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) & sequenceMask;
      if (sequence == 0) {
        timestamp = untilNextMillis(lastTimestamp);
      }
    } else {
      // 下一毫秒重置sequence为0
      sequence = 0L;
    }

    lastTimestamp = timestamp;

    long id = ((timestamp - epoch) << timestampShift) | (dataCenterId << datacenterShift) | (workerId << workerIdShift)
      | sequence;

    return String.valueOf(id);
  }

  private static long untilNextMillis(long lastTimestamp) {
    long timestamp = timeGen();
    while (timestamp == lastTimestamp) {
      timestamp = timeGen();
    }
    return timestamp;
  }

  private static long timeGen() {
    return System.currentTimeMillis();
  }

}
