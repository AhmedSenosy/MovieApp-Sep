package com.senosy.evamovieapp.datasource.remote.client

import com.senosy.evamovieapp.datasource.remote.model.MovieListRemote
import com.senosy.evamovieapp.datasource.remote.model.MovieRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API Service interface that holds all movie APIs.
 */
interface MovieApiService {

    @GET("movie/top_rated")
    fun getMoviesTopRated(@Query("page") pageNumber: Int?): Call<MovieListRemote>

    @GET("movie/now_playing")
    fun getMoviesNowPlaying(@Query("page") pageNumber: Int?): Call<MovieListRemote>

    @GET("search/movie")
    fun searchMovie(@Query("page") pageNumber: Int?,@Query("query") query:String?): Call<MovieListRemote>

    @GET("movie/{id}")
    fun getMovie(@Path("id") movieId: Int?): Call<MovieRemote>
}