package com.senosy.evamovieapp.ui.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.senosy.evamovieapp.core.thread.TaskExecutor
import com.senosy.evamovieapp.domain.usecase.SearchUserCase

class SearchMovieViewModelFactory(private val searchUseCase: SearchUserCase,
                                  private val executor: TaskExecutor
): ViewModelProvider.NewInstanceFactory()  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SearchMovieViewModel(
        searchUseCase = searchUseCase, executor = executor
    ) as T
}