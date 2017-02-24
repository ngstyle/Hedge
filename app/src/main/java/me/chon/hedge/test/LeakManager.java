package me.chon.hedge.test;

import java.lang.ref.WeakReference;

/**
 * Created by chon on 2017/2/22.
 * What? How? Why?
 */

public class LeakManager {

    private WeakReference<Leak> leak;

    public void register(Leak leak) {
        this.leak = new WeakReference<>(leak);
    }

    public void unRegister() {
        this.leak = null;
    }

    public void print(String s) {
        int i = 0;
        System.gc();
        while (i < 1000) {
            if (leak.get() != null) {
                this.leak.get().print("LeakManager ->" + i++);
            } else {
                System.out.println("leak obj has been gc");
                return;
            }
        }
    }

}
