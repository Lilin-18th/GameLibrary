package com.lilin.gamelibrary.data.mapper

import com.lilin.gamelibrary.data.dto.DeveloperDto
import com.lilin.gamelibrary.data.dto.GenreDto
import com.lilin.gamelibrary.data.dto.PlatformInfoDto
import com.lilin.gamelibrary.data.dto.PublisherDto
import com.lilin.gamelibrary.data.dto.ScreenshotDto
import com.lilin.gamelibrary.data.dto.TagDto
import com.lilin.gamelibrary.domain.model.Developer
import com.lilin.gamelibrary.domain.model.Genre
import com.lilin.gamelibrary.domain.model.Publisher
import com.lilin.gamelibrary.domain.model.Screenshot
import com.lilin.gamelibrary.domain.model.Tag

fun PlatformInfoDto.toPlatformName(): String {
    return platform.name
}

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name,
        slug = slug,
    )
}

fun ScreenshotDto.toScreenshot(): Screenshot {
    return Screenshot(
        id = id,
        imageUrl = image,
    )
}

fun DeveloperDto.toDeveloper(): Developer {
    return Developer(
        id = id,
        name = name,
    )
}

fun PublisherDto.toPublisher(): Publisher {
    return Publisher(
        id = id,
        name = name,
    )
}

fun TagDto.toTag(): Tag {
    return Tag(
        id = id,
        name = name,
        slug = slug,
    )
}
