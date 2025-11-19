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
        val (startDate, endDate) = getCurrentWeekDates()

        return runCatching {
            fetchGames(page, pageSize, startDate, endDate, "-added")
        }
    }

    override suspend fun getHighRatedGames(page: Int, pageSize: Int): Result<List<Game>> {
        val (startDate, endDate) = getLast30DaysDates()
        return runCatching {
            fetchGames(page, pageSize, startDate, endDate, "-rating")
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
        startDate: String,
        endDate: String,
        ordering: String,
    ): List<Game> {
        val response = apiService.getGames(
            page = page,
            pageSize = pageSize,
            dates = "$startDate,$endDate",
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

    @OptIn(ExperimentalTime::class)
    private fun getCurrentWeekDates(): Pair<String, String> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val dayOfWeek = today.dayOfWeek.ordinal

        val startOfWeek = today.minus(dayOfWeek, DateTimeUnit.DAY)
        val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)

        return Pair(startOfWeek.toString(), endOfWeek.toString())
    }
}