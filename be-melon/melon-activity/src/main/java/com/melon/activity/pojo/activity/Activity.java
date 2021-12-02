package com.melon.activity.pojo.activity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author: Fimon
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

  @NotBlank(message = "title不能为空")
  private String title;

  private String content;

  @NotNull(message = "type不能为空")
  private Integer type;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "timeFrom不能为空")
  private Timestamp timeFrom;

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "timeTo不能为空")
  private Timestamp timeTo;

  private String participantDesc;

  private Integer participantNum;

  private Float fare;

}
