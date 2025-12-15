package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import com.lilin.gamelibrary.domain.model.FavoriteGame
import javax.inject.Inject

/**
 * ゲームをお気に入りに追加するUseCase
 *
 * 同じIDのゲームが既に存在する場合は上書きされる。
 *
 * @param game お気に入りに追加するゲーム
 */
class AddFavoriteGameUseCase @Inject constructor(
    private val repository: FavoriteGameRepository,
) {
    suspend operator fun invoke(game: FavoriteGame) {
        repository.insertFavoriteGame(game)
    }
}
