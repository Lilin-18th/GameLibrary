package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import javax.inject.Inject

/**
 * お気に入りからゲームを削除するUseCase
 *
 * 指定されたIDのゲームをお気に入りから削除する。
 * 存在しないIDが指定された場合は何も行わない。
 *
 * @param gameId 削除するゲームのID
 */
class RemoveFavoriteGameUseCase @Inject constructor(
    private val repository: FavoriteGameRepository,
) {
    suspend operator fun invoke(gameId: Int) {
        repository.deleteFavoriteGame(gameId)
    }
}
