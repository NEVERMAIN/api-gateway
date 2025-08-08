package io.github.NEVERMAIN.gateway.center.domain.register.adapter.repository;


import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceMethodEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationSystemEntity;

public interface IRegisterManageRepository {

    Boolean registerApplication(ApplicationSystemEntity applicationSystemEntity);

    Boolean registerApplicationInterface(ApplicationInterfaceEntity applicationInterfaceEntity);

    Boolean registerApplicationInterfaceMethod(ApplicationInterfaceMethodEntity applicationInterfaceMethodEntity);
}
