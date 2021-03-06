package com.senosy.evamovieapp.domain.model



/**
 * Entity model for movie.
 */
data class MovieEntity(
    val id: Int,
    val overview: String,
    val originalLanguage: String,
    val originalTitle: String,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val popularity: Double,
    val isAdult: Boolean,
    val voteAverage: Double
)

///**
// * Mapper to map [MovieEntity] to [MovieDataSource].
// */
//fun MovieEntity.mapToUiModel(): MovieUiModel = MovieUiModel(
//    id = id,
//    overview = overview,
//    originalLanguage = originalLanguage,
//    originalTitle = originalTitle,
//    title = title,
//    posterPath = posterPath,
//    releaseDate = releaseDate,
//    popularity = popularity
//)
//
///**
// * Mapper to map list of [MovieEntity] to list of [MovieUiModel].
// */
//fun List<MovieEntity>.mapToUiModel(): List<MovieUiModel> = map { it.mapToUiModel() }