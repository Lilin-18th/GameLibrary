package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.data.api.ApiServices
import com.lilin.gamelibrary.data.mapper.toDomainList
import com.lilin.gamelibrary.domain.model.Game
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GameRepositoryImpl @Inject constructor(
    private val apiService: ApiServices,
) : GameRepository {

    override suspend fun getTrendingGames(page: Int, pageSize: Int): Result<List<Game>> {
        return runCatching {
            fetchGames(page, pageSize, null, null, "-added")
        }
    }

    override suspend fun getHighMetacriticScoreGames(page: Int, pageSize: Int): Result<List<Game>> {
        return runCatching {
            fetchGames(page, pageSize, null, null, "-metacritic")
        }
    }

    override suspend fun getNewReleases(
        page: Int,
        pageSize: Int,
    ): Result<List<Game>> {
        val (startDate, endDate) = getLast30DaysDates()
        return runCatching {
            fetchGames(page, pageSize, startDate, endDate, "-released")
        }
    }

    private suspend fun fetchGames(
        page: Int,
        pageSize: Int,
        startDate: String?,
        endDate: String?,
        ordering: String,
    ): List<Game> {
        val dates = if (startDate != null && endDate != null) {
            "$startDate,$endDate"
        } else {
            null
        }

        val response = apiService.getGames(
            page = page,
            pageSize = pageSize,
            dates = dates,
            ordering = ordering,
        )
        return if (response.isSuccessful) {
            response.body()?.toDomainList() ?: emptyList()
        } else {
            throw Exception("API Error: ${response.code()}")
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getLast30DaysDates(): Pair<String, String> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val thirtyDaysAgo = today.minus(30, DateTimeUnit.DAY)

        return Pair(thirtyDaysAgo.toString(), today.toString())
    }
}