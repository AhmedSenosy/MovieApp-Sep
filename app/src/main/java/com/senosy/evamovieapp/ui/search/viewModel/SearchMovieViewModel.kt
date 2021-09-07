package com.senosy.evamovieapp.ui.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.senosy.evamovieapp.core.thread.TaskExecutor
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.senosy.evamovieapp.domain.usecase.SearchUserCase

class SearchMovieViewModel(private val searchUseCase: SearchUserCase,
                           private val executor: TaskExecutor
):ViewModel() {
    private val _movieList = MutableLiveData<List<MovieEntity>>()
    val movieList: LiveData<List<MovieEntity>>
        get() = _movieList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun searchMovies(pageNumber: Int,query:String) {
        try {
            executor.executeOnDiskIO {
                searchUseCase.searchMovies(
                    pageNumber = pageNumber,
                    query = query,
                    success = { movieListEntity ->
                        _movieList.postValue(movieListEntity)
                    },
                    apiError = { message ->
                        _errorMessage.postValue(message)
                    }
                )
            }
        } catch (e: Exception) {
            _errorMessage.postValue(e.message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        executor.shutDown()
    }
}