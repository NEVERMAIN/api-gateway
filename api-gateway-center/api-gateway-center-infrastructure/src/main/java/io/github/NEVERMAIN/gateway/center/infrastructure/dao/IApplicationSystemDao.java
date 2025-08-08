package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.ApplicationSystemDataVO;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.ApplicationSystem;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IApplicationSystemDao {

    Boolean insert(ApplicationSystem applicationSystem);

    List<ApplicationSystem> queryApplicationSystemList(List<String> systemIdList);

    List<ApplicationSystem> queryApplicationSystemListByPage(OperationRequest<ApplicationSystemDataVO> req);

    int queryApplicationSystemCountByPage(OperationRequest<ApplicationSystemDataVO> req);
}
