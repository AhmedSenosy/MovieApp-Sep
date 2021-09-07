package com.senosy.evamovieapp.ui.movelist.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.senosy.evamovieapp.core.extensions.gone
import com.senosy.evamovieapp.core.extensions.visible
import com.senosy.evamovieapp.core.extensions.visibleOrGone
import com.senosy.evamovieapp.core.widget.EndlessRecyclerViewScrollListener
import com.senosy.evamovieapp.databinding.MovieListViewBinding
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.senosy.evamovieapp.ui.movelist.adapter.MovieListAdapter

/**
 * Custom view that holds all the logic related to showing [MovieEntity] in a list appearance.
 */
class MovieListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttrs, defStyleRes) {

    private val viewBinding: MovieListViewBinding = MovieListViewBinding.inflate(LayoutInflater.from(context), this)

    private val movieListAdapter = MovieListAdapter()

    private var getMovies: ((pageNumber: Int) -> Unit)? = null
    private var onMovieClickListener: ((movieId: Int) -> Unit)? = null

    init {
        configUI()
    }

    private fun configUI() {
        with(viewBinding) {
            val endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                movieListView.layoutManager as GridLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    getMovies?.invoke(page)
                }
            }
            movieListView.apply {
                itemAnimator = DefaultItemAnimator()
                setHasFixedSize(true)
                adapter = movieListAdapter
            }

            movieListSrl.setOnRefreshListener {
                endlessRecyclerViewScrollListener.resetState()
                getMovies?.invoke(1)
            }

            movieListView.addOnScrollListener(endlessRecyclerViewScrollListener)

            movieListAdapter.onMovieClickListener = { movieId ->
                onMovieClickListener?.invoke(movieId)
            }
        }
    }

    fun addCallbacks(getMovies: (Int) -> Unit, onMovieClickListener: (Int) -> Unit) {
        this.getMovies = getMovies
        this.onMovieClickListener = onMovieClickListener
    }

    fun showLoading(isLoading: Boolean, isFirstPage: Boolean) {
        with(viewBinding) {
            if (isFirstPage) {
                movieListSrl.post {
                    movieListSrl.isRefreshing = isLoading
                }
            } else {
                loadMorePb.post {
                    loadMorePb.visibleOrGone(show = isLoading)
                }
            }
        }
    }

    fun showMovies(movies: List<MovieEntity>) {
        with(viewBinding) {
            loadMorePb.gone()
            emptyListTv.gone()
            movieListView.visible()

            movieListAdapter.submitList(movies)
        }
    }

    fun showErrorMessage(message: String?) {
        with(viewBinding) {
            loadMorePb.gone()
            emptyListTv.visible()
            movieListView.gone()

            emptyListTv.text = message
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        getMovies = null
        onMovieClickListener = null
    }
}