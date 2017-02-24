package me.chon.hedge.test;

/**
 * Created by chon on 2017/2/22.
 * What? How? Why?
 */

public class Main {

    public static void main(String[] args) {
        // Listenr 导致的内存泄露
        Leak leak = new Leak();
        LeakManager leakManager = new LeakManager();

        leakManager.register(leak);
        leak = null;
        leakManager.print("chon");

        // Inner class 导致的内存泄露
//        Leak leak = new Leak();
//        Leak.LeakInner leakInner = leak.leakInner;
//        leak = null;
//        leakInner.print();

        Transform<String,Integer> transform = Main::strToInt;
        int result = transform.transform("200");
        System.out.println("result = " + result);
    }

    static int strToInt(String str){
        return Integer.valueOf(str);
    }

    /**
     * 函数式接口
     * @param <A>
     * @param <B>
     */
    @FunctionalInterface
    interface Transform<A,B>{
        B transform(A a);
    }



}
