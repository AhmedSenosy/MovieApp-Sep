package com.senosy.evamovieapp.ui.movelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.senosy.evamovieapp.core.thread.DefaultTaskExecutor
import com.senosy.evamovieapp.databinding.FragmentMovieListBinding
import com.senosy.evamovieapp.datasource.model.MovieDataSource
import com.senosy.evamovieapp.datasource.remote.client.MovieApiService
import com.senosy.evamovieapp.datasource.remote.client.RetrofitClient
import com.senosy.evamovieapp.datasource.repository.MovieNowPlayingRepositoryImpl
import com.senosy.evamovieapp.datasource.repository.MovieRepositoryImpl
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.senosy.evamovieapp.domain.usecase.MovieUseCase
import com.senosy.evamovieapp.ui.movelist.viewmodel.MovieListViewModel
import com.senosy.evamovieapp.ui.movelist.viewmodel.MovieListViewModelFactory

/**
 * Fragment that shows [MovieEntity] in a list appearance.
 */
class MovieListFragment : Fragment() {
    lateinit var type:String
    companion object {
        fun newInstance(args: Bundle? = null): MovieListFragment = MovieListFragment().apply {
            arguments = args
        }
    }

    private var binding: FragmentMovieListBinding? = null
//
//    private val movieCoordinator: MovieCoordinator = MovieListFragmentServiceLocator.getCoordinator(
//        navigator = MovieNavigator()
//    )

    private val movieListViewModel: MovieListViewModel by viewModels {
        val movieUseCase = getMovieUseCase(
            apiKey = "fc47660226072874be57974ff797a0cd",

        )
        MovieListViewModelFactory(
            movieUseCase = movieUseCase,
            executor = DefaultTaskExecutor()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieListBinding.inflate(layoutInflater)
        type = arguments?.getString("fragmentType") ?: "now_playing"
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieList: MutableList<MovieEntity> = mutableListOf()
        var isFirstPage = true
        binding?.movieListView?.let {
            movieListViewModel.movieList.observe(viewLifecycleOwner) { movies ->
                movieList += movies
                it.showLoading(isLoading = false, isFirstPage = isFirstPage)
                it.showMovies(movies = movieList)
            }
            movieListViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
                it.showLoading(isLoading = false, isFirstPage = isFirstPage)
                it.showErrorMessage(message = message)
            }
            it.showLoading(isLoading = true, isFirstPage = true)
            movieListViewModel.getMovies(pageNumber = 1)
            it.addCallbacks(
                getMovies = { pageNumber ->
                    isFirstPage = pageNumber == 1
                    if (isFirstPage) {
                        movieList.clear()
                    }
                    it.showLoading(isLoading = true, isFirstPage = isFirstPage)
                    movieListViewModel.getMovies(pageNumber = pageNumber)
                },
                onMovieClickListener = { movieId ->
                    if (activity != null && activity is AppCompatActivity) {
//                        movieCoordinator.startMovieDetailsFragment(
//                            activity = activity as AppCompatActivity,
//                            movieId = movieId
//                        )
                    }
                }
            )
        }
    }

    fun getMovieUseCase(apiKey: String): MovieUseCase {
        val remote = RetrofitClient.getRetrofit(apiKey = apiKey).create(MovieApiService::class.java)
        val movieRepository =
        when(type){
            "top_rated" -> MovieRepositoryImpl(remote = remote)
            else -> MovieNowPlayingRepositoryImpl(remote = remote)
        }
        return MovieUseCase(movieRepository = movieRepository)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}