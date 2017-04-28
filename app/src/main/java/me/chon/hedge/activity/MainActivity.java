package me.chon.hedge.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.chon.hedge.R;
import me.chon.hedge.entity.MovieEntity;
import me.chon.hedge.http.HttpMethods;
import me.chon.hedge.http.ProgressSubscriber;
import me.chon.hedge.recyclerView.AppAdapter;
import me.chon.hedge.recyclerView.MovieAdapter;
import me.chon.hedge.utils.LogUtils;
import me.chon.hedge.widgets.pull.PullRecycler;
import me.chon.hedge.widgets.pull.layoutmanager.MyGridLayoutManager;
import me.chon.hedge.widgets.pull.layoutmanager.MyLinearLayoutManager;

public class MainActivity extends AppCompatActivity implements PullRecycler.OnRecyclerRefreshListener {

    @BindView(R.id.recycler_view)
    PullRecycler mRecyclerView;

    ProgressSubscriber<List<MovieEntity.Subject>> subscriber;

    private int start = 0;
    private int count = 10;
    private List<MovieEntity.Subject> mSubjects;
    private MovieAdapter movieAdapter;
    private AppAdapter appAdapter;

//    static Messenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        getMovie();
        getApps();
        mRecyclerView.enableLoadMore(false);
        mRecyclerView.enablePullToRefresh(false);
//        mRecyclerView.setOnRefreshListener(this);

        LogUtils.e("MainActivity onCreate: " + System.currentTimeMillis());
//        Intent intent = new Intent(this, MessengerService.class);
//        bindService(intent, new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                messenger = new Messenger(service);
//                String msg = "hello this is client";
//
//                sendMsgToServe(msg);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        }, Context.BIND_AUTO_CREATE);
    }

//    static int msgCount;
//    private static void sendMsgToServe(String msg) {
//        Message message = Message.obtain(null, Constants.MSG_FROM_CLIENT);
//        Bundle bundle = message.getData();
//        if (bundle == null) {
//            bundle = new Bundle();
//            bundle.putString("msg", msg);
//            message.setData(bundle);
//        } else {
//            bundle.putString("msg", msg);
//        }
//
//        // 客户端用来接收
//        message.replyTo = new Messenger(new MessengerHandler());
//
//        try {
//            messenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }

//    private static class MessengerHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Constants.MSG_FROM_SERVE:
//                    String replyMsg = msg.getData().getString("msg");
//                    LogUtils.e(replyMsg + Thread.currentThread().getName());
//
//                    SystemClock.sleep(1000);
//                    sendMsgToServe("消息又来了: " + ++msgCount);
//                    break;
//            }
//        }
//    }


    private void getMovie(){
        subscriber = new ProgressSubscriber<List<MovieEntity.Subject>>(this) {
            @Override
            public void onNext(List<MovieEntity.Subject> subjects) {
                if (mSubjects == null) {
                    mSubjects = new ArrayList<>();
                    mSubjects.addAll(subjects);
                    mRecyclerView.setLayoutManager(new MyLinearLayoutManager(MainActivity.this));
                    movieAdapter = new MovieAdapter(MainActivity.this, mSubjects);
                    mRecyclerView.setAdapter(movieAdapter);
                } else {
                    if (start == 0) {
                        mSubjects.clear();
                    }
                    mSubjects.addAll(subjects);
                    movieAdapter.notifyDataSetChanged();
                    mRecyclerView.onRefreshCompleted();
                }
            }
        };
        HttpMethods.getInstance().getTopMovie(subscriber, start, count);
    }

    private void getApps(){
//        PackageManager pkgMngr = getPackageManager();
//        List<PackageInfo> pkgs = pkgMngr.getInstalledPackages(0);
//        for (PackageInfo packageInfo : pkgs) {
//            ApplicationInfo appInfo = packageInfo.applicationInfo;
//            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//
//            }
//        }

        final List<PackageInfo> packageInfos = new ArrayList<>();

        Observable.just(getPackageManager()).flatMap(new Function<PackageManager, ObservableSource<PackageInfo>>() {
            @Override
            public ObservableSource<PackageInfo> apply(PackageManager packageManager) throws Exception {
                return Observable.fromIterable(packageManager.getInstalledPackages(0));
            }
        }).filter(new Predicate<PackageInfo>() {
            @Override
            public boolean test(PackageInfo packageInfo) throws Exception {
//                return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
                return true;
            }
        }).subscribe(new Observer<PackageInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PackageInfo packageInfo) {
                LogUtils.e("nohc",packageInfo.applicationInfo.loadLabel(getPackageManager()) + "\t" + packageInfo.packageName);
                packageInfos.add(packageInfo);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mRecyclerView.setLayoutManager(new MyGridLayoutManager(MainActivity.this,4));
                appAdapter = new AppAdapter(MainActivity.this, packageInfos);
                mRecyclerView.setAdapter(appAdapter);
            }
        });

    }


    @Override
    protected void onDestroy() {
        subscriber.onCancelProgress();
        super.onDestroy();
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_LOAD_MORE_REFRESH) {
            start += count;
        } else if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            SystemClock.sleep(2000);
            start = 0;
        }
        getMovie();
    }
}
