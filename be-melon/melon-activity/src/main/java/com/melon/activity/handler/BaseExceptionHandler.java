package com.melon.activity.handler;

import com.melon.common.pojo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 默认异常拦截处理器
 *
 * @author: Fimon
 **/
@RestControllerAdvice
@Controller
public class BaseExceptionHandler {

  @ExceptionHandler(BindException.class)
  public Response bindExceptionHandler(BindException e){
    return Response.error("参数错误：" + e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response paramsErrorHandler(MethodArgumentNotValidException e) {
    return Response.error("参数错误：" + e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
  }


}
