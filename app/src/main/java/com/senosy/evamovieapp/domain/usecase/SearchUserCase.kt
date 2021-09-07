package com.senosy.evamovieapp.domain.usecase

import com.senosy.evamovieapp.datasource.model.mapToEntity
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.senosy.evamovieapp.domain.repository.SearchRepository

class SearchUserCase(private val searchRepository: SearchRepository) {

    fun searchMovies(pageNumber: Int,query:String, success: (List<MovieEntity>) -> Unit, apiError: (String) -> Unit) {
        searchRepository.searchMovies(
            pageNumber = pageNumber,
            query = query,
            success = { moviesListDataSource ->
                success.invoke(moviesListDataSource.mapToEntity())
            },
            apiError = { message ->
                apiError.invoke(message)
            }
        )
    }
}