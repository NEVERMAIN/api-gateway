package io.github.NEVERMAIN.gateway.center.infrastructure.adapter.repository;

import io.github.NEVERMAIN.gateway.center.domain.register.adapter.repository.IRegisterManageRepository;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceMethodEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationSystemEntity;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.IApplicationInterfaceDao;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.IApplicationInterfaceMethodDao;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.IApplicationSystemDao;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.ApplicationInterface;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.ApplicationInterfaceMethod;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.ApplicationSystem;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class RegisterManageRepository implements IRegisterManageRepository {

    @Resource
    private IApplicationSystemDao applicationSystemDao;

    @Resource
    private IApplicationInterfaceDao applicationInterfaceDao;

    @Resource
    private IApplicationInterfaceMethodDao applicationInterfaceMethodDao;

    @Override
    public Boolean registerApplication(ApplicationSystemEntity applicationSystemEntity) {

        // 1.转换持久层数据
        ApplicationSystem applicationSystem = new ApplicationSystem();
        applicationSystem.setSystemId(applicationSystemEntity.getSystemId());
        applicationSystem.setSystemName(applicationSystemEntity.getSystemName());
        applicationSystem.setSystemType(applicationSystemEntity.getSystemType());
        applicationSystem.setSystemRegistry(applicationSystemEntity.getSystemRegistry());

        // 2.保存
        return applicationSystemDao.insert(applicationSystem);
    }

    @Override
    public Boolean registerApplicationInterface(ApplicationInterfaceEntity applicationInterfaceEntity) {

        // 1.转换持久层数据
        ApplicationInterface applicationInterface = new ApplicationInterface();
        applicationInterface.setSystemId(applicationInterfaceEntity.getSystemId());
        applicationInterface.setInterfaceId(applicationInterfaceEntity.getInterfaceId());
        applicationInterface.setInterfaceName(applicationInterfaceEntity.getInterfaceName());
        applicationInterface.setInterfaceVersion(applicationInterfaceEntity.getInterfaceVersion());

        // 2.保存
        return applicationInterfaceDao.insert(applicationInterface);
    }

    @Override
    public Boolean registerApplicationInterfaceMethod(ApplicationInterfaceMethodEntity applicationInterfaceMethodEntity) {

        // 1.转换持久层数据
        ApplicationInterfaceMethod applicationInterfaceMethod = new ApplicationInterfaceMethod();
        applicationInterfaceMethod.setSystemId(applicationInterfaceMethodEntity.getSystemId());
        applicationInterfaceMethod.setInterfaceId(applicationInterfaceMethodEntity.getInterfaceId());
        applicationInterfaceMethod.setMethodId(applicationInterfaceMethodEntity.getMethodId());
        applicationInterfaceMethod.setMethodName(applicationInterfaceMethodEntity.getMethodName());
        applicationInterfaceMethod.setParameterType(applicationInterfaceMethodEntity.getParameterType());
        applicationInterfaceMethod.setUri(applicationInterfaceMethodEntity.getUri());
        applicationInterfaceMethod.setAuth(applicationInterfaceMethodEntity.getAuth());
        applicationInterfaceMethod.setHttpCommandType(applicationInterfaceMethodEntity.getHttpCommandType());

        // 2.保存
        return applicationInterfaceMethodDao.insert(applicationInterfaceMethod);

    }
}
