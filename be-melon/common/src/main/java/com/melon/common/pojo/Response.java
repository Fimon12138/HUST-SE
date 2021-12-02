package com.melon.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回结果类
 *
 * @author: Fimon
 **/
@Data
@AllArgsConstructor
public class Response {

  private int code;

  private String message;

  private Object results;

  public static Response success(String msg) {
    return new Response(200, msg, null);
  }

  public static Response success(String msg, Object obj) {
    return new Response(200, msg, obj);
  }

  public static Response error(String msg) {
    return new Response(500, msg, null);
  }

  public static Response error(String msg, Object obj) {
    return new Response(500, msg, obj);
  }

  public static Response systemError() {
    return new Response(501, "系统异常", null);
  }

}
