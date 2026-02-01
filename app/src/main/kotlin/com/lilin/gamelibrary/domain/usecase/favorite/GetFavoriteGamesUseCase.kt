package com.lilin.gamelibrary.domain.usecase.favorite

import com.lilin.gamelibrary.data.repository.FavoriteGameRepository
import com.lilin.gamelibrary.domain.model.FavoriteGame
import com.lilin.gamelibrary.domain.model.SortOption
import com.lilin.gamelibrary.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * お気に入りゲーム一覧を取得するUseCase
 *
 * 指定されたソート順でお気に入り登録されたゲームのリストを取得する。
 * Flowを返すため、データベースの変更が自動的にUIに反映される。
 *
 * @param sortOption ソート順
 * @return お気に入りゲームのリストを流すFlow
 */
class GetFavoriteGamesUseCase @Inject constructor(
    private val repository: FavoriteGameRepository,
) {
    operator fun invoke(sortOption: SortOption): Flow<List<FavoriteGame>> {
        return repository.getFavoriteGames2(sortOption)
    }
}
