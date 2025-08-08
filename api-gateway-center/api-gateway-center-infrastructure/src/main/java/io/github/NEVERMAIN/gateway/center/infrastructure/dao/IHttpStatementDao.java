package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.HttpStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHttpStatementDao {

    List<HttpStatement> queryHttpStatementList();

}
