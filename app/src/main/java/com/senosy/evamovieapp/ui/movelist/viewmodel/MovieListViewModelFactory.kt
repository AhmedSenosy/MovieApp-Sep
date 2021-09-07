package com.senosy.evamovieapp.ui.movelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senosy.evamovieapp.core.thread.TaskExecutor
import com.senosy.evamovieapp.domain.usecase.MovieUseCase
import com.senosy.evamovieapp.ui.movelist.MovieListFragment

/**
 * Generates [MovieListViewModel] to be used in [MovieListFragment].
 */
@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(
    private val movieUseCase: MovieUseCase,
    private val executor: TaskExecutor
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MovieListViewModel(
        movieUseCase = movieUseCase, executor = executor
    ) as T
}