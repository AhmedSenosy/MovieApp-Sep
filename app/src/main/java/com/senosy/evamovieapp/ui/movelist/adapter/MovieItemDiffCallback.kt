package com.senosy.evamovieapp.ui.movelist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.senosy.evamovieapp.datasource.model.MovieDataSource
import com.senosy.evamovieapp.domain.model.MovieEntity

/**
 * Callback function for [MovieEntity] used in the [MovieListAdapter].
 */
class MovieItemDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(
        oldItem: MovieEntity,
        newItem: MovieEntity
    ): Boolean = oldItem.title == newItem.title && oldItem.overview == newItem.overview &&
        oldItem.posterPath == newItem.posterPath
}