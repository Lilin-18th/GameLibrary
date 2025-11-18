package com.lilin.gamelibrary.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GamesResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("previous")
    val previous: String?,
    @SerialName("results")
    val results: List<GameDto>,
)

@Serializable
data class GameDto(
    @SerialName("id")
    val id: Int,
    @SerialName("slug")
    val slug: String,
    @SerialName("name")
    val name: String,
    @SerialName("released")
    val released: String?,
    @SerialName("tba")
    val tba: Boolean?,
    @SerialName("background_image")
    val backgroundImage: String?,
    @SerialName("rating")
    val rating: Double,
    @SerialName("rating_top")
    val ratingTop: Int?,
    @SerialName("ratings")
    val ratings: List<RatingsDto>?,
    @SerialName("ratings_count")
    val ratingsCount: Int?,
    @SerialName("reviews_text_count")
    val reviewsTextCount: Int?,
    @SerialName("added")
    val added: Int?,
    @SerialName("added_by_status")
    val addedByStatus: AddedByStatusDto?,
    @SerialName("metacritic")
    val metacritic: Int?,
    @SerialName("playtime")
    val playtime: Int?,
    @SerialName("suggestions_count")
    val suggestionsCount: Int?,
    @SerialName("updated")
    val updated: String?,
    @SerialName("esrb_rating")
    val esrbRating: EsrbRatingDto?,
    @SerialName("platforms")
    val platforms: List<PlatformInfoDto>,
)

@Serializable
data class RatingsDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("count")
    val count: Int,
    @SerialName("percent")
    val percent: Double,
)

@Serializable
data class AddedByStatusDto(
    @SerialName("yet")
    val yet: Int?,
    @SerialName("owned")
    val owned: Int?,
    @SerialName("beaten")
    val beaten: Int?,
    @SerialName("toplay")
    val toplay: Int?,
    @SerialName("dropped")
    val dropped: Int?,
    @SerialName("playing")
    val playing: Int?,
)

@Serializable
data class EsrbRatingDto(
    @SerialName("id")
    val id: Int,
    @SerialName("slug")
    val slug: String,
    @SerialName("name")
    val name: String,
)

@Serializable
data class PlatformInfoDto(
    @SerialName("platform")
    val platform: PlatformDto,
    @SerialName("released_at")
    val releasedAt: String?,
    @SerialName("requirements")
    val requirements: JsonObject? = null,
)

@Serializable
data class PlatformDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("slug")
    val slug: String,
)

// サブDTO
//@Serializable
//data class RatingDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("title")
//    val title: String,
//    @SerialName("count")
//    val count: Int,
//    @SerialName("percent")
//    val percent: Double
//)
//
//@Serializable
//data class AddedByStatusDto(
//    @SerialName("yet")
//    val yet: Int?,
//    @SerialName("owned")
//    val owned: Int?,
//    @SerialName("beaten")
//    val beaten: Int?,
//    @SerialName("toplay")
//    val toplay: Int?,
//    @SerialName("dropped")
//    val dropped: Int?,
//    @SerialName("playing")
//    val playing: Int?
//)
//
//@Serializable
//data class RequirementsDto(
//    @SerialName("minimum")
//    val minimum: String?,
//    @SerialName("recommended")
//    val recommended: String?
//)
//
//@Serializable
//data class ParentPlatformInfoDto(
//    @SerialName("platform")
//    val platform: ParentPlatformDto
//)
//
//@Serializable
//data class ParentPlatformDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("name")
//    val name: String,
//    @SerialName("slug")
//    val slug: String
//)
//
//@Serializable
//data class GenreDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("name")
//    val name: String,
//    @SerialName("slug")
//    val slug: String
//)
//
//@Serializable
//data class StoreInfoDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("store")
//    val store: StoreDto
//)
//
//@Serializable
//data class StoreDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("name")
//    val name: String,
//    @SerialName("slug")
//    val slug: String
//)
//
//@Serializable
//data class TagDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("name")
//    val name: String,
//    @SerialName("slug")
//    val slug: String,
//    @SerialName("language")
//    val language: String,
//    @SerialName("games_count")
//    val gamesCount: Int
//)
//
//@Serializable
//data class ScreenshotDto(
//    @SerialName("id")
//    val id: Int,
//    @SerialName("image")
//    val image: String
//)
