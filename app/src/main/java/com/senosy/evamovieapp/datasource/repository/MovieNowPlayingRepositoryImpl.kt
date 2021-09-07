package com.senosy.evamovieapp.datasource.repository

import com.senosy.evamovieapp.core.api.ApiEmptyResponse
import com.senosy.evamovieapp.core.api.ApiErrorResponse
import com.senosy.evamovieapp.core.api.ApiResponse
import com.senosy.evamovieapp.core.api.ApiSuccessResponse
import com.senosy.evamovieapp.datasource.model.MovieDataSource
import com.senosy.evamovieapp.datasource.remote.client.MovieApiService
import com.senosy.evamovieapp.datasource.remote.model.MovieListRemote
import com.senosy.evamovieapp.datasource.remote.model.MovieRemote
import com.senosy.evamovieapp.datasource.remote.model.mapToDataSource
import com.senosy.evamovieapp.domain.repository.MovieRepository

class MovieNowPlayingRepositoryImpl(private val remote: MovieApiService,
): MovieRepository {
    override fun getMovies(
        pageNumber: Int,
        success: (List<MovieDataSource>) -> Unit,
        apiError: (String) -> Unit
    ) {
        val remoteMovies = remote.getMoviesNowPlaying(pageNumber = pageNumber).execute()
        when (val apiResponse = ApiResponse.create<MovieListRemote>(remoteMovies)) {
            is ApiSuccessResponse -> {
                if (apiResponse.body.movies?.isNotEmpty() == true) {
                    success.invoke(apiResponse.body.movies.mapToDataSource())
                }
            }
            is ApiEmptyResponse -> {
                success.invoke(emptyList())
            }
            is ApiErrorResponse -> {
                apiError.invoke(apiResponse.errorMessage)
            }
        }
    }

    override fun getMovie(
        movieId: Int,
        success: (MovieDataSource) -> Unit,
        apiError: (String) -> Unit
    ) {
        val remoteMovie = remote.getMovie(movieId = movieId).execute()
        when (val apiResponse = ApiResponse.create<MovieRemote>(remoteMovie)) {
            is ApiSuccessResponse -> {
                success.invoke(apiResponse.body.mapToDataSource())
            }
            is ApiEmptyResponse -> {
                success.invoke(MovieRemote(id = -1).mapToDataSource())
            }
            is ApiErrorResponse -> {
                apiError.invoke(apiResponse.errorMessage)
            }
        }
    }
}