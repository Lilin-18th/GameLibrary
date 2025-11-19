package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.domain.model.Game

interface GameRepository {
    /**
     * 週間トレンドゲームのリストを取得します。
     *
     * @param page ページ番号（1から始まる）
     * @param pageSize 1ページあたりの件数
     * @return 成功時はゲームのリスト、失敗時はエラー情報を含むResult
     */
    suspend fun getTrendingGames(
        page: Int,
        pageSize: Int,
    ): Result<List<Game>>

    /**
     * 過去30日間の高評価ゲームのリストを取得します。
     *
     * @param page ページ番号（1から始まる）
     * @param pageSize 1ページあたりの件数
     * @return 成功時はゲームのリスト、失敗時はエラー情報を含むResult
     */
    suspend fun getHighRatedGames(
        page: Int,
        pageSize: Int,
    ): Result<List<Game>>

    /**
     * 過去30日間の新作ゲームのリストを取得します。
     *
     * @param page ページ番号（1から始まる）
     * @param pageSize 1ページあたりの件数
     * @return 成功時はゲームのリスト、失敗時はエラー情報を含むResult
     */
    suspend fun getNewReleases(
        page: Int,
        pageSize: Int,
    ): Result<List<Game>>
}
