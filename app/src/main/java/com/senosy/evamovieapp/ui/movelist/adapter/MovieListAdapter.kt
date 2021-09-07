package com.senosy.evamovieapp.ui.movelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.senosy.evamovieapp.Constants
import com.senosy.evamovieapp.databinding.MovieListItemBinding
import com.senosy.evamovieapp.domain.model.MovieEntity
import com.squareup.picasso.Picasso

/**
 * Adapter for showing [MovieEntity] in a list.
 */
class MovieListAdapter :
    ListAdapter<MovieEntity, MovieListAdapter.ViewHolder>(MovieItemDiffCallback()) {

    var onMovieClickListener: ((movieId: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder = ViewHolder(
        binding = MovieListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(movie = getItem(position), onMovieClickListener = onMovieClickListener)
    }

    inner class ViewHolder(private val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity, onMovieClickListener: ((movieId: Int) -> Unit)?) {
            binding.movieTitleTv.text = movie.title
            binding.movieRateTv.text = movie.voteAverage.toString()
            Picasso.get().load( Constants.IMAGE_BASE_URL + movie.posterPath).into(binding.movieImageIv)
            itemView.setOnClickListener {
                onMovieClickListener?.invoke(movie.id)
            }
        }
    }
}