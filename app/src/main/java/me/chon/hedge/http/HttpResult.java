package me.chon.hedge.http;

/**
 * Created by chon on 2017/2/15.
 * What? How? Why?
 */

public class HttpResult<T> {
    //用来模仿resultCode和resultMessage
    private int count;
    private int start;
    private int total;
    private String title;

    private int resultCode = 0;
    private String resultMessage;

    private T subjects;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getData() {
        return subjects;
    }

    public void setData(T data) {
        this.subjects = data;
    }
}
