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

        ApplicationSystem applicationSystem = applicationSystemDao.queryApplicationSystemBySystemId(applicationSystemEntity.getSystemId());
        if (applicationSystem == null) {
            applicationSystem = new ApplicationSystem();
            applicationSystem.setSystemId(applicationSystemEntity.getSystemId());
            applicationSystem.setSystemName(applicationSystemEntity.getSystemName());
            applicationSystem.setSystemRegistry(applicationSystemEntity.getSystemRegistry());
            applicationSystem.setSystemAddress(applicationSystemEntity.getSystemAddress());
            // 2.保存
            return applicationSystemDao.insert(applicationSystem);
        } else {
            // 直接修改查询到的对象，而不是重新创建
            applicationSystem.setSystemName(applicationSystemEntity.getSystemName());
            applicationSystem.setSystemRegistry(applicationSystemEntity.getSystemRegistry());
            applicationSystem.setSystemAddress(applicationSystemEntity.getSystemAddress());
            // 3.更新
            return applicationSystemDao.update(applicationSystem);
        }
    }

    @Override
    public Boolean registerApplicationInterface(ApplicationInterfaceEntity applicationInterfaceEntity) {

        ApplicationInterface req = new ApplicationInterface();
        req.setSystemId(applicationInterfaceEntity.getSystemId());
        req.setInterfaceId(applicationInterfaceEntity.getInterfaceId());

        ApplicationInterface applicationInterface = applicationInterfaceDao.queryApplicationInterface(req);
        if (applicationInterface == null) {

            applicationInterface = new ApplicationInterface();
            applicationInterface.setSystemId(applicationInterfaceEntity.getSystemId());
            applicationInterface.setInterfaceId(applicationInterfaceEntity.getInterfaceId());
            applicationInterface.setInterfaceName(applicationInterfaceEntity.getInterfaceName());
            applicationInterface.setProtocolType(applicationInterfaceEntity.getProtocolType());
            applicationInterface.setInterfaceVersion(applicationInterfaceEntity.getInterfaceVersion());
            // 2.保存
            return applicationInterfaceDao.insert(applicationInterface);
        } else {
            applicationInterface.setInterfaceName(applicationInterfaceEntity.getInterfaceName());
            applicationInterface.setProtocolType(applicationInterfaceEntity.getProtocolType());
            applicationInterface.setInterfaceVersion(applicationInterfaceEntity.getInterfaceVersion());
            // 3.更新
            return applicationInterfaceDao.update(applicationInterface);
        }
    }

    @Override
    public Boolean registerApplicationInterfaceMethod(ApplicationInterfaceMethodEntity applicationInterfaceMethodEntity) {

        // 1.判断服务接口是否存在
        ApplicationInterfaceMethod req = new ApplicationInterfaceMethod();
        req.setSystemId(applicationInterfaceMethodEntity.getSystemId());
        req.setInterfaceId(applicationInterfaceMethodEntity.getInterfaceId());
        req.setMethodId(applicationInterfaceMethodEntity.getMethodId());

        ApplicationInterfaceMethod applicationInterfaceMethod = applicationInterfaceMethodDao.queryApplicationInterfaceMethod(req);
        if (applicationInterfaceMethod == null) {
             applicationInterfaceMethod = new ApplicationInterfaceMethod();
            applicationInterfaceMethod.setSystemId(applicationInterfaceMethodEntity.getSystemId());
            applicationInterfaceMethod.setInterfaceId(applicationInterfaceMethodEntity.getInterfaceId());
            applicationInterfaceMethod.setMethodId(applicationInterfaceMethodEntity.getMethodId());
            applicationInterfaceMethod.setMethodName(applicationInterfaceMethodEntity.getMethodName());
            applicationInterfaceMethod.setParameterType(applicationInterfaceMethodEntity.getParameterType());
            applicationInterfaceMethod.setParameterName(applicationInterfaceMethodEntity.getParameterName());
            applicationInterfaceMethod.setUri(applicationInterfaceMethodEntity.getUri());
            applicationInterfaceMethod.setAuth(applicationInterfaceMethodEntity.getAuth());
            applicationInterfaceMethod.setHttpCommandType(applicationInterfaceMethodEntity.getHttpCommandType());

            // 2.保存
            return applicationInterfaceMethodDao.insert(applicationInterfaceMethod);
        }else{

            applicationInterfaceMethod.setMethodName(applicationInterfaceMethodEntity.getMethodName());
            applicationInterfaceMethod.setParameterType(applicationInterfaceMethodEntity.getParameterType());
            applicationInterfaceMethod.setParameterName(applicationInterfaceMethodEntity.getParameterName());
            applicationInterfaceMethod.setUri(applicationInterfaceMethodEntity.getUri());
            applicationInterfaceMethod.setAuth(applicationInterfaceMethodEntity.getAuth());
            applicationInterfaceMethod.setHttpCommandType(applicationInterfaceMethodEntity.getHttpCommandType());

            return applicationInterfaceMethodDao.update(applicationInterfaceMethod);
        }
    }
}
