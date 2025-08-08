package io.github.NEVERMAIN.gateway.center.infrastructure.adapter.repository;

import io.github.NEVERMAIN.gateway.center.domain.manage.adapter.repository.IConfigManageRepository;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.*;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.*;
import io.github.NEVERMAIN.gateway.center.infrastructure.dao.po.*;
import io.github.NEVERMAIN.gateway.center.infrastructure.event.Publisher;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 配置管理仓储实现
 */
@Repository
public class ConfigManageRepository implements IConfigManageRepository {

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

    @Resource
    private Publisher publisher;


    @Override
    public List<GatewayServerVO> queryGatewayServerList() {
        // 1. 查询所有网关服务节点信息
        List<GatewayServer> gatewayServerList = gatewayServerDao.queryGatewayServerList();

        // 转换数据
        ArrayList<GatewayServerVO> gatewayServerVOS = new ArrayList<>(gatewayServerList.size());
        gatewayServerList.forEach(gatewayServer -> {
            GatewayServerVO gatewayServerVO = new GatewayServerVO();
            gatewayServerVO.setGroupId(gatewayServer.getGroupId());
            gatewayServerVO.setGroupName(gatewayServer.getGroupName());

            gatewayServerVOS.add(gatewayServerVO);
        });

        // 3.返回数据
        return gatewayServerVOS;

    }

    @Override
    public GatewayServerDetailVO queryGatewayServerDetail(String gatewayId, String gatewayAddress) {
        // 1. 查询网关服务节点信息
        GatewayServerDetail req = new GatewayServerDetail();
        req.setGatewayId(gatewayId);
        req.setGatewayAddress(gatewayAddress);
        GatewayServerDetail gatewayServerDetail = gatewayServerDetailDao.queryGatewayServerDetail(req);
        if (null == gatewayServerDetail) return null;
        // 2.转换数据
        GatewayServerDetailVO gatewayServerDetailVO = new GatewayServerDetailVO();
        gatewayServerDetailVO.setGatewayId(gatewayServerDetail.getGatewayId());
        gatewayServerDetailVO.setGatewayName(gatewayServerDetail.getGatewayName());
        gatewayServerDetailVO.setGatewayAddress(gatewayServerDetail.getGatewayAddress());
        gatewayServerDetailVO.setStatus(gatewayServerDetail.getStatus());
        // 3.返回结果
        return gatewayServerDetailVO;
    }

    @Override
    public Boolean registerGatewayServerNode(String groupId, String gatewayId, String gatewayName, String gatewayAddress) {
        GatewayServerDetail req = new GatewayServerDetail();
        req.setGroupId(groupId);
        req.setGatewayId(gatewayId);
        req.setGatewayAddress(gatewayAddress);
        req.setGatewayName(gatewayName);
        req.setStatus(GatewayStatusTypeEnum.AVAILABLE.getCode());
        return gatewayServerDetailDao.insert(req);
    }

    @Override
    public Boolean updateGatewayStatus(String gatewayId, String gatewayAddress, GatewayStatusTypeEnum gatewayStatusTypeEnum) {

        GatewayServerDetail req = new GatewayServerDetail();
        req.setGatewayId(gatewayId);
        req.setGatewayAddress(gatewayAddress);
        req.setStatus(gatewayStatusTypeEnum.getCode());
        // 2.更新网关节点状态
        return gatewayServerDetailDao.updateGatewayStatus(req);

    }

    @Override
    public List<String> queryGatewayDistributionSystemIdList(String gatewayId) {
        List<String> systemIdList = gatewayDistributionDao.queryGatewayDistributionSystemIdList(gatewayId);
        if (null == systemIdList) return new ArrayList<>();
        return systemIdList;
    }

    @Override
    public List<ApplicationSystemVO> queryApplicationSystemList(List<String> systemIdList) {
        // 1. 查询系统列表
        List<ApplicationSystem> applicationSystemList = applicationSystemDao.queryApplicationSystemList(systemIdList);

        // 2.转换实体数据
        ArrayList<ApplicationSystemVO> applicationSystemVOList = new ArrayList<>(applicationSystemList.size());
        applicationSystemList.forEach(applicationSystem -> {
            ApplicationSystemVO applicationSystemVO = new ApplicationSystemVO();
            applicationSystemVO.setSystemId(applicationSystem.getSystemId());
            applicationSystemVO.setSystemName(applicationSystem.getSystemName());
            applicationSystemVO.setSystemType(applicationSystem.getSystemType());
            applicationSystemVO.setSystemRegistry(applicationSystem.getSystemRegistry());
            applicationSystemVOList.add(applicationSystemVO);
        });

        // 3.返回结果
        return applicationSystemVOList;
    }

    @Override
    public List<ApplicationInterfaceVO> queryApplicationInterfaceList(String systemId) {
        // 1. 查询接口列表
        List<ApplicationInterface> applicationInterfaceList = applicationInterfaceDao.queryApplicationInterfaceList(systemId);

        // 2.转换实体数据
        ArrayList<ApplicationInterfaceVO> applicationInterfaceVOS = new ArrayList<>(applicationInterfaceList.size());
        applicationInterfaceList.forEach(applicationInterface -> {
            ApplicationInterfaceVO applicationInterfaceVO = new ApplicationInterfaceVO();
            applicationInterfaceVO.setSystemId(applicationInterface.getSystemId());
            applicationInterfaceVO.setInterfaceId(applicationInterface.getInterfaceId());
            applicationInterfaceVO.setInterfaceName(applicationInterface.getInterfaceName());
            applicationInterfaceVO.setInterfaceVersion(applicationInterface.getInterfaceVersion());

            applicationInterfaceVOS.add(applicationInterfaceVO);
        });

        // 3.返回结果
        return applicationInterfaceVOS;
    }

    @Override
    public List<ApplicationInterfaceMethodVO> queryApplicationInterfaceMethodList(String systemId, String interfaceId) {
        // 1.创建请求
        ApplicationInterfaceMethod req = new ApplicationInterfaceMethod();
        req.setSystemId(systemId);
        req.setInterfaceId(interfaceId);

        // 2.查询接口下的方法详情列表
        List<ApplicationInterfaceMethod> applicationInterfaceMethodList = applicationInterfaceMethodDao.queryApplicationInterfaceMethodList(req);

        // 3.转换数据
        ArrayList<ApplicationInterfaceMethodVO> list = new ArrayList<>(applicationInterfaceMethodList.size());
        applicationInterfaceMethodList.forEach(applicationInterfaceMethod -> {

            ApplicationInterfaceMethodVO applicationInterfaceMethodVO = new ApplicationInterfaceMethodVO();
            applicationInterfaceMethodVO.setSystemId(applicationInterfaceMethod.getSystemId());
            applicationInterfaceMethodVO.setInterfaceId(applicationInterfaceMethod.getInterfaceId());
            applicationInterfaceMethodVO.setMethodId(applicationInterfaceMethod.getMethodId());
            applicationInterfaceMethodVO.setMethodName(applicationInterfaceMethod.getMethodName());
            applicationInterfaceMethodVO.setParameterType(applicationInterfaceMethod.getParameterType());
            applicationInterfaceMethodVO.setUri(applicationInterfaceMethod.getUri());
            applicationInterfaceMethodVO.setAuth(applicationInterfaceMethod.getAuth());
            applicationInterfaceMethodVO.setHttpCommandType(applicationInterfaceMethod.getHttpCommandType());

            list.add(applicationInterfaceMethodVO);

        });

        return list;
    }

    @Override
    public List<ApplicationInterfaceVO> queryApplicationInterfaceListBySystemIds(List<String> systemIdList) {
        // 1.查询接口列表
        List<ApplicationInterface> applicationInterfaceList =
                applicationInterfaceDao.queryApplicationInterfaceListBySystemIds(systemIdList);
        // 2.转换数据
        return applicationInterfaceList.stream().map(applicationInterface -> {
            ApplicationInterfaceVO applicationInterfaceVO = new ApplicationInterfaceVO();
            applicationInterfaceVO.setSystemId(applicationInterface.getSystemId());
            applicationInterfaceVO.setInterfaceId(applicationInterface.getInterfaceId());
            applicationInterfaceVO.setInterfaceName(applicationInterface.getInterfaceName());
            applicationInterfaceVO.setInterfaceVersion(applicationInterface.getInterfaceVersion());
            return applicationInterfaceVO;
        }).toList();
    }

    @Override
    public List<ApplicationInterfaceMethodVO> queryApplicationInterfaceMethodListBySystemIds(List<String> systemIdList) {
        return List.of();
    }

    @Override
    public Boolean registerEvent(String systemId) {
        try {
            // 1.查询系统对应的网关信息
            String gatewayId = gatewayDistributionDao.queryGatewayDistribution(systemId);
            // 2.发送消息
            publisher.pushMessage(gatewayId, systemId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<GatewayServerDetailVO> queryGatewayServerDetailList() {
        // 1.查询网关节点详情列表
        List<GatewayServerDetail> gatewayServerDetailList = gatewayServerDetailDao.queryGatewayServerDetailList();
        // 2.转换数据
        ArrayList<GatewayServerDetailVO> gatewayServerDetailVOList = new ArrayList<>(gatewayServerDetailList.size());
        gatewayServerDetailList.forEach(gatewayServerDetail -> {
            GatewayServerDetailVO gatewayServerDetailVO = new GatewayServerDetailVO();
            gatewayServerDetailVO.setGroupId(gatewayServerDetail.getGroupId());
            gatewayServerDetailVO.setGatewayId(gatewayServerDetail.getGatewayId());
            gatewayServerDetailVO.setGatewayName(gatewayServerDetail.getGatewayName());
            gatewayServerDetailVO.setGatewayAddress(gatewayServerDetail.getGatewayAddress());
            gatewayServerDetailVO.setStatus(gatewayServerDetail.getStatus());

            gatewayServerDetailVOList.add(gatewayServerDetailVO);
        });
        return gatewayServerDetailVOList;
    }
}
