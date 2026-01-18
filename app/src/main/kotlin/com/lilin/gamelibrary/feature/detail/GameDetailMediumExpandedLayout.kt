package com.lilin.gamelibrary.feature.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.domain.model.GameDetail
import com.lilin.gamelibrary.domain.model.Tag
import com.lilin.gamelibrary.ui.component.detail.DetailSectionHeader
import com.lilin.gamelibrary.ui.component.detail.GameDescription
import com.lilin.gamelibrary.ui.component.detail.GameTags
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientEnd
import com.lilin.gamelibrary.ui.theme.DetailDescriptionGradientStart
import com.lilin.gamelibrary.ui.theme.DetailRatingGradientStart
import com.lilin.gamelibrary.ui.theme.DetailTagGradientStart
import java.util.Locale

@Composable
fun GameDetailMediumExpandedLayout(
    gameDetail: GameDetail,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize(),
    ) {
        // 左側40%: 基本情報カラム
        GameDetailBasicInfoColumn(
            gameDetail = gameDetail,
            bottomBarPadding = bottomBarPadding,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
        )

        // 右側60%: コンテンツカラム
        GameDetailContentColumn(
            gameDetail = gameDetail,
            bottomBarPadding = bottomBarPadding,
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight(),
        )
    }
}

@Composable
private fun GameDetailBasicInfoColumn(
    gameDetail: GameDetail,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = bottomBarPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            GameImageSection(
                gameName = gameDetail.name,
                imageUrl = gameDetail.backgroundImage,
            )
        }

        item {
            BasicInfoSection(
                developers = gameDetail.developers.map { it.name },
                releaseYear = gameDetail.releaseDate,
                rating = gameDetail.rating,
                ratingsCount = gameDetail.ratingsCount,
                genres = gameDetail.genres.map { it.name },
            )
        }
    }
}

@Composable
private fun GameImageSection(
    gameName: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isNullOrEmpty()) {
        Image(
            painter = painterResource(R.drawable.ic_broken_image),
            contentDescription = stringResource(R.string.game_image_placeholder),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.game_image_description, gameName),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        )
    }
}

@Composable
private fun BasicInfoSection(
    developers: List<String>,
    releaseYear: String?,
    rating: Double,
    ratingsCount: Int,
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.detail_basic_info),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            HorizontalDivider()

            InfoRow(
                icon = Icons.Outlined.Category,
                label = stringResource(R.string.detail_label_genre),
                value = genres.joinToString(", "),
            )

            InfoRow(
                icon = Icons.Outlined.Code,
                label = stringResource(R.string.detail_label_developer),
                value = developers.joinToString(", "),
            )

            releaseYear?.let { releaseYear ->
                InfoRow(
                    icon = Icons.Outlined.CalendarToday,
                    label = stringResource(R.string.detail_label_release_date),
                    value = releaseYear,
                )
            }

            InfoRow(
                icon = Icons.Outlined.Star,
                label = stringResource(R.string.detail_label_rating),
                value = stringResource(R.string.game_rating_format, rating, formatCount(ratingsCount)),
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = "$label : ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// 60%
@Composable
private fun GameDetailContentColumn(
    gameDetail: GameDetail,
    bottomBarPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(bottom = bottomBarPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                gameDetail.description?.let { description ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        DetailSectionHeader(
                            title = stringResource(R.string.detail_section_description_title),
                            icon = Icons.Rounded.Description,
                            gradientColors = DetailDescriptionGradientStart to DetailDescriptionGradientEnd,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        GameDescription(
                            description = description,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            item {
                if (gameDetail.platformNames.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        DetailSectionHeader(
                            title = stringResource(R.string.detail_section_platforms_title),
                            icon = Icons.Outlined.Gamepad,
                            gradientColors = DetailDescriptionGradientEnd to DetailRatingGradientStart,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        PlatformChipsRow(
                            platforms = gameDetail.platformNames,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        )
                    }
                }
            }

            item {
                if (gameDetail.tags.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        DetailSectionHeader(
                            title = stringResource(R.string.detail_section_tags_title),
                            icon = Icons.Rounded.LocalOffer,
                            gradientColors = DetailRatingGradientStart to DetailTagGradientStart,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        GameTags(
                            tags = gameDetail.tags.map { it.name },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlatformChipsRow(
    platforms: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        platforms.forEach { platform ->
            AssistChip(
                onClick = {},
                label = { Text(text = platform) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Gamepad,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            )
        }
    }
}

/**
 * 数値をフォーマット（例：1234 → 1.2k）
 */
private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> {
            String.format(Locale.ROOT, "%.1f k", count / 1000.0)
        }

        else -> count.toString()
    }
}

@Preview(
    name = "Game Detail Medium Expanded Layout",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240",
)
@Composable
private fun GameDetailMediumExpandedLayoutPreview() {
    GameDetailMediumExpandedLayout(
        gameDetail = GameDetail(
            id = 3498,
            name = "Grand Theft Auto V",
            description = "Los Santos is a sprawling sun-soaked metropolis full of self-help gurus, " +
                "starlets and fading celebrities, once the envy of the Western world, " +
                "now struggling to stay afloat in an era of economic uncertainty and cheap reality TV.",
            releaseDate = "2013-09-17",
            backgroundImage = "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
            rating = 4.48,
            ratingsCount = 7300,
            metacritic = 92,
            platformNames = listOf("PlayStation 5", "Xbox Series S/X", "PlayStation 4", "PC"),
            developers = listOf(),
            genres = listOf(),
            tags = listOf(
                Tag(id = 1, name = "Action", slug = "action"),
                Tag(id = 2, name = "Open World", slug = "open-world"),
                Tag(id = 3, name = "Multiplayer", slug = "multiplayer"),
                Tag(id = 4, name = "Atmospheric", slug = "atmospheric"),
            ),
            esrbRating = "Mature 17+",
            websiteUrl = "",
            shortScreenshots = listOf(),
            publishers = listOf(),
            playtime = 50,
        ),
        bottomBarPadding = 0.dp,
    )
}

@Preview(showBackground = true)
@Composable
private fun GameImageSectionPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            GameImageSection(
                imageUrl = "https://example.com/image.jpg",
                gameName = "Grand Theft Auto V",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicInfoSectionPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BasicInfoSection(
                developers = listOf("Rockstar North", "Rockstar Games"),
                releaseYear = "2013-09-17",
                rating = 4.48,
                ratingsCount = 7300,
                genres = listOf("Action", "Adventure"),
            )
        }
    }
}

@Preview(
    name = "Game Basic Info Column",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp,dpi=240",
)
@Composable
private fun GameDetailBasicInfoColumnPreview() {
    MaterialTheme {
        GameDetailBasicInfoColumn(
            gameDetail = GameDetail(
                id = 3498,
                name = "Grand Theft Auto V",
                description = "Los Santos is a sprawling sun-soaked metropolis full of self-help gurus, " +
                    "starlets and fading celebrities, once the envy of the Western world, " +
                    "now struggling to stay afloat in an era of economic uncertainty and cheap reality TV.",
                releaseDate = "2013-09-17",
                backgroundImage = "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
                rating = 4.48,
                ratingsCount = 7300,
                metacritic = 92,
                platformNames = listOf("PlayStation 5", "Xbox Series S/X", "PlayStation 4", "PC"),
                developers = listOf(),
                genres = listOf(),
                tags = listOf(
                    Tag(id = 1, name = "Action", slug = "action"),
                    Tag(id = 2, name = "Open World", slug = "open-world"),
                    Tag(id = 3, name = "Multiplayer", slug = "multiplayer"),
                    Tag(id = 4, name = "Atmospheric", slug = "atmospheric"),
                ),
                esrbRating = "Mature 17+",
                websiteUrl = "",
                shortScreenshots = listOf(),
                publishers = listOf(),
                playtime = 50,
            ),
            bottomBarPadding = 0.dp,
        )
    }
}

@Preview(
    name = "Game Detail Content Column",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp,dpi=240",
)
@Composable
private fun GameDetailContentColumnPreview() {
    MaterialTheme {
        GameDetailContentColumn(
            gameDetail = GameDetail(
                id = 3498,
                name = "Grand Theft Auto V",
                description = "Los Santos is a sprawling sun-soaked metropolis full of self-help gurus, " +
                    "starlets and fading celebrities, once the envy of the Western world, " +
                    "now struggling to stay afloat in an era of economic uncertainty and cheap reality TV.",
                releaseDate = "2013-09-17",
                backgroundImage = "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
                rating = 4.48,
                ratingsCount = 7300,
                metacritic = 92,
                platformNames = listOf("PlayStation 5", "Xbox Series S/X", "PlayStation 4", "PC"),
                developers = listOf(),
                genres = listOf(),
                tags = listOf(
                    Tag(id = 1, name = "Action", slug = "action"),
                    Tag(id = 2, name = "Open World", slug = "open-world"),
                    Tag(id = 3, name = "Multiplayer", slug = "multiplayer"),
                    Tag(id = 4, name = "Atmospheric", slug = "atmospheric"),
                ),
                esrbRating = "Mature 17+",
                websiteUrl = "",
                shortScreenshots = listOf(),
                publishers = listOf(),
                playtime = 50,
            ),
            bottomBarPadding = 0.dp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlatformChipsRowPreview() {
    MaterialTheme {
        PlatformChipsRow(
            platforms = listOf("PlayStation 5", "Xbox Series S/X", "PlayStation 4", "PC"),
            modifier = Modifier.padding(16.dp),
        )
    }
}
