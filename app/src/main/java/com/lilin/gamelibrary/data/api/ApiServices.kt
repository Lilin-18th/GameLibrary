package com.lilin.gamelibrary.data.api

import com.lilin.gamelibrary.data.dto.GamesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface ApiServices {
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): Response<GamesResponse>
}
