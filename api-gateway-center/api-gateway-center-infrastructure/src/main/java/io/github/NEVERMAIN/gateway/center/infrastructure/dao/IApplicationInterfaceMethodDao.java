package io.github.NEVERMAIN.gateway.center.infrastructure.dao;

import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.ApplicationInterfaceMethodDataVO;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.ApplicationInterfaceMethod;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IApplicationInterfaceMethodDao {

    Boolean insert(ApplicationInterfaceMethod applicationInterfaceMethod);


    List<ApplicationInterfaceMethod> queryApplicationInterfaceMethodList(ApplicationInterfaceMethod req);


    int queryApplicationInterfaceMethodListCountByPage(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest);

    List<ApplicationInterfaceMethod> queryApplicationInterfaceMethodListByPage(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest);
}
