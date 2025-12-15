package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ゲームがお気に入り登録されているか確認するUseCase
 *
 * 指定されたIDのゲームがお気に入りに登録されているかをチェックする。
 * Flowを返すため、お気に入り状態の変化が自動的にUIに反映される。
 *
 * @param gameId 確認するゲームのID
 * @return お気に入り登録されている場合はtrue、そうでない場合はfalseを流すFlow
 */
class IsFavoriteGameUseCase @Inject constructor(
    private val repository: FavoriteGameRepository,
) {
    operator fun invoke(gameId: Int): Flow<Boolean> {
        return repository.isFavorite(gameId)
    }
}
