package com.senosy.evamovieapp.datasource.repository

import com.senosy.evamovieapp.core.api.ApiEmptyResponse
import com.senosy.evamovieapp.core.api.ApiErrorResponse
import com.senosy.evamovieapp.core.api.ApiResponse
import com.senosy.evamovieapp.core.api.ApiSuccessResponse
import com.senosy.evamovieapp.datasource.model.MovieDataSource
import com.senosy.evamovieapp.datasource.remote.client.MovieApiService
import com.senosy.evamovieapp.datasource.remote.model.MovieListRemote
import com.senosy.evamovieapp.datasource.remote.model.mapToDataSource
import com.senosy.evamovieapp.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val remote: MovieApiService):SearchRepository {
    override fun searchMovies(
        pageNumber: Int,
        query: String,
        success: (List<MovieDataSource>) -> Unit,
        apiError: (String) -> Unit
    ) {
        val remoteMovies = remote.searchMovie(pageNumber = pageNumber,query = query).execute()
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
}