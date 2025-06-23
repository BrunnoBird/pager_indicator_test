package com.tek.pagerindicator

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

data class DotStyle(
    val currentDotRadius: Dp,
    val regularDotRadius: Dp,
    val dotMargin: Dp,
    val visibleDotCount: Int,
    val currentDotColor: Color,
    val regularDotColor: Color
) {
    init {
        require(visibleDotCount > 2) { "Visible dot count must be greater than 2" }
        require(currentDotRadius > 0.dp) { "Current dot radius must be greater than 0.dp" }
        require(regularDotRadius > 0.dp) { "Regular dot radius must be greater than 0.dp" }
        require(dotMargin > 0.dp) { "Dot margin must be greater than 0.dp" }
    }

    companion object {
        private const val defaultVisibleDotCount = 5
        private val defaultRegularRadius = 4.dp
        private val defaultCurrentDotRadius = 8.dp
        private val defaultDotMargin = 4.dp
        private val defaultCurrentDotColor = Color(0xFF0d6efd)
        private val defaultRegularDotColor = Color(0xFF6c757d)
        val defaultDotStyle = DotStyle(
            defaultCurrentDotRadius,
            defaultRegularRadius,
            defaultDotMargin,
            defaultVisibleDotCount,
            defaultCurrentDotColor,
            defaultRegularDotColor
        )
    }
}

internal fun DotStyle.contentWidth(): Dp {
    val dotCount = visibleDotCount
    if (dotCount <= 0) return 0.dp
    val regularDiameter = regularDotRadius * 2
    val currentDiameter = currentDotRadius * 2
    val dotsWidth = currentDiameter + regularDiameter * (dotCount - 1)
    val spacing = dotMargin * (dotCount - 1)
    return dotsWidth + spacing
}


internal data class DotStylePx(
    val currentDotRadius: Float,
    val regularDotRadius: Float,
    val dotMargin: Float,
    val visibleDotCount: Int,
    val currentDotColor: Color,
    val regularDotColor: Color
)

internal fun DotStyle.toPx(density: Density): DotStylePx = with(density) {
        DotStylePx(
            currentDotRadius = currentDotRadius.toPx(),
            regularDotRadius = regularDotRadius.toPx(),
            dotMargin = dotMargin.toPx(),
            visibleDotCount = visibleDotCount,
            currentDotColor = currentDotColor,
            regularDotColor = regularDotColor
    )
}

internal fun DotStylePx.contentWidth(): Float {
    val dotCount = visibleDotCount
    if (dotCount <= 0) return 0f
    val regularDiameter = regularDotRadius * 2
    val currentDiameter = currentDotRadius * 2
    val dotsWidth = currentDiameter + regularDiameter * (dotCount - 1)
    val spacing = dotMargin * (dotCount - 1)
    return dotsWidth + spacing
}

