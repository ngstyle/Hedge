package me.chon.hedge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chon.hedge.R;
import me.chon.hedge.entity.MovieEntity;

/**
 * Created by chon on 2017/2/24.
 * What? How? Why?
 */

public class DetailActivity extends AppCompatActivity {
    private final static String DATA_SUBJECT = "data_subject";

    public static void start(Context context,MovieEntity.Subject subject) {
        Intent intent = new Intent(context,DetailActivity.class);
        intent.putExtra(DATA_SUBJECT,subject);
        context.startActivity(intent);
    }

    @BindView(R.id.avatar)
    ImageView avatar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        MovieEntity.Subject subject = (MovieEntity.Subject) getIntent().getSerializableExtra(DATA_SUBJECT);
        if (subject != null) {
            Glide.with(this).load(subject.getImages().getLarge()).into(avatar);
        }
    }
}
