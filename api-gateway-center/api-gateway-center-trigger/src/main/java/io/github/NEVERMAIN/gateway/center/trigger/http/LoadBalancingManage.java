package io.github.NEVERMAIN.gateway.center.trigger.http;

import com.alibaba.fastjson2.JSON;
import io.github.NEVERMAIN.gateway.center.domain.docker.ILoadBalancingService;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.aggregates.NginxConfig;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.vo.LocationVO;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.vo.UpstreamVO;
import jakarta.annotation.Resource;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/wg/admin/load")
public class LoadBalancingManage {

    private Logger logger = LoggerFactory.getLogger(LoadBalancingManage.class);

    @Resource
    private ILoadBalancingService loadBalancingService;

    /**
     * 刷新 Nginx 配置文件
     * http://localhost:8080/wg/admin/load/updateNginxConfig
     */
    @GetMapping(value = "updateNginxConfig", produces = "application/json;charset=UTF-8")
    public void updateNginxConfig(){

        ArrayList<UpstreamVO> upstreamList = new ArrayList<>();
        upstreamList.add(new UpstreamVO("api01", "least_conn;", Arrays.asList("192.168.1.102:9001;", "192.168.1.102:9002;")));
        upstreamList.add(new UpstreamVO("api02", "least_conn;", Arrays.asList("192.168.1.102:9003;")));

        List<LocationVO> locationList = new ArrayList<>();
        locationList.add(new LocationVO("/api01/", "http://api01;"));
        locationList.add(new LocationVO("/api02/", "http://api02;"));

        NginxConfig nginxConfig = new NginxConfig(upstreamList, locationList);
        try{
            logger.info("刷新 Nginx 配置文件开始 nginxConfig:{}", JSON.toJSONString(nginxConfig));
            loadBalancingService.updateNginxConfig(nginxConfig);
            logger.info("刷新 Nginx 配置文件结束");
        }catch (Exception e){
            logger.error("刷新 Nginx 配置文件失败",e);
        }
    }

    /**
     * http://localhost:8001/wg/admin/load/copy
     */
    @GetMapping(value = "copy", produces = "application/json;charset=utf-8")
    public void copy() throws IOException {
        Path projectDir = Paths.get("").toAbsolutePath().getParent();
        Path nginxConfFilePath = projectDir.resolve("docs").resolve("data").resolve("nginx").resolve("nginx.conf");

        ProcessBuilder pb = new ProcessBuilder("docker", "cp", "/nginx.conf", "host:/"+nginxConfFilePath);
        pb.start();
    }

    private static void unTar(TarArchiveInputStream tis, File destFile)
            throws IOException {
        TarArchiveEntry tarEntry = null;
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            if (tarEntry.isDirectory()) {
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
            } else {
                FileOutputStream fos = new FileOutputStream(destFile);
                IOUtils.copy(tis, fos);
                fos.close();
            }
        }
        tis.close();
    }





}
