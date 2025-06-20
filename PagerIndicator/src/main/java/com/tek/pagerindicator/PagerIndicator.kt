package com.tek.pagerindicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun PagerIndicatorKernel(
    pageCount: Int,
    currentIndex: Int,
    intSize: IntSize,
    dotStyle: DotStylePx,
    dotAnimation: DotAnimation = DotAnimation.defaultDotAnimation
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
            val step = kotlin.math.min(RANGE_STEP, pageCount - 1 - range.endIndex)
            range = RangeChanged(
                startIndex = range.startIndex + step,
                endIndex = range.endIndex + step
            )

        } else if (index == range.startIndex && index != 0) {
            val step = kotlin.math.min(RANGE_STEP, range.startIndex)
            range = RangeChanged(
                startIndex = range.startIndex - step,
                endIndex = range.endIndex - step
            )
        }

    }

    val indicatorController =
        rememberIndicatorController(
            count = pageCount,
            size = intSize,
            dotStyle = dotStyle,
            startIndex = page,
            startRange = range.startIndex..range.endIndex

        )

    LaunchedEffect(currentIndex) {
        indicatorController.pageChanged(currentIndex)
        page = currentIndex
        updateRange(currentIndex)
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
            val width = indicatorController.sizes[i].value * 2
            val height = dotStyle.regularDotRadius * 2
            val topLeft = indicatorController.offSets[i].value -
                Offset(width / 2f, height / 2f)

            drawRoundRect(
                color = indicatorController.colors[i].value,
                topLeft = topLeft,
                size = Size(width, height),
                cornerRadius = CornerRadius(dotStyle.regularDotRadius)
            )
        }
    })
}

@Composable
fun PagerIndicator(
    modifier: Modifier,
    pageCount: Int,
    currentIndex: Int,
    dotStyle: DotStyle = DotStyle.defaultDotStyle,
    dotAnimation: DotAnimation = DotAnimation.defaultDotAnimation
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val h = this.maxHeight
        val w = this.maxWidth
        val stylePx = dotStyle.toPx(density)
        PagerIndicatorKernel(
            pageCount = pageCount,
            currentIndex = currentIndex,
            intSize = with(density) {
                IntSize(
                    w.toPx().toInt(),
                    h.toPx().toInt()
                )
            },
            dotStyle = stylePx,
            dotAnimation = dotAnimation
        )

    }
}