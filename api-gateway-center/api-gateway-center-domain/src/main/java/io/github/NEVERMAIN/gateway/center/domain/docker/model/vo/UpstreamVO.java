package io.github.NEVERMAIN.gateway.center.domain.docker.model.vo;

import java.util.List;

/**
 * 设定负载均衡的服务器列表
 */
public class UpstreamVO {

    /**
     * 名称
     */
    private String name;
    /**
     * 复杂策略 least_conn ip_hash
     */
    private String strategy;
    /**
     * 服务列表 192.168.198.138:9001
     */
    private List<String> servers;

    public UpstreamVO() {
    }

    public UpstreamVO(String name, String strategy, List<String> servers) {
        this.name = name;
        this.strategy = strategy;
        this.servers = servers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
