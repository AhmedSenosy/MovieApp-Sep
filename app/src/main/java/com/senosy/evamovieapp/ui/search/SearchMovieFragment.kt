package com.senosy.evamovieapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.senosy.evamovieapp.core.thread.DefaultTaskExecutor
import com.senosy.evamovieapp.databinding.FragmentSearchMovieBinding
import com.senosy.evamovieapp.datasource.remote.client.MovieApiService
import com.senosy.evamovieapp.datasource.remote.client.RetrofitClient
import com.senosy.evamovieapp.datasource.repository.SearchRepositoryImpl
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.senosy.evamovieapp.domain.usecase.SearchUserCase
import com.senosy.evamovieapp.ui.movelist.viewmodel.MovieListViewModelFactory
import com.senosy.evamovieapp.ui.search.viewModel.SearchMovieViewModel
import com.senosy.evamovieapp.ui.search.viewModel.SearchMovieViewModelFactory


class SearchMovieFragment : Fragment() {

    private var binding: FragmentSearchMovieBinding? = null
    private val movieListViewModel: SearchMovieViewModel by viewModels {
        val searchUseCase = getSearchUseCase(
            apiKey = "fc47660226072874be57974ff797a0cd",

            )
        SearchMovieViewModelFactory(
            searchUseCase = searchUseCase,
            executor = DefaultTaskExecutor()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSearchMovieBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieList: MutableList<MovieEntity> = mutableListOf()
        var isFirstPage = true

        binding?.searchEt?.doOnTextChanged { text, start, before, count ->
            if(text.toString().trim().length >=3)
            {
                movieList.clear()
                binding?.resultView?.showLoading(isLoading = true,isFirstPage=true)
                movieListViewModel.searchMovies(pageNumber = 1,text.toString())
            }
            else
            {
                movieList.clear()
                binding?.resultView?.showMovies(movieList)
            }

        }

        binding?.resultView?.let {
            movieListViewModel.movieList.observe(viewLifecycleOwner) { movies ->
                movieList.clear()
                movieList.addAll( movies)
                it.showLoading(isLoading = false, isFirstPage = isFirstPage)
                it.showMovies(movies = movieList)
            }
            movieListViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
                it.showLoading(isLoading = false, isFirstPage = isFirstPage)
                it.showErrorMessage(message = message)
            }
            it.addCallbacks(
                getMovies = { pageNumber ->
                    it.showLoading(isLoading = true, isFirstPage = isFirstPage)
                    movieListViewModel.searchMovies(pageNumber = 1,binding?.searchEt?.text.toString())
                },
                onMovieClickListener = { movieId ->

                }
            )
        }
    }

    fun getSearchUseCase(apiKey: String): SearchUserCase {
        val remote = RetrofitClient.getRetrofit(apiKey = apiKey).create(MovieApiService::class.java)
        val searchRepository = SearchRepositoryImpl(remote)
        return SearchUserCase(searchRepository = searchRepository)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}