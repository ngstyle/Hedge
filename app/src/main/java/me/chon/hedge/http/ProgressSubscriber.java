package me.chon.hedge.http;

import android.content.Context;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by chon on 2017/2/16.
 * What? How? Why?
 */

public abstract class ProgressSubscriber<T> implements ProgressDialogHandler.ProgressCancelListener, Observer<T> {
    private Context context;
    private Disposable disposable;
    private ProgressDialogHandler mProgressDialogHandler;

    public ProgressSubscriber(Context context) {
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        System.out.println("currentThread:" + Thread.currentThread().getName());
        showProgressDialog();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
    }


    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onCancelProgress() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
