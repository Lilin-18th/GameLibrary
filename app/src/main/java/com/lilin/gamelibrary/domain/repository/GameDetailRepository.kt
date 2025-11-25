package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.domain.model.GameDetail

interface GameDetailRepository {
    /**
     * ゲームの詳細情報取得
     * @param gameId 取得対象のゲームのID
     * @return ゲームの詳細情報
     */
    suspend fun getGameDetail(gameId: Int): Result<GameDetail>
}
