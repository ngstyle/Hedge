package me.chon.hedge.test;

/**
 * Created by chon on 2017/2/22.
 * What? How? Why?
 */

public class Leak {


    public TestThread thread;
    public LeakInner leakInner;

    public Leak() {
        this.leakInner = new LeakInner();
//        this.thread = new TestThread(this);
//        thread.start();
    }

    public void print(String s) {
        System.out.println(s);
    }


    public class LeakInner {

        public void print() {
            int i = 0;
            while (i < 100) {
                Leak.this.print("leakInner: " + i++);
            }
        }
    }

}
