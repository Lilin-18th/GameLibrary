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
data class GameDetailResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("background_image")
    val backgroundImage: String? = null,
    @SerialName("released")
    val released: String? = null,
    @SerialName("metacritic")
    val metacritic: Int? = null,
    @SerialName("rating")
    val rating: Double = 0.0,
    @SerialName("ratings_count")
    val ratingsCount: Int = 0,
    @SerialName("platforms")
    val platforms: List<PlatformInfoDto> = emptyList(),
    @SerialName("genres")
    val genres: List<GenreDto> = emptyList(),
    @SerialName("description_raw")
    val descriptionRaw: String? = null,
    @SerialName("short_screenshots")
    val shortScreenshots: List<ScreenshotDto> = emptyList(),
    @SerialName("developers")
    val developers: List<DeveloperDto> = emptyList(),
    @SerialName("publishers")
    val publishers: List<PublisherDto> = emptyList(),
    @SerialName("esrb_rating")
    val esrbRating: EsrbRatingDto? = null,
    @SerialName("playtime")
    val playtime: Int = 0,
    @SerialName("tags")
    val tags: List<TagDto> = emptyList(),
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
    val released: String? = null,
    @SerialName("tba")
    val tba: Boolean? = null,
    @SerialName("background_image")
    val backgroundImage: String? = null,
    @SerialName("rating")
    val rating: Double,
    @SerialName("rating_top")
    val ratingTop: Int? = null,
    @SerialName("ratings")
    val ratings: List<RatingsDto>? = null,
    @SerialName("ratings_count")
    val ratingsCount: Int? = null,
    @SerialName("reviews_text_count")
    val reviewsTextCount: Int? = null,
    @SerialName("added")
    val added: Int? = null,
    @SerialName("added_by_status")
    val addedByStatus: AddedByStatusDto? = null,
    @SerialName("metacritic")
    val metacritic: Int? = null,
    @SerialName("playtime")
    val playtime: Int? = null,
    @SerialName("suggestions_count")
    val suggestionsCount: Int? = null,
    @SerialName("esrb_rating")
    val esrbRating: EsrbRatingDto? = null,
    @SerialName("platforms")
    val platforms: List<PlatformInfoDto>? = null,
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
    val yet: Int? = null,
    @SerialName("owned")
    val owned: Int? = null,
    @SerialName("beaten")
    val beaten: Int? = null,
    @SerialName("toplay")
    val toplay: Int? = null,
    @SerialName("dropped")
    val dropped: Int? = null,
    @SerialName("playing")
    val playing: Int? = null,
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
    val releasedAt: String? = null,
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

@Serializable
data class GenreDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("slug")
    val slug: String,
)

@Serializable
data class ScreenshotDto(
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String,
)

@Serializable
data class DeveloperDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class PublisherDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class TagDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("language")
    val language: String = "eng",
)
