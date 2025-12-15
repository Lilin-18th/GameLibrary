package com.lilin.gamelibrary.domain.usecase

import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.domain.repository.GameDetailRepository
import javax.inject.Inject

class GetGameDetailUseCase @Inject constructor(
    private val gameDetailRepository: GameDetailRepository,
) {
    /**
     * ゲームの詳細情報を取得する
     *
     * @param gameId 取得対象のゲームのID
     * @return ゲームの詳細情報
     */
    suspend operator fun invoke(id: Int): Result<GameDetail> {
        return gameDetailRepository.getGameDetail(id)
    }
}
