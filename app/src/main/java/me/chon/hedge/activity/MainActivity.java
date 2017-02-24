package me.chon.hedge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chon.hedge.http.HttpMethods;
import me.chon.hedge.http.ProgressSubscriber;
import me.chon.hedge.R;
import me.chon.hedge.entity.MovieEntity;
import me.chon.hedge.recyclerView.MovieAdapter;
import me.chon.hedge.widgets.pull.PullRecycler;
import me.chon.hedge.widgets.pull.layoutmanager.MyLinearLayoutManager;

public class MainActivity extends AppCompatActivity implements PullRecycler.OnRecyclerRefreshListener {

    @BindView(R.id.recycler_view)
    PullRecycler mRecyclerView;

    ProgressSubscriber<List<MovieEntity.Subject>> subscriber;

    private int start = 0;
    private int count = 10;
    private List<MovieEntity.Subject> mSubjects;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getMovie();
        mRecyclerView.enableLoadMore(true);
        mRecyclerView.setOnRefreshListener(this);
    }


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

    @Override
    protected void onDestroy() {
        subscriber.onCancelProgress();
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_LOAD_MORE_REFRESH) {
            start += count;
        } else if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            start = 0;
        }
        getMovie();
    }
}
