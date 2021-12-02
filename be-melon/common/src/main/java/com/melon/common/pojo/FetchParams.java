package com.melon.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

/**
 * @author: Fimon
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchParams {

  private Timestamp timestamp;

  private Integer page = 1;

  private Integer pageSize = 10;

  private String userId;

  @Pattern(regexp = "^POINT([\\+\\-]?[\\d]+(\\.[\\d]+)? [\\+\\-]?[\\d]+(\\.[\\d]+)?)$",
    message = "locationPoint格式错误")
  private String locationPoint;

  public Integer getOffset() {
    return pageSize * (page - 1);
  }
}
