package com.melon.activity.pojo.activity;

import com.alibaba.fastjson.annotation.JSONField;
import com.melon.common.pojo.Location;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author: Fimon
 **/
@Data
public class ActivityResp extends Activity {

  private String id;

  private String userId;

  private List<String> fileList;

  private Integer registerNum;

  private Integer status;

  private Location location;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Timestamp uploadTimestamp;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Timestamp lastUpdateTimestamp;

  private Integer registerStatus;

}
