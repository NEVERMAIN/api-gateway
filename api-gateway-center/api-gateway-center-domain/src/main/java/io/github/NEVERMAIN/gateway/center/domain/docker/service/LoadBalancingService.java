package io.github.NEVERMAIN.gateway.center.domain.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.aggregates.NginxConfig;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.vo.LocationVO;
import io.github.NEVERMAIN.gateway.center.domain.docker.model.vo.UpstreamVO;
import io.github.NEVERMAIN.gateway.center.domain.manage.model.valobj.GatewayServerDetailVO;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: 负载均衡配置服务
 */

@Service
public class LoadBalancingService extends AbstractLoadBalancing {

    private final Logger logger = LoggerFactory.getLogger(LoadBalancingService.class);

    @Value("${nginx.server_name}")
    private String nginx_server_name;

    @Override
    protected String createNginxConfigFile(NginxConfig nginxConfig) throws IOException {
        // 1.创建 Nginx 配置文件
        String nginxConfigContentStr = buildNginxConfig(nginxConfig.getUpstreamList(), nginxConfig.getLocationList());
        // 2. 定义目标路径（绝对路径）
        logger.info("当前项目目录 {}", Paths.get("").toAbsolutePath());
        Path path = Paths.get("data", "nginx", "nginx.conf");
        try {
            // 创建父目录（如果不存在）
            Path parentDir = path.getParent();
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
                logger.info("✅ 创建父目录: {}", parentDir);
            }
            // 4. 创建/覆盖配置文件
            Files.write(path, nginxConfigContentStr.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            logger.info("🔄 Nginx 配置文件已更新: {}", path);
            return path.toAbsolutePath().toString();
        } catch (Exception e) {
            logger.error("❌ nginx 配置文件创建失败: {} | 原因: {}", path, e.getMessage());
            throw e;
        }

    }

    @Override
    protected void copyDockerFile(String applicationName, String containerFilePath, String localNginxPath) throws IOException {
        // Docker client
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock").build();

        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

        // Copy file from container
        try (TarArchiveInputStream tarStream = new TarArchiveInputStream(
                dockerClient.copyArchiveFromContainerCmd(applicationName,
                                containerFilePath)
                        .exec())) {
            unTar(tarStream, new File(localNginxPath));
        }
        dockerClient.close();
    }

    private static void unTar(TarArchiveInputStream tis, File destFile) throws IOException {
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

    @Override
    protected void refreshNginxConfig(String nginxName) throws InterruptedException, IOException {
        DockerClient dockerClient = null;
        try {
            // 1. 创建Docker客户端配置
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("unix:///var/run/docker.sock")
                    .build();

            // 2. 创建 Apache HTTP 客户端
            DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                    .dockerHost(config.getDockerHost())
                    .sslConfig(config.getSSLConfig())
                    .connectionTimeout(Duration.ofSeconds(30))
                    .responseTimeout(Duration.ofSeconds(60))
                    .build();

            // 3. 构建Docker客户端实例
            dockerClient = DockerClientImpl.getInstance(config, httpClient);

            // 4. 查找目标容器
            List<Container> containers = dockerClient.listContainersCmd()
                    .withNameFilter(new ArrayList<String>() {{
                        add(nginxName);
                    }})
                    .withStatusFilter(new ArrayList<String>() {{
                        add("running");
                    }})
                    .exec();

            if (containers.isEmpty()) {
                logger.error("🚨 未找到运行中的Nginx容器: {}", nginxName);
                throw new RuntimeException("Nginx容器不存在或未运行: " + nginxName);
            }

            String containerId = containers.get(0).getId();
            logger.info("✅ 定位到容器 ID: {}", containerId);

            // 5. 执行nginx重载命令
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient
                    .execCreateCmd(containerId)
                    .withCmd("nginx", "-s", "reload")
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();

            logger.info("⚡ 执行配置重载命令，执行ID: {}", execCreateCmdResponse.getId());

            // 6. 异步等待命令执行完成
            dockerClient.execStartCmd(execCreateCmdResponse.getId())
                    .exec(new ResultCallback.Adapter<>())
                    .awaitCompletion(15, TimeUnit.SECONDS);  // 设置超时时间

            logger.info("🔄 Nginx配置热重载完成");

        } catch (Exception e) {
            logger.error("❌ 重载配置时发生错误", e);
            throw e;
        } finally {
            // 7. 安全关闭连接
            if (dockerClient != null) {
                try {
                    dockerClient.close();
                    logger.debug("🔌 Docker客户端连接已关闭");
                } catch (Exception e) {
                    logger.warn("⚠️ 关闭连接时发生非阻塞性错误", e);
                }
            }
        }
    }

    private String buildNginxConfig(List<UpstreamVO> upstreamList, List<LocationVO> locationList) {
        String config = "\n" +
                "user  nginx;\n" +
                "worker_processes  auto;\n" +
                "\n" +
                "error_log  /var/log/nginx/error.log notice;\n" +
                "pid        /run/nginx.pid;\n" +
                "\n" +
                "\n" +
                "events {\n" +
                "    worker_connections  1024;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "http {\n" +
                "    include       /etc/nginx/mime.types;\n" +
                "    default_type  application/octet-stream;\n" +
                "\n" +
                "    log_format  main  '$remote_addr - $remote_user [$time_local] \"$request\" '\n" +
                "                      '$status $body_bytes_sent \"$http_referer\" '\n" +
                "                      '\"$http_user_agent\" \"$http_x_forwarded_for\"';\n" +
                "\n" +
                "    access_log  /var/log/nginx/access.log  main;\n" +
                "\n" +
                "    sendfile        on;\n" +
                "    #tcp_nopush     on;\n" +
                "\n" +
                "    keepalive_timeout  65;\n" +
                "\n" +
                "    #gzip  on;\n" +
                "\n" +
                "    include /etc/nginx/conf.d/*.conf;\n" +
                "\n" +
                "    # 设定负载均衡的服务器列表\n" +
                "upstream_config_placeholder" +
                "\n" +
                "    # HTTP 服务器\n" +
                "    server {\n" +
                "        # 监听 80 端口,用于HTTP协议\n" +
                "        listen       80;\n" +
                "\n" +
                "        # 定义使用 IP/域名访问\n" +
                "        server_name  " + nginx_server_name + ";\n" +
                "\n" +
                "        # 首页\n" +
                "        index index.html;\n" +
                "\n" +
                "        # 反向代理的路径(upstream绑定),location 后面设置映射的路径\n" +
                "        # location / {\n" +
                "        #    proxy_pass http://192.168.198.1:9001;\n" +
                "        # }\n" +
                "\n" +
                "location_config_placeholder" +
                "    }\n" +
                "\n" +
                "}\n";

        // 组装配置 Upstream
        /*
           upstream api01 {

                least_conn;

                server 192.168.198.1:9001;
                server 192.168.198.1:9002;
            }
         */
        StringBuilder upstreamStrBuilder = new StringBuilder();
        for (UpstreamVO upstreamVO : upstreamList) {
            upstreamStrBuilder.append("\t").append("upstream").append(" ").append(upstreamVO.getName()).append("{\r\n");
            upstreamStrBuilder.append("\t").append("\t").append(upstreamVO.getStrategy()).append("\r\n").append("\r\n");
            List<String> servers = upstreamVO.getServers();
            for (String server : servers) {
                upstreamStrBuilder.append("\t").append("\t").append("server").append(" ").append(server).append(";\r\n");
            }
            upstreamStrBuilder.append("\t").append("}").append("\r\n").append("\r\n");
        }

        // 组装配置 Location
        /*
            location /api01/ {
                rewrite ^/api01/(.*)$ /$1 break;
                proxy_pass http://api01;
            }
         */
        StringBuilder locationStrBuilder = new StringBuilder();
        for (LocationVO locationVO : locationList) {
            // location /api01/
            locationStrBuilder.append("\t").append("\t").append("location").append(" ").append(locationVO.getName()).append(" {\r\n");
            // rewrite ^/api01/(.*)$ /$1 break; 设置重写URL，在代理后去掉根路径 api01 此字段只是配合路由，不做处理
            locationStrBuilder.append("\t").append("\t").append("\t").append("rewrite ^").append(locationVO.getName()).append("(.*)$ /$1 break;").append("\r\n");
            // proxy_pass http://api01;
            locationStrBuilder.append("\t").append("\t").append("\t").append("proxy_pass").append(" ").append(locationVO.getProxy_pass()).append("\r\n");
            locationStrBuilder.append("\t").append("\t").append("}").append("\r\n").append("\r\n");
        }

        // 替换配置
        config = config.replace("upstream_config_placeholder", upstreamStrBuilder.toString());
        config = config.replace("location_config_placeholder", locationStrBuilder.toString());
        return config;
    }

    @Override
    public NginxConfig assembleNginxConfig(List<GatewayServerDetailVO> gatewayServerDetailVOList) {

        // 1.组装 Nginx 网关刷新配置信息
        Map<String, List<GatewayServerDetailVO>> gatewayServerDetailMap =
                gatewayServerDetailVOList.stream().collect(Collectors.groupingBy(GatewayServerDetailVO::getGroupId));

        Set<String> uniqueGroupIdList = gatewayServerDetailMap.keySet();
        // 2.组装 Location 信息
        ArrayList<LocationVO> locationList = new ArrayList<>();
        for (String name : uniqueGroupIdList) {
            // location /api01/ {
            //     rewrite ^/api01/(.*)$ /$1 break;
            // 	   proxy_pass http://api01;
            // }
            locationList.add(new LocationVO("/" + name + "/", "http://" + name + ";"));
        }
        // 3. 组装 Upstream 信息
        ArrayList<UpstreamVO> upstreamList = new ArrayList<>();
        for (String name : uniqueGroupIdList) {
            // upstream api01 {
            //     least_conn;
            //     server 172.20.10.12:9001;
            //     #server 172.20.10.12:9002;
            // }
            List<String> servers = gatewayServerDetailMap.get(name).stream()
                    .map(GatewayServerDetailVO::getGatewayAddress)
                    .toList();
            upstreamList.add(new UpstreamVO(name, "least_conn;", servers));
        }

        return new NginxConfig(upstreamList, locationList);
    }
}
