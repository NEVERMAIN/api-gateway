package io.github.NEVERMAIN.gateway.center.domain.register.service;

import io.github.NEVERMAIN.gateway.center.domain.register.IRegisterManageService;
import io.github.NEVERMAIN.gateway.center.domain.register.adapter.repository.IRegisterManageRepository;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationInterfaceMethodEntity;
import io.github.NEVERMAIN.gateway.center.domain.register.model.entity.ApplicationSystemEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RegisterManageService implements IRegisterManageService {

    @Resource
    private IRegisterManageRepository registerManageRepository;

    @Override
    public Boolean registerApplication(ApplicationSystemEntity applicationSystemEntity) {
        return registerManageRepository.registerApplication(applicationSystemEntity);
    }

    @Override
    public Boolean registerApplicationInterface(ApplicationInterfaceEntity applicationInterfaceEntity) {
        return registerManageRepository.registerApplicationInterface(applicationInterfaceEntity);
    }

    @Override
    public Boolean registerApplicationInterfaceMethod(ApplicationInterfaceMethodEntity applicationInterfaceMethodEntity) {
        return registerManageRepository.registerApplicationInterfaceMethod(applicationInterfaceMethodEntity);
    }
}
