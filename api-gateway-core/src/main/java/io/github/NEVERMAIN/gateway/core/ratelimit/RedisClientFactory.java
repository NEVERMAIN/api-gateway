package io.github.NEVERMAIN.gateway.core.ratelimit;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(RedisClientFactory.class);

    private static final String REDIS_URI = "redis://192.168.198.138:16379";
    private static final RedisClient redisClient;
    private static final StatefulRedisConnection<String, String> connection;

    // 使用静态代码块在类加载时初始化，保证单例
    static {
        redisClient = RedisClient.create(REDIS_URI);
        connection = redisClient.connect();
        logger.info("成功连接到 Redis....");

        // 增加一个关闭钩子，在JVM关闭时优雅地释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("正在关闭 Redis 连接....");
            connection.close();
            redisClient.shutdown();
            logger.info("Redis 连接已关闭...");
        }));
    }

    /**
     * 获取一个异步命令的连接实例
     * @return RedisAsyncCommands
     */
    public RedisAsyncCommands<String, String> getAsyncCommands() {
        return connection.async();
    }

    public StatefulRedisConnection<String, String> getConnection() {
        return connection;
    }
}
