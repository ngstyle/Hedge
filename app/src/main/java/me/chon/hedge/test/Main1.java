package me.chon.hedge.test;

import java.util.function.Predicate;

/**
 * Created by chon on 2017/2/23.
 * What? How? Why?
 */

public class Main1 {

    public static void main(String[] args) {
        Factory<Boy> boyFactory = Boy::new;

        Predicate<String> predicate = s -> s.length() > 3;
    }


    //工厂类接口
    public interface Factory<T extends Parent> {
        T create(String name,int age);
    }

    //父类
    public static class Parent {
        private String name ;
        private int age;
        public Parent(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public void doSome(){

        }
    }

    //男孩类
    public static class Boy extends Parent {
        public Boy(String name, int age) {
            super(name, age);
        }

        @Override
        public void doSome() {
            System.out.println("我是个男孩");
        }
    }

    //女孩类
    public static class Girl extends Parent {
        public Girl(String name, int age) {
            super(name, age);
        }

        @Override
        public void doSome() {
            System.out.println("我是个女孩");
        }
    }

}
