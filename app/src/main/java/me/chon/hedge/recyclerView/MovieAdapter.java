package me.chon.hedge.recyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.chon.hedge.R;
import me.chon.hedge.activity.DetailActivity;
import me.chon.hedge.entity.MovieEntity;
import me.chon.hedge.widgets.pull.BaseListAdapter;
import me.chon.hedge.widgets.pull.BaseViewHolder;

/**
 * Created by chon on 2017/2/23.
 * What? How? Why?
 */

public class MovieAdapter extends BaseListAdapter {
    private final Context context;
    private final List<MovieEntity.Subject> data;

    public MovieAdapter(Context context, List<MovieEntity.Subject> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected int getDataCount() {
        return data.size();
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_movie,null);
        return new MovieHolder(itemView,data);
    }

    private static class MovieHolder extends BaseViewHolder {
        private List<MovieEntity.Subject> data;
        ImageView avatar;
        TextView name;
        TextView directors;
        TextView casts;

        MovieHolder(View itemView, List<MovieEntity.Subject> data) {
            super(itemView);
            this.data = data;
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.tv_movie_name);
            directors = (TextView) itemView.findViewById(R.id.tv_directors);
            casts = (TextView) itemView.findViewById(R.id.tv_casts);
        }

        @Override
        public void onBindViewHolder(int position) {
            MovieEntity.Subject subject = data.get(position);

            name.setText(subject.getTitle());

            String directors = "导演：";
            for (MovieEntity.Cast cast : subject.getDirectors()) {
                directors += cast.getName() + " ";
            }
            this.directors.setText(directors);

            String casts = "主演：";
            for (MovieEntity.Cast cast : subject.getCasts()) {
                casts += cast.getName() + " ";
            }
            this.casts.setText(casts + "\n" + subject.getGenres().toString());

            Glide.with(itemView.getContext()).load(subject.getImages().getSmall()).into(avatar);
        }

        @Override
        public void onItemClick(View view, int position) {
            MovieEntity.Subject subject = data.get(position);
            DetailActivity.start(itemView.getContext(),subject);
        }
    }
}
