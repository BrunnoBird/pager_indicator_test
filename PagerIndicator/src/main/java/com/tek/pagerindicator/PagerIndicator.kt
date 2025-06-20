package com.tek.pagerindicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun PagerIndicatorKernel(
    pageCount: Int,
    currentIndex: Int,
    intSize: IntSize,
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

    val indicatorController =
        rememberIndicatorController(
            count = pageCount,
            size = intSize,
            dotStyle = dotStyle,
            orientation = orientation,
            startIndex = page,
            startRange = range.startIndex..range.endIndex

        )

    LaunchedEffect(currentIndex) {
        indicatorController.transitionStart()
        indicatorController.pageChanged(currentIndex)
        page = currentIndex
        updateRange(currentIndex)
        delay(300)
        indicatorController.transitionEnd()
    }


    indicatorController.clearAll()
    for (i in 0 until pageCount) {
        indicatorController.sizes.add(
            animateFloatAsState(
                targetValue = indicatorController.sizeTargets[i],
                dotAnimation.sizeAnim
            )
        )
        indicatorController.offSets.add(
            animateOffsetAsState(
                targetValue = indicatorController.offsetTargets[i],
                dotAnimation.offsetAnim
            )
        )
        indicatorController.colors.add(
            animateColorAsState(
                targetValue = indicatorController.colorTargets[i],
                dotAnimation.colorAnim
            )
        )
    }


    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        for (i in 0 until pageCount) {
            drawCircle(
                indicatorController.colors[i].value,
                radius = indicatorController.sizes[i].value,
                center = indicatorController.offSets[i].value
            )
        }
    })
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
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val h = this.maxHeight
        val w = this.maxWidth
        PagerIndicatorKernel(
            pageCount = pageCount,
            currentIndex = currentIndex,
            intSize = with(density) {
                IntSize(
                    w.toPx().toInt(),
                    h.toPx().toInt()
                )
            },
            orientation = orientation,
            dotStyle = dotStyle,
            dotAnimation = dotAnimation
        )

    }
}