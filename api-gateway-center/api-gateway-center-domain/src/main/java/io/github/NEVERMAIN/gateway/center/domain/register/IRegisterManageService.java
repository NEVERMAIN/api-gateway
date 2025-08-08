package io.github.NEVERMAIN.gateway.center.domain.register;


import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceMethodEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationSystemEntity;

public interface IRegisterManageService {

    Boolean registerApplication(ApplicationSystemEntity applicationSystemEntity);

    Boolean registerApplicationInterface(ApplicationInterfaceEntity applicationInterfaceEntity);

    Boolean registerApplicationInterfaceMethod(ApplicationInterfaceMethodEntity applicationInterfaceMethodEntity);


}
