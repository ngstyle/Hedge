package me.chon.hedge.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

import me.chon.hedge.utils.LogUtils;

/**
 * Created by chon on 2017/2/28.
 * What? How? Why?
 */

public class MessengerService extends Service {

    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("MessengerService onCreate: " + System.currentTimeMillis());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    String clientMsg = msg.getData().getString("msg");
                    LogUtils.e(clientMsg + Thread.currentThread().getName());

                    Messenger client = msg.replyTo;
                    Message message = Message.obtain(null,Constants.MSG_FROM_SERVE);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","嗯，我用心淡保持我的持续热情");
                    message.setData(bundle);

                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    Looper.getMainLooper().getThread().getStackTrace();

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private IBookManager.Stub stub = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return null;
        }

        @Override
        public void addBook(Book book) throws RemoteException {

        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            // 权限认证
            return super.onTransact(code, data, reply, flags);
        }
    };

}
