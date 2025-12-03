package com.lilin.gamelibrary.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.lilin.gamelibrary.domain.model.GameDetail
import com.valentinilk.shimmer.shimmer

// セクション2: 背景画像
@Composable
fun GameBackgroundImageSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    GameBackgroundImage(
        imageUrl = gameDetail.backgroundImage,
        contentDescription = gameDetail.name,
        metacriticScore = gameDetail.metacriticScore,
        modifier = modifier.fillMaxWidth(),
    )
}

// セクション3: 基本情報
@Composable
fun GameBasicInfoSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    GameBasicInfo(
        gameName = gameDetail.name,
        developers = gameDetail.developers.map {
            it.name
        },
        releaseYear = gameDetail.releaseDate.toString(),
        modifier = modifier.fillMaxWidth(),
    )
}

// セクション4: 評価サマリー メタデータ情報
@Composable
fun GameRatingSummarySection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    GameInfoCard(
        rating = gameDetail.rating,
        ratingsCount = gameDetail.ratingsCount,
        platforms = gameDetail.platformNames,
        genres = gameDetail.genres.map { it.name },
        releaseDate = gameDetail.releaseDate,
        esrbRating = gameDetail.esrbRating,
        modifier = modifier.fillMaxWidth(),
    )
}

// セクション6: 説明
@Composable
fun GameDescriptionSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    gameDetail.description?.let {
        GameDescription(
            description = it,
            modifier = modifier.fillMaxWidth(),
        )
    }
}

// セクション7: スクリーンショット
@Composable
fun GameScreenshotsSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    if (gameDetail.shortScreenshots.isNotEmpty()) {
        GameScreenshots(
            screenshots = gameDetail.shortScreenshots.map { it.imageUrl },
            modifier = modifier.fillMaxWidth(),
        )
    }
}

// セクション8: タグ
@Composable
fun GameTagsSection(
    gameDetail: GameDetail,
    modifier: Modifier = Modifier,
) {
    if (gameDetail.tags.isNotEmpty()) {
        GameTags(
            tags = gameDetail.tags.map { it.name },
            modifier = modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun SkeletonGameBackgroundImageSection(
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imageHeight = max(280.dp, min(screenHeight * 0.4f, 400.dp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmer()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
    }
}

// セクション3: 基本情報 Skeleton
@Composable
fun SkeletonGameBasicInfoSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        // タイトル
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(28.dp)
                .shimmer()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp),
                ),
        )

        Spacer(Modifier.height(8.dp))

        // サブタイトル
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(16.dp)
                .shimmer()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp),
                ),
        )
    }
}

// セクション4: 評価サマリー & メタデータ Skeleton
@Composable
fun SkeletonGameRatingSummarySection(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
        ) {
            // 左側: 評価サマリー
            Column(
                modifier = Modifier
                    .weight(1f)
                    .shimmer(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(20.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp),
                        ),

                    )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp),
                        ),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp),
                        ),
                )
            }

            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // 右側: メタデータ
            Column(
                modifier = Modifier
                    .weight(1f)
                    .shimmer(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                repeat(3) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(14.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp),
                                ),
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(16.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp),
                                ),
                        )
                    }
                }
            }
        }
    }
}

// セクション6: 説明 Skeleton
@Composable
fun SkeletonGameDescriptionSection(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            // ラベル
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(14.dp)
                    .shimmer()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp),
                    ),
            )

            Spacer(Modifier.height(8.dp))

            // 説明文（3行）
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .shimmer()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp),
                        ),
                )
                Spacer(Modifier.height(6.dp))
            }

            // ボタン
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(14.dp)
                    .shimmer()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp),
                    ),
            )
        }
    }
}

// セクション7: スクリーンショット Skeleton
@Composable
fun SkeletonGameScreenshotsSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        // ラベル
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(14.dp)
                .shimmer()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp),
                ),
        )

        Spacer(Modifier.height(8.dp))

        // スクリーンショットリスト
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(3) {
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .height(160.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    )
                }
            }
        }
    }
}

// セクション8: タグ Skeleton
@Composable
fun SkeletonGameTagsSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        // ラベル
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(14.dp)
                .shimmer()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp),
                ),
        )

        Spacer(Modifier.height(8.dp))

        // タグチップ
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .width((60..120).random().dp)
                        .height(32.dp)
                        .shimmer()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp),
                        ),
                )
            }
        }
    }
}
