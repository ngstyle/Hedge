package me.chon.hedge;

import android.app.Application;
import android.content.Intent;
import android.os.Looper;
import android.util.Printer;

import me.chon.hedge.aidl.MessengerService;
import me.chon.hedge.performance.LogMonitor;
import me.chon.hedge.utils.LogUtils;

/**
 * Created by chon on 2017/3/2.
 * What? How? Why?
 */

public class HedgeApplication extends Application {

    @Override
    public void onCreate() {
        LogUtils.e("HedgeApplication onCreate: " + System.currentTimeMillis());
        startService(new Intent(this, MessengerService.class));

        Looper.getMainLooper().setMessageLogging(new Printer() {

            private final String START = ">>>>> Dispatching";
            private final String END = "<<<<< Finished";

            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    LogMonitor.getInstance().startMonitor();
                } else if (x.startsWith(END)) {
                    LogMonitor.getInstance().removeMonitor();
                }
            }
        });
    }
}
