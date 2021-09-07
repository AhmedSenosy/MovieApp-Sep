package com.senosy.evamovieapp.ui.movelist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.senosy.evamovieapp.core.thread.TaskExecutor
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.senosy.evamovieapp.domain.usecase.MovieUseCase
import com.senosy.evamovieapp.ui.movelist.MovieListFragment

/**
 * View model for handling and dealing with [MovieUseCase]. This view model used in [MovieListFragment].
 */
class MovieListViewModel(
    private val movieUseCase: MovieUseCase,
    private val executor: TaskExecutor
) : ViewModel() {

    private val _movieList = MutableLiveData<List<MovieEntity>>()
    val movieList: LiveData<List<MovieEntity>>
        get() = _movieList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMovies(pageNumber: Int) {
        try {
            executor.executeOnDiskIO {
                movieUseCase.getMovies(
                    pageNumber = pageNumber,
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