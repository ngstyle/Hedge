package me.chon.hedge.performance;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.Choreographer;

import java.util.HashMap;
import java.util.Map;

import me.chon.hedge.utils.LogUtils;

/**
 * Created by chon on 2017/3/2.
 * What? How? Why?
 */

public class LogMonitor {
    private static LogMonitor instance = new LogMonitor();
    private LogMonitor() {
        HandlerThread handlerThread = new HandlerThread("LogMonitor");
        handlerThread.start();
        monitorHandler = new Handler(handlerThread.getLooper());
    }

    public static LogMonitor getInstance() {
        return instance;
    }
    private static final short DELAY_TIME = 1000;

    private Handler monitorHandler;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder stringBuilder = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement traceElement : stackTrace) {
                stringBuilder.append(traceElement.toString() + "\n");
            }

            LogUtils.e(stringBuilder.toString());

        }
    };

    public void startMonitor() {
        monitorHandler.postDelayed(runnable,DELAY_TIME);

//        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
//            @Override
//            public void doFrame(long frameTimeNanos) {
//
//            }
//        });
    }

    public void removeMonitor() {
        monitorHandler.removeCallbacks(runnable);
    }


}
