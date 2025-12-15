package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.data.api.ApiServices
import com.lilin.gamelibrary.data.mapper.toDomain
import com.lilin.gamelibrary.domain.model.GameDetail
import java.io.IOException
import javax.inject.Inject

class GameDetailRepositoryImpl @Inject constructor(
    private val apiService: ApiServices,
) : GameDetailRepository {
    override suspend fun getGameDetail(gameId: Int): Result<GameDetail> {
        return runCatching {
            val response = apiService.getGameDetail(gameId)

            if (response.isSuccessful) {
                response.body()?.toDomain() ?: throw NoSuchElementException("Empty body")
            } else {
                throw IOException("API Error: ${response.code()}")
            }
        }
    }
}
