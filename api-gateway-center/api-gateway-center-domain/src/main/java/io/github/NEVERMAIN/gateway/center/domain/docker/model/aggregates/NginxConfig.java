package io.github.NEVERMAIN.gateway.center.domain.docker.model.aggregates;

import io.github.NEVERMAIN.gateway.center.domain.docker.model.vo.LocationVO;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.vo.UpstreamVO;

import java.util.List;

/**
 * @description: Nginx 负载配置信息
 */
public class NginxConfig {

    private String applicationName;

    private String nginxName;

    private String localNginxPath;

    private String remoteNginxPath;

    private List<UpstreamVO> upstreamList;

    private List<LocationVO> locationList;

    private String NGINX_CONF_FILE_PATH = "";

    public NginxConfig(String applicationName, String localNginxPath, String nginxName, String remoteNginxPath, List<UpstreamVO> upstreamList, List<LocationVO> locationList) {
        this.applicationName = applicationName;
        this.localNginxPath = localNginxPath;
        this.nginxName = nginxName;
        this.remoteNginxPath = remoteNginxPath;
        this.upstreamList = upstreamList;
        this.locationList = locationList;
    }


    public NginxConfig(List<UpstreamVO> upstreamList, List<LocationVO> locationList){
        this.applicationName = "api-gateway-center";
        this.nginxName = "Nginx";
        this.localNginxPath = NGINX_CONF_FILE_PATH;
        this.remoteNginxPath = "/etc/nginx/";
        this.upstreamList = upstreamList;
        this.locationList = locationList;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNginxName() {
        return nginxName;
    }

    public void setNginxName(String nginxName) {
        this.nginxName = nginxName;
    }

    public String getLocalNginxPath() {
        return localNginxPath;
    }

    public void setLocalNginxPath(String localNginxPath) {
        this.localNginxPath = localNginxPath;
    }

    public String getRemoteNginxPath() {
        return remoteNginxPath;
    }

    public void setRemoteNginxPath(String remoteNginxPath) {
        this.remoteNginxPath = remoteNginxPath;
    }

    public List<UpstreamVO> getUpstreamList() {
        return upstreamList;
    }

    public void setUpstreamList(List<UpstreamVO> upstreamList) {
        this.upstreamList = upstreamList;
    }

    public String getNGINX_CONF_FILE_PATH() {
        return NGINX_CONF_FILE_PATH;
    }

    public void setNGINX_CONF_FILE_PATH(String NGINX_CONF_FILE_PATH) {
        this.NGINX_CONF_FILE_PATH = NGINX_CONF_FILE_PATH;
    }

    public List<LocationVO> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationVO> locationList) {
        this.locationList = locationList;
    }
}
