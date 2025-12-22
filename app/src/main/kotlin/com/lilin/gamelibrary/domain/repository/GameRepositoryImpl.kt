package com.lilin.gamelibrary.domain.repository

import com.lilin.gamelibrary.data.api.ApiServices
import com.lilin.gamelibrary.data.mapper.toDomainList
import com.lilin.gamelibrary.domain.model.Game
import com.lilin.gamelibrary.domain.model.SearchResult
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GameRepositoryImpl @Inject constructor(
    private val apiService: ApiServices,
) : GameRepository {

    override suspend fun getTrendingGames(page: Int, pageSize: Int): Result<List<Game>> {
        return runCatching {
            fetchGames(page, pageSize, null, null, null, "-added")
        }
    }

    override suspend fun getHighMetacriticScoreGames(page: Int, pageSize: Int): Result<List<Game>> {
        return runCatching {
            fetchGames(page, pageSize, null, null, null, "-metacritic")
        }
    }

    override suspend fun getNewReleases(
        page: Int,
        pageSize: Int,
    ): Result<List<Game>> {
        val (startDate, endDate) = getLast30DaysDates()

        return runCatching {
            fetchGames(page, pageSize, startDate, endDate, null, "-released")
        }
    }

    override suspend fun getSearchGameResults(
        page: Int,
        pageSize: Int,
        searchText: String,
    ): Result<SearchResult> {
        val search = searchText.ifBlank { null }

        return runCatching {
            val response = apiService.getGames(
                page = page,
                pageSize = pageSize,
                dates = null,
                search = search,
                ordering = "relevance,-metacritic",
            )

            return runCatching {
                if (response.isSuccessful) {
                    val body = response.body()
                    SearchResult(
                        games = body?.toDomainList() ?: emptyList(),
                        currentPage = page,
                        hasNextPage = body?.next != null,
                    )
                } else {
                    throw IOException("API Error: ${response.code()}")
                }
            }
        }
    }

    private suspend fun fetchGames(
        page: Int,
        pageSize: Int,
        startDate: String?,
        endDate: String?,
        search: String?,
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
            search = search,
            ordering = ordering,
        )
        return if (response.isSuccessful) {
            response.body()?.toDomainList() ?: emptyList()
        } else {
            throw IOException("API Error: ${response.code()}")
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getLast30DaysDates(): Pair<String, String> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val thirtyDaysAgo = today.minus(THIRTY_DAYS, DateTimeUnit.DAY)

        return Pair(thirtyDaysAgo.toString(), today.toString())
    }

    private companion object {
        const val THIRTY_DAYS = 30
    }
}
