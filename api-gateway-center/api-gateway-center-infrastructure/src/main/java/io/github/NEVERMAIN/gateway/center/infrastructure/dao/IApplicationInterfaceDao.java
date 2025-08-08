package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.ApplicationInterfaceDataVO;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.ApplicationInterface;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IApplicationInterfaceDao {

    Boolean insert(ApplicationInterface applicationInterface);

    List<ApplicationInterface> queryApplicationInterfaceList(String systemId);

    List<ApplicationInterface> queryApplicationInterfaceListBySystemIds(List<String> systemIdList);

    List<ApplicationInterface> queryApplicationInterfaceListByPage(OperationRequest<ApplicationInterfaceDataVO> req);

    int queryApplicationInterfaceListCountByPage(OperationRequest<ApplicationInterfaceDataVO> req);
}
