package com.lilin.gamelibrary.domain.model

data class GameDetail(
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val releaseDate: String?,
    val metacritic: Int?,
    val rating: Double,
    val ratingsCount: Int,
    val platformNames: List<String>,
    val genres: List<Genre>,
    val description: String?,
    val shortScreenshots: List<Screenshot>,
    val developers: List<Developer>,
    val publishers: List<Publisher>,
    val esrbRating: String?,
    val playtime: Int,
    val tags: List<Tag>,
    val stores: List<StoreLink>,
    val updated: String?,
) {
    val metacriticScore = metacritic ?: 0
    val displayRating: String = "%.1f".format(rating)
}

/**
 * プラットフォームの情報
 * @param id
 * @param name "PlayStation 5"
 * @param slug 検索用: "playstation5"
 */
data class Platform(
    val id: Int,
    val name: String,
    val slug: String,
)

/**
 * ジャンルの情報
 * @param id
 * @param name "Action"
 * @param slug 検索用: "action"
 */
data class Genre(
    val id: Int,
    val name: String,
    val slug: String,
)

/**
 * 開発元の情報
 * @param id
 * @param name "Sora"
 */
data class Developer(
    val id: Int,
    val name: String,
)

/**
 * パブリッシャーの情報
 * @param id
 * @param name "Nintendo"
 */
data class Publisher(
    val id: Int,
    val name: String,
)

/**
 * タグ
 * @param id
 * @param name "Singleplayer"
 * @param slug 検索用: "Singleplayer"
 */
data class Tag(
    val id: Int,
    val name: String,
    val slug: String,
)

/**
 * スクリーンショット
 * @param id
 * @param imageUrl 画像リンク: "https://..."
 */
data class Screenshot(
    val id: Int,
    val imageUrl: String,
)

/**
 * ストアリンクの情報
 * @param id
 * @param storeName "PlayStation Store"
 * @param storeSlug 検索用: "playstation-store"
 * @param url "https://..."
 */
data class StoreLink(
    val id: Int,
    val storeName: String,
    val storeSlug: String,
    val url: String,
)

/**
 * 年齢レーティング
 * @param id
 * @param name "Mature 17+"
 */
data class EsrbRating(
    val id: Int,
    val name: String,
)