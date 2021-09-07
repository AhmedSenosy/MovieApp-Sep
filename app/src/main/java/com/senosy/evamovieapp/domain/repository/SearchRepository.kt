package com.senosy.evamovieapp.domain.repository

import com.senosy.evamovieapp.datasource.model.MovieDataSource

interface SearchRepository {
    fun searchMovies(pageNumber: Int,query:String, success: (List<MovieDataSource>) -> Unit, apiError: (String) -> Unit)
}