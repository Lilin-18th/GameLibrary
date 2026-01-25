package com.lilin.gamelibrary.data.repository

import com.lilin.gamelibrary.data.local.dao.FavoriteGameDao
import com.lilin.gamelibrary.data.local.entity.FavoriteGameEntity
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteGameRepository @Inject constructor(
    private val favoriteGameDao: FavoriteGameDao,
) {
    fun getFavoriteGames(order: SortOrder): Flow<List<FavoriteGame>> {
        return when (order) {
            SortOrder.NEWEST_FIRST -> favoriteGameDao.getFavoriteGamesNewestFirst()
            SortOrder.OLDEST_FIRST -> favoriteGameDao.getFavoriteGamesOldestFirst()
        }.map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getFavoriteGames2(order: SortOption): Flow<List<FavoriteGame>> {
        return when (order) {
            SortOption.ADDED_DATE_DESC -> favoriteGameDao.getFavoriteGamesNewestFirst()
            SortOption.ADDED_DATE_ASC -> favoriteGameDao.getFavoriteGamesOldestFirst()
            SortOption.TITLE_DESC -> favoriteGameDao.getFavoriteGamesByTitleDesc()
            SortOption.TITLE_ASC -> favoriteGameDao.getFavoriteGamesByTitleAsc()
            SortOption.RATING_DESC -> favoriteGameDao.getFavoriteGamesByRatingDesc()
            SortOption.RATING_ASC -> favoriteGameDao.getFavoriteGamesByRatingAsc()
        }.map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun isFavorite(gameId: Int): Flow<Boolean> {
        return favoriteGameDao.isFavorite(gameId)
    }

    suspend fun insertFavoriteGame(game: FavoriteGame) {
        favoriteGameDao.insertFavoriteGame(game.toEntity())
    }

    suspend fun deleteFavoriteGame(gameId: Int) {
        favoriteGameDao.deleteFavoriteGame(gameId)
    }
}

private fun FavoriteGameEntity.toDomainModel(): FavoriteGame {
    return FavoriteGame(
        id = id,
        name = name,
        backgroundImage = backgroundImage,
        rating = rating,
        metacritic = metacritic,
        released = released,
        addedAt = addedAt,
    )
}

private fun FavoriteGame.toEntity(): FavoriteGameEntity {
    return FavoriteGameEntity(
        id = id,
        name = name,
        backgroundImage = backgroundImage,
        rating = rating,
        metacritic = metacritic,
        released = released,
        addedAt = addedAt,
    )
}
