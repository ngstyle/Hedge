package me.chon.hedge.test;

/**
 * Created by chon on 2017/2/22.
 * What? How? Why?
 */

public class TestThread extends Thread {

    private final Leak leak;

    public TestThread(Leak leak) {
        this.leak = leak;
    }

    @Override
    public void run() {
        int i = 0;

        while (i < 100) {
            System.out.println(Thread.currentThread().getName() + ": " + i++);
        }
    }
}
