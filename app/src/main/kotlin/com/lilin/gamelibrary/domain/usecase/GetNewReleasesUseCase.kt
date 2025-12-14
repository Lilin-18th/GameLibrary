package com.lilin.gamelibrary.domain.usecase

import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.repository.GameRepository
import javax.inject.Inject

class GetNewReleasesUseCase @Inject constructor(
    private val repository: GameRepository,
) {
    /**
     * 新作ゲームを取得する
     *
     * @param page ページ番号（1から始まる）
     * @param pageSize 1ページあたりの件数
     * @return 成功時はゲームのリスト、失敗時はエラー情報を含むResult
     */
    suspend operator fun invoke(page: Int, pageSize: Int): Result<List<Game>> {
        return repository.getNewReleases(page, pageSize)
    }
}
