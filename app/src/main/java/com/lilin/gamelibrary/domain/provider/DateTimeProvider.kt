package com.lilin.gamelibrary.domain.provider

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface DateTimeProvider {
    fun today(): LocalDate
}

class DefaultDateTimeProvider : DateTimeProvider {
    @OptIn(ExperimentalTime::class)
    override fun today(): LocalDate {
        return Clock.System.todayIn(TimeZone.currentSystemDefault())
    }
}