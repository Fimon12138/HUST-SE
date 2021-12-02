package com.melon.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 位置信息类
 *
 * @author: Fimon
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

  private String id;

  private String point;

  private String desc;

  private Float precision;


}
