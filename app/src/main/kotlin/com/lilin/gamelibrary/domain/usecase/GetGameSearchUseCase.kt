package com.lilin.gamelibrary.domain.usecase

import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.repository.GameRepository

class GetGameSearchUseCase(
    private val gameRepository: GameRepository,
) {
    /**
     * ゲームの検索結果を取得する
     *
     * @param page ページ番号（1から始まる）
     * @param pageSize 1ページあたりの件数
     * @param searchText 検索キーワード
     * @return 成功時はゲームのリスト、失敗時はエラー情報を含むResult
     */
    suspend operator fun invoke(page: Int, pageSize: Int, searchText: String): Result<List<Game>> {
        return gameRepository.getSearchGameResults(page, pageSize, searchText)
    }
}
