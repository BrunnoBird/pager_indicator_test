package com.tek.pagerindicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
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
    var page by rememberSaveable {
        mutableStateOf(currentIndex)
    }
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
            RangeChanged(
                start,
                kotlin.math.min(start + dotStyle.visibleDotCount - 1, pageCount - 1)
            )
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
        indicatorController.alphas.add(
            animateFloatAsState(
                targetValue = indicatorController.alphaTargets[i],
                dotAnimation.alphaAnim
            )
        )
    }


    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        for (i in 0 until pageCount) {
            val radius = indicatorController.sizes[i].value
            val width = radius * 2
            val height = if (indicatorController.alphas[i].value < 1f) {
                radius * 2
            } else {
                dotStyle.regularDotRadius * 2
            }
            val topLeft = indicatorController.offSets[i].value -
                    Offset(width / 2f, height / 2f)

            drawRoundRect(
                color = indicatorController.colors[i].value,
                topLeft = topLeft,
                size = Size(width, height),
                cornerRadius = CornerRadius(dotStyle.regularDotRadius),
                alpha = indicatorController.alphas[i].value
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
    dotAnimation: DotAnimation = DotAnimation.defaultDotAnimation,
    hasArrow: Boolean = true,
    onIndexChange: (Int) -> Unit = {}
) {
    val density = LocalDensity.current
    val stylePx = dotStyle.toPx(density)
    val height = 24.dp
    val widthDots = dotStyle.contentWidth()
    val size = with(density) { IntSize(widthDots.toPx().toInt(), height.toPx().toInt()) }

    Row(
        modifier = modifier.height(height),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasArrow) {
            IconButton(
                onClick = { if (currentIndex > 0) onIndexChange(currentIndex - 1) },
                enabled = currentIndex > 0,
                modifier = Modifier.size(height)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Prev")
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier
                .width(widthDots)
                .height(height)
        ) {
            PagerIndicatorKernel(
                pageCount = pageCount,
                currentIndex = currentIndex,
                intSize = size,
                dotStyle = stylePx,
                dotAnimation = dotAnimation
            )
        }

        if (hasArrow) {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = { if (currentIndex < pageCount - 1) onIndexChange(currentIndex + 1) },
                enabled = currentIndex < pageCount - 1,
                modifier = Modifier.size(height)
            ) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Next")
            }
        }
    }
}

