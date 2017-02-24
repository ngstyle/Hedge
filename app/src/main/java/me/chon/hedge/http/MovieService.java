package me.chon.hedge.http;

import java.util.List;

import io.reactivex.Observable;
import me.chon.hedge.entity.MovieEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chon on 2017/2/15.
 * What? How? Why?
 */

public interface MovieService {

    @GET("top250")
    Observable<HttpResult<List<MovieEntity.Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("in_theaters")
    Observable<HttpResult<List<MovieEntity.Subject>>> getMoviesInTheater();
}
