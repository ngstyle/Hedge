package me.nohc;

/**
 * Created by chon on 2017/4/27.
 * What? How? Why?
 */

public interface ViewInject<T> {
    void inject(T t, Object source);
}
