package me.chon.hedge.test;

import android.animation.ObjectAnimator;

/**
 * Created by chon on 2017/2/22.
 * What? How? Why?
 */

public class Main {
    private A a = A.getA();

    public static void main(String[] args) {

        System.err.println("result = " + add(10) + "\t" + System.nanoTime());

        System.out.println("main method execute.");
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

        Transform<String, Integer> transform = Main::strToInt;
        int result = transform.transform("200");
        System.out.println("result = " + result);

        RequestMethod method = RequestMethod.DELETE;
        System.out.println(method.name());
    }

    public enum RequestMethod {
        GET, PUT, DELETE
    }

    static int strToInt(String str) {
        return Integer.valueOf(str);
    }

    /**
     * 函数式接口
     *
     * @param <A>
     * @param <B>
     */
    @FunctionalInterface
    interface Transform<A, B> {
        B transform(A a);
    }

    private static class A {
        public A() {
            System.out.println("inner class A constructor execute");
        }

        static A getA() {
            A a = new A();
            return a;
        }

        public void print() {
            System.out.println("A print method");
        }
    }

    public static int add(int param) {
        int result;
        if (param == 1) {
            return 1;
        } else {
            System.err.println("1param = " + param + "\t" + System.nanoTime());
            result = param + add(--param);
            System.err.println("2param = " + param + "\t" + System.nanoTime());
            return result;
        }
    }
}