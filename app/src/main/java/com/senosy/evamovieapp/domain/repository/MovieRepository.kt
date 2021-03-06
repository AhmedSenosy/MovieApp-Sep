package com.senosy.evamovieapp.domain.repository

import com.senosy.evamovieapp.datasource.model.MovieDataSource


/**
 * Repository class that holds all the [MovieDataSource] CRUD operations and.
 */
interface MovieRepository {
    fun getMovies(pageNumber: Int, success: (List<MovieDataSource>) -> Unit, apiError: (String) -> Unit)
    fun getMovie(movieId: Int, success: (MovieDataSource) -> Unit, apiError: (String) -> Unit)
}
