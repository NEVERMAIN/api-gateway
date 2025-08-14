package io.github.NEVERMAIN.gateway.core.ratelimit;

public class RateLimiterLuaScripts {

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
}
