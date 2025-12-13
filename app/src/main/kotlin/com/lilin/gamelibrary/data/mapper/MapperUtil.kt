package com.lilin.gamelibrary.data.mapper

import com.lilin.gamelibrary.data.dto.PlatformInfoDto

fun PlatformInfoDto.toPlatformName(): String {
    return platform.name
}
