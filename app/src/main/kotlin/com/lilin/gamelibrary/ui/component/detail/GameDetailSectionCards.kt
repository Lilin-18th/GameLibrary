package com.lilin.gamelibrary.ui.component.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lilin.gamelibrary.R
import com.lilin.gamelibrary.ui.component.discovery.getMetacriticColor
import java.util.Locale

@Composable
fun GameBackgroundImage(
    imageUrl: String?,
    contentDescription: String?,
    metacriticScore: Int,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imageHeight = max(280.dp, min(screenHeight * 0.4f, 400.dp))
    val badgeColor = getMetacriticColor(metacriticScore)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl.isNullOrBlank()) {
            Image(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f),
                        ),
                    ),
                ),
        )

        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
                .size(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = badgeColor.copy(alpha = 0.3f),
                    spotColor = badgeColor.copy(alpha = 0.3f),
                ),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = badgeColor,
            ),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = metacriticScore.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun GameBasicInfo(
    gameName: String,
    developers: List<String>,
    releaseYear: String?,
    modifier: Modifier = Modifier,
) {
    val subtitle = buildString {
        if (!developers.isNotEmpty()) {
            append(developers.joinToString(", "))
        }
        if (!developers.isNotEmpty() && releaseYear != null) {
            append("・")
        }
        if (releaseYear != null) {
            append(releaseYear)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Text(
            text = gameName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 32.sp,
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
fun GameInfoCard(
    rating: Double,
    ratingsCount: Int,
    platforms: List<String>,
    genres: List<String>,
    releaseDate: String?,
    esrbRating: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(20.dp),
        ) {
            GameRatingSummary(
                rating = rating,
                ratingsCount = ratingsCount,
                platforms = platforms,
                modifier = Modifier.weight(1f),
            )
            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            GameMetadataInfo(
                genres = genres,
                releaseDate = releaseDate,
                esrbRating = esrbRating,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun GameRatingSummary(
    rating: Double,
    ratingsCount: Int,
    platforms: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // 評価行
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = "%.1f".format(rating),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "(${formatCount(ratingsCount)} ratings)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // プラットフォーム行
        Row(
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Rounded.Games,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp),
            )

            Spacer(Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                platforms.forEach { platform ->
                    Text(
                        text = platform,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun GameMetadataInfo(
    genres: List<String>,
    releaseDate: String?,
    esrbRating: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (genres.isNotEmpty()) {
            MetaDataInfoRow(
                label = stringResource(R.string.detail_label_genre),
                content = genres.joinToString(", "),
            )
        }

        releaseDate?.let { date ->
            MetaDataInfoRow(
                label = stringResource(R.string.detail_label_release_date),
                content = date,
            )
        }

        esrbRating?.let { esrb ->
            MetaDataInfoRow(
                label = stringResource(R.string.detail_label_esrb_rating),
                content = esrb,
            )
        }
    }
}

@Composable
private fun MetaDataInfoRow(
    label: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = content,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
fun GameDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            text = description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium,
                    ),
                ),
        )

        Spacer(Modifier.height(8.dp))
        Text(
            text = if (isExpanded) {
                stringResource(R.string.detail_description_button_collapse)
            } else {
                stringResource(R.string.detail_description_button_expand)
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded },
        )
    }
}

@Composable
fun GameTags(
    tags: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tags.forEach { tag ->
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = tag,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = SuggestionChipDefaults.suggestionChipColors(),
                )
            }
        }
    }
}

/**
 * 数値をフォーマット（例：1234 → 1.2k）
 */
private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> {
            String.format(Locale.ROOT, "%.1fk", count / 1000.0)
        }

        else -> count.toString()
    }
}

@Preview
@Composable
private fun GameBackgroundImage90ScorePreview() {
    GameBackgroundImage(
        imageUrl = null,
        contentDescription = "The Witcher 3: Wild Hunt",
        metacriticScore = 90,
    )
}

@Preview
@Composable
private fun GameBackgroundImage70ScorePreview() {
    GameBackgroundImage(
        imageUrl = null,
        contentDescription = "The Witcher 3: Wild Hunt",
        metacriticScore = 70,
    )
}

@Preview
@Composable
private fun GameBackgroundImage50ScorePreview() {
    GameBackgroundImage(
        imageUrl = null,
        contentDescription = "The Witcher 3: Wild Hunt",
        metacriticScore = 50,
    )
}

@Preview
@Composable
private fun GameInfoCardPreview() {
    GameInfoCard(
        rating = 5.50,
        ratingsCount = 1500,
        platforms = listOf("Switch", "PS5", "xbox"),
        genres = listOf("Action", "Adventure", "RPG"),
        releaseDate = "2023-05-15",
        esrbRating = "E10+",
    )
}

@Preview
@Composable
private fun GameBasicInfoPreview() {
    GameBasicInfo(
        gameName = "The Last of Us Part II",
        developers = listOf("Naughty Dog"),
        releaseYear = "2020",
    )
}

@Preview(showBackground = true)
@Composable
private fun GameRatingSummaryPreview() {
    GameRatingSummary(
        rating = 8.5,
        ratingsCount = 10000,
        platforms = listOf("Xbox One", "PC", "PlayStation 4"),
    )
}

@Preview(showBackground = true)
@Composable
private fun GameMetadataInfoPreview() {
    GameMetadataInfo(
        genres = listOf("Action", "Adventure", "RPG"),
        releaseDate = "2023-05-15",
        esrbRating = "E10+",
    )
}

@Preview
@Composable
private fun GameDescriptionPreview() {
    GameDescription(
        description = "説明",
    )
}

@Preview
@Composable
private fun GameTagsPreview() {
    GameTags(
        tags = listOf("Action", "Adventure", "RPG", "Indie", "Sandbox", "Strategy"),
    )
}
