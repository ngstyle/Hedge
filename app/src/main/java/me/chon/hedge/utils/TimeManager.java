package me.chon.hedge.utils;

import android.os.SystemClock;

/**
 * Created by chon on 2017/4/25.
 * What? How? Why?
 */

public class TimeManager {
    private TimeManager(){}
    private static TimeManager TIME_MANAGER;
    private static TimeManager getInstance() {
        if (TIME_MANAGER == null) {
            synchronized (TimeManager.class) {
                if (TIME_MANAGER == null) {
                    TIME_MANAGER = new TimeManager();
                }
            }
        }
        return TIME_MANAGER;
    }

    private boolean isServerTime;
    private long diffTime;

    public synchronized long getServerTime() {
        if (!isServerTime) {
            return System.currentTimeMillis();
        }
        return diffTime + SystemClock.elapsedRealtime();
    }

    // 在每个网络请求里边同步
    public synchronized void init(long serverTime) {
        diffTime = serverTime - System.currentTimeMillis() - SystemClock.elapsedRealtime();
        isServerTime = true;
    }

    // 利用OkHttp的Interceptor自动同步时间
    // 网络响应头包含Date字段（世界时间）
    // 利用Interceptor记录每次请求响应时间，如果本次网络操作的时间小于上一次网络操作的时间，
    // 则获取Date字段，转换时区后更新本地TimeManager。(init方法)
    // 这样时间就只会越来越精确了
}
