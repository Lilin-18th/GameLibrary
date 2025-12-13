package com.lilin.gamelibrary.data.api

import com.lilin.gamelibrary.data.dto.GamesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String?,
        @Query("dates") dates: String?,
        @Query("ordering") ordering: String,
    ): Response<GamesResponse>
}
