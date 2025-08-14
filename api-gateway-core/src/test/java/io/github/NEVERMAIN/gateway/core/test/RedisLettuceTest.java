package io.github.NEVERMAIN.gateway.core.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.Test;

public class RedisLettuceTest {

    private static final String REDIS_URI = "redis://192.168.198.138:16379";

    private static final RedisClient redisClient;

    private static final StatefulRedisConnection<String, String> connection;

    /**
     * 滑动窗口限流脚本
     * KEYS[1]: 限流的 key (例如: rate_limit:ip:127.0.0.1)
     * ARGV[1]: 时间窗口（秒）
     * ARGV[2]: 时间窗口内允许的最大请求数
     *
     * 返回值:
     * 1: 允许
     * 0: 拒绝
     */
    public static final String SLIDING_WINDOW_SCRIPT =
            """
                    local current = redis.call('INCR', KEYS[1])
                    if tonumber(current) == 1 then
                        redis.call('EXPIRE', KEYS[1], ARGV[1])
                    end
                    if tonumber(current) > tonumber(ARGV[2]) then
                        return 0
                    else
                        return 1
                    end""";

    static {
        redisClient = RedisClient.create(REDIS_URI);
        connection = redisClient.connect();
        System.out.println("成功连接到 Redis.");

        // 增加一个关闭钩子，在JVM关闭时优雅地释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在关闭 Redis 连接...");
            connection.close();
            redisClient.shutdown();
            System.out.println("Redis 连接已关闭.");
        }));
    }

    public static void main(String[] args) throws InterruptedException {
        RedisAsyncCommands<String, String> async = connection.async();
        String rateLimitKey = "rate_limit:/wg/activity/sayHi";
        String timeWindow = "1";
        String maxRequests = "500";
        RedisFuture<Long> stage = async.eval(
                SLIDING_WINDOW_SCRIPT,
                ScriptOutputType.INTEGER,
                new String[]{rateLimitKey},
                timeWindow,
                maxRequests
        );

        stage.whenComplete((result, throwable) -> {
           if(throwable != null){
               System.err.println("限流检查时 Redis 出错: " + throwable.getMessage());
           }
           else if(result == 1){
               System.out.println("请求通过限流检查.");
           }
           else {
               System.out.println("请求被限流.");
           }
        });


        Thread.sleep(Integer.MAX_VALUE);


    }



}
