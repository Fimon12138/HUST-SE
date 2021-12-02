package com.melon.common.handler.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 文件 url 数组和字符串之间的转换
 *
 * @author: Fimon
 **/
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class FileTypeHandler implements TypeHandler<List<String>> {

  @Override
  public void setParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
    String fileStr = String.join(",", strings);
    preparedStatement.setString(i, fileStr);
  }

  @Override
  public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
    String fileStr = resultSet.getString(s);
    String[] fileList = fileStr == null ? null : fileStr.split(",");
    return fileList == null ? null : Arrays.asList(fileList);
  }

  @Override
  public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
    String fileStr = resultSet.getString(i);
    String[] fileList = fileStr == null ? null : fileStr.split(",");
    return fileList == null ? null : Arrays.asList(fileList);
  }

  @Override
  public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
    String fileStr = callableStatement.getString(i);
    String[] fileList = fileStr == null ? null : fileStr.split(",");
    return fileList == null ? null : Arrays.asList(fileList);
  }

}
