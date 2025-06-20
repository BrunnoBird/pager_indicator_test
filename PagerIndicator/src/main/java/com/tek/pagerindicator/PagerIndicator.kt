package com.tek.pagerindicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun PagerIndicatorKernel(
    pageCount: Int,
    currentIndex: Int,
    dotStyle: DotStyle = DotStyle.defaultDotStyle,
    dotAnimation: DotAnimation = DotAnimation.defaultDotAnimation,
    orientation: Orientation = Orientation.Vertical
) {
    //save page on config changes
    var page by rememberSaveable {
        mutableStateOf(currentIndex)
    }
    //save displayed range on config changes
    var range by rememberSaveable {
        val start = when {
            pageCount <= dotStyle.visibleDotCount -> 0
            else -> {
                val half = dotStyle.visibleDotCount / 2
                val maxStart = pageCount - dotStyle.visibleDotCount
                val desired = currentIndex - half
                kotlin.math.min(kotlin.math.max(desired, 0), maxStart)
            }
        }
        mutableStateOf(
            RangeChanged(start, kotlin.math.min(start + dotStyle.visibleDotCount - 1, pageCount - 1))
        )
    }

    fun updateRange(index: Int) {
        if (index == range.endIndex && index != pageCount - 1) {
            range = RangeChanged(
                startIndex = range.startIndex + 1,
                endIndex = range.endIndex + 1
            )

        } else if (index == range.startIndex && index != 0) {
            range = RangeChanged(
                startIndex = range.startIndex - 1,
                endIndex = range.endIndex - 1
            )
        }

    }

    LaunchedEffect(currentIndex) {
        page = currentIndex
        updateRange(currentIndex)
    }

    fun colorForIndex(index: Int) =
        if (index == page) dotStyle.currentDotColor else dotStyle.regularDotColor

    fun sizeForIndex(index: Int): Float {
        return when (index) {
            page -> dotStyle.currentDotRadius
            range.startIndex -> if (range.startIndex != 0) dotStyle.notLastDotRadius else dotStyle.regularDotRadius
            range.endIndex -> if (range.endIndex != pageCount - 1) dotStyle.notLastDotRadius else dotStyle.regularDotRadius
            else -> dotStyle.regularDotRadius
        }
    }

    val space = with(LocalDensity.current) { dotStyle.dotMargin.toDp() }

    if (orientation == Orientation.Vertical) {
        Row(horizontalArrangement = Arrangement.spacedBy(space)) {
            for (i in range.startIndex..range.endIndex) {
                val targetRadius = sizeForIndex(i)
                val sizeAnim = animateDpAsState(
                    targetValue = with(LocalDensity.current) { (targetRadius * 2).toDp() },
                    animationSpec = dotAnimation.sizeAnim
                )
                val colorAnim = animateColorAsState(
                    targetValue = colorForIndex(i),
                    animationSpec = dotAnimation.colorAnim
                )
                Box(
                    modifier = Modifier
                        .size(sizeAnim.value)
                        .clip(CircleShape)
                        .background(colorAnim.value)
                )
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(space)) {
            for (i in range.startIndex..range.endIndex) {
                val targetRadius = sizeForIndex(i)
                val sizeAnim = animateDpAsState(
                    targetValue = with(LocalDensity.current) { (targetRadius * 2).toDp() },
                    animationSpec = dotAnimation.sizeAnim
                )
                val colorAnim = animateColorAsState(
                    targetValue = colorForIndex(i),
                    animationSpec = dotAnimation.colorAnim
                )
                Box(
                    modifier = Modifier
                        .size(sizeAnim.value)
                        .clip(CircleShape)
                        .background(colorAnim.value)
                )
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(
    modifier: Modifier,
    pagerState: PagerState,
    dotStyle: DotStyle = DotStyle.defaultDotStyle,
    dotAnimation: DotAnimation = DotAnimation.defaultDotAnimation,
    orientation: Orientation = Orientation.Vertical
) {
    PagerIndicator(
        modifier = modifier,
        pageCount = pagerState.pageCount,
        currentIndex = pagerState.currentPage,
        dotStyle = dotStyle,
        dotAnimation = dotAnimation,
        orientation = orientation
    )
}

@Composable
fun PagerIndicator(
    modifier: Modifier,
    pageCount: Int,
    currentIndex: Int,
    dotStyle: DotStyle = DotStyle.defaultDotStyle,
    dotAnimation: DotAnimation = DotAnimation.defaultDotAnimation,
    orientation: Orientation = Orientation.Vertical
) {
    Box(modifier = modifier) {
        PagerIndicatorKernel(
            pageCount = pageCount,
            currentIndex = currentIndex,
            orientation = orientation,
            dotStyle = dotStyle,
            dotAnimation = dotAnimation
        )
    }
}