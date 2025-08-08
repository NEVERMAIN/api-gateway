package io.github.NEVERMAIN.gateway.center.infrastructure.adapter.repository;

import io.github.NEVERMAIN.gateway.center.domain.data.adapter.repository.IDataOperationManageRepository;
import io.github.NEVERMAIN.gateway.center.domain.data.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.*;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.*;
import io.github.NEVERMAIN.gateway.center.types.common.OperationRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class DataOperationManageRepository implements IDataOperationManageRepository {

    @Resource
    private IGatewayServerDao gatewayServerDao;

    @Resource
    private IGatewayServerDetailDao gatewayServerDetailDao;

    @Resource
    private IGatewayDistributionDao gatewayDistributionDao;

    @Resource
    private IApplicationSystemDao applicationSystemDao;

    @Resource
    private IApplicationInterfaceDao applicationInterfaceDao;

    @Resource
    private IApplicationInterfaceMethodDao applicationInterfaceMethodDao;


    @Override
    public List<GatewayServerDataVO> queryGatewayServerListByPage(OperationRequest<String> req) {

        List<GatewayServer> gatewayServerList = gatewayServerDao.queryGatewayServerListByPage(req);

        ArrayList<GatewayServerDataVO> gatewayServerDataVOS = new ArrayList<>(gatewayServerList.size());
        gatewayServerList.forEach(gatewayServer -> {
            GatewayServerDataVO gatewayServerDataVO = new GatewayServerDataVO();
            gatewayServerDataVO.setId(gatewayServer.getId());
            gatewayServerDataVO.setGroupId(gatewayServer.getGroupId());
            gatewayServerDataVO.setGroupName(gatewayServer.getGroupName());

            gatewayServerDataVOS.add(gatewayServerDataVO);
        });

        return gatewayServerDataVOS;
    }

    @Override
    public int queryGatewayServerListCountByPage(OperationRequest<String> req) {
        return gatewayServerDao.queryGatewayServerListCountByPage(req);
    }

    @Override
    public List<GatewayServerDetailDataVO> queryGatewayServerDetailListByPage(OperationRequest<GatewayServerDetailDataVO> req) {

        List<GatewayServerDetail> gatewayServerDetailList = gatewayServerDetailDao.queryGatewayServerDetailListByPage(req);

        ArrayList<GatewayServerDetailDataVO> gatewayServerDetailDataVOS = new ArrayList<>(gatewayServerDetailList.size());
        gatewayServerDetailList.forEach(gatewayServerDetail -> {
            GatewayServerDetailDataVO gatewayServerDetailDataVO = new GatewayServerDetailDataVO();
            gatewayServerDetailDataVO.setId(gatewayServerDetail.getId());
            gatewayServerDetailDataVO.setGroupId(gatewayServerDetail.getGroupId());
            gatewayServerDetailDataVO.setGatewayId(gatewayServerDetail.getGatewayId());
            gatewayServerDetailDataVO.setGatewayName(gatewayServerDetail.getGatewayName());
            gatewayServerDetailDataVO.setGatewayAddress(gatewayServerDetail.getGatewayAddress());
            gatewayServerDetailDataVO.setStatus(gatewayServerDetail.getStatus());
            gatewayServerDetailDataVO.setCreateTime(gatewayServerDetail.getCreateTime());
            gatewayServerDetailDataVO.setUpdateTime(gatewayServerDetail.getUpdateTime());

            gatewayServerDetailDataVOS.add(gatewayServerDetailDataVO);
        });
        return gatewayServerDetailDataVOS;
    }

    @Override
    public int queryGatewayServerDetailListCountByPage(OperationRequest<GatewayServerDetailDataVO> req) {
        return gatewayServerDetailDao.queryGatewayServerDetailListCountByPage(req);
    }

    @Override
    public List<GatewayDistributionDataVO> queryGatewayDistribution(OperationRequest<GatewayDistributionDataVO> req) {
        List<GatewayDistribution> gatewayDistributions = gatewayDistributionDao.queryGatewayDistributionListByPage(req);

        ArrayList<GatewayDistributionDataVO> gatewayDistributionDataVOList = new ArrayList<>(gatewayDistributions.size());
        gatewayDistributions.forEach(gatewayDistribution -> {
            GatewayDistributionDataVO gatewayDistributionDataVO = new GatewayDistributionDataVO();
            gatewayDistributionDataVO.setId(gatewayDistribution.getId());
            gatewayDistributionDataVO.setGroupId(gatewayDistribution.getGroupId());
            gatewayDistributionDataVO.setGatewayId(gatewayDistribution.getGatewayId());
            gatewayDistributionDataVO.setSystemId(gatewayDistribution.getSystemId());
            gatewayDistributionDataVO.setSystemName(gatewayDistribution.getSystemName());
            gatewayDistributionDataVO.setCreateTime(gatewayDistribution.getCreateTime());
            gatewayDistributionDataVO.setUpdateTime(gatewayDistribution.getUpdateTime());
            // 添加到列表中
            gatewayDistributionDataVOList.add(gatewayDistributionDataVO);
        });

        return gatewayDistributionDataVOList;
    }

    @Override
    public int queryGatewayDistributionListCountByPage(OperationRequest<GatewayDistributionDataVO> req) {
        return gatewayDistributionDao.queryGatewayDistributionListCountByPage(req);
    }

    @Override
    public List<ApplicationSystemDataVO> queryApplicationSystemListByPage(OperationRequest<ApplicationSystemDataVO> req) {

        // 1.查询数据
        List<ApplicationSystem> applicationSystemList = applicationSystemDao.queryApplicationSystemListByPage(req);
        // 2.转换数据
        ArrayList<ApplicationSystemDataVO> applicationSystemDataVOS = new ArrayList<>(applicationSystemList.size());
        applicationSystemList.forEach(applicationSystem -> {
            ApplicationSystemDataVO applicationSystemDataVO = new ApplicationSystemDataVO();
            applicationSystemDataVO.setSystemId(applicationSystem.getSystemId());
            applicationSystemDataVO.setSystemName(applicationSystem.getSystemName());
            applicationSystemDataVO.setSystemType(applicationSystem.getSystemType());
            applicationSystemDataVO.setSystemRegistry(applicationSystem.getSystemRegistry());

            applicationSystemDataVOS.add(applicationSystemDataVO);

        });
        return applicationSystemDataVOS;
    }

    @Override
    public int queryApplicationSystemCountByPage(OperationRequest<ApplicationSystemDataVO> req) {
        return applicationSystemDao.queryApplicationSystemCountByPage(req);
    }

    @Override
    public List<ApplicationInterfaceDataVO> queryApplicationInterfaceListByPage(OperationRequest<ApplicationInterfaceDataVO> req) {

        // 1.从数据库中查询数据
        List<ApplicationInterface> applicationInterfaceList = applicationInterfaceDao.queryApplicationInterfaceListByPage(req);
        // 2.实体数据转换
        ArrayList<ApplicationInterfaceDataVO> applicationInterfaceDataVOS = new ArrayList<>(applicationInterfaceList.size());
        applicationInterfaceList.forEach(applicationInterface -> {
            ApplicationInterfaceDataVO applicationInterfaceDataVO = new ApplicationInterfaceDataVO();
            applicationInterfaceDataVO.setSystemId(applicationInterface.getSystemId());
            applicationInterfaceDataVO.setInterfaceId(applicationInterface.getInterfaceId());
            applicationInterfaceDataVO.setInterfaceName(applicationInterface.getInterfaceName());
            applicationInterfaceDataVO.setInterfaceVersion(applicationInterface.getInterfaceVersion());
            // 添加到列表中
            applicationInterfaceDataVOS.add(applicationInterfaceDataVO);
        });

        return applicationInterfaceDataVOS;
    }

    @Override
    public int queryApplicationInterfaceListCountByPage(OperationRequest<ApplicationInterfaceDataVO> req) {
        return applicationInterfaceDao.queryApplicationInterfaceListCountByPage(req);
    }

    @Override
    public List<ApplicationInterfaceMethodDataVO> queryApplicationInterfaceMethodListByPage(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest) {

        List<ApplicationInterfaceMethod> applicationInterfaceMethodList = applicationInterfaceMethodDao.queryApplicationInterfaceMethodListByPage(operationRequest);
        ArrayList<ApplicationInterfaceMethodDataVO> applicationInterfaceMethodDataVOS = new ArrayList<>(applicationInterfaceMethodList.size());
        applicationInterfaceMethodList.forEach(applicationInterfaceMethod -> {
            ApplicationInterfaceMethodDataVO applicationInterfaceMethodDataVO = new ApplicationInterfaceMethodDataVO();
            applicationInterfaceMethodDataVO.setSystemId(applicationInterfaceMethod.getSystemId());
            applicationInterfaceMethodDataVO.setInterfaceId(applicationInterfaceMethod.getInterfaceId());
            applicationInterfaceMethodDataVO.setMethodId(applicationInterfaceMethod.getMethodId());
            applicationInterfaceMethodDataVO.setMethodName(applicationInterfaceMethod.getMethodName());
            applicationInterfaceMethodDataVO.setParameterType(applicationInterfaceMethod.getParameterType());
            applicationInterfaceMethodDataVO.setUri(applicationInterfaceMethod.getUri());
            applicationInterfaceMethodDataVO.setHttpCommandType(applicationInterfaceMethod.getHttpCommandType());
            applicationInterfaceMethodDataVO.setAuth(applicationInterfaceMethod.getAuth());
            applicationInterfaceMethodDataVOS.add(applicationInterfaceMethodDataVO);
        });
        return applicationInterfaceMethodDataVOS;
    }

    @Override
    public int queryApplicationInterfaceMethodListCountByPage(OperationRequest<ApplicationInterfaceMethodDataVO> operationRequest) {
        return applicationInterfaceMethodDao.queryApplicationInterfaceMethodListCountByPage(operationRequest);
    }
}
