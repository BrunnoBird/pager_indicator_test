package com.tek.pagerindicator

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

data class DotStyle(
    val currentDotRadius: Dp,
    val notLastDotRadius: Dp,
    val regularDotRadius: Dp,
    val dotMargin: Dp,
    val visibleDotCount: Int,
    val currentDotColor: Color,
    val regularDotColor: Color
) {
    init {
        require(visibleDotCount > 2) { "Visible dot count must be greater than 2" }
        require(currentDotRadius > 0.dp) { "Current dot radius must be greater than 0.dp" }
        require(notLastDotRadius > 0.dp) { "Not last dot radius must be greater than 0.dp" }
        require(regularDotRadius > 0.dp) { "Regular dot radius must be greater than 0.dp" }
        require(dotMargin > 0.dp) { "Dot margin must be greater than 0.dp" }
    }

    companion object {
        private const val defaultVisibleDotCount = 5
        private val defaultRegularRadius = 4.dp
        private val defaultDotNotLastRadius = 2.dp
        private val defaultCurrentDotRadius = 8.dp
        private val defaultDotMargin = defaultRegularRadius * 3f
        private val defaultCurrentDotColor = Color(0xFF0d6efd)
        private val defaultRegularDotColor = Color(0xFF6c757d)
        val defaultDotStyle = DotStyle(
            defaultCurrentDotRadius,
            defaultDotNotLastRadius,
            defaultRegularRadius,
            defaultDotMargin,
            defaultVisibleDotCount,
            defaultCurrentDotColor,
            defaultRegularDotColor
        )
    }
}

internal data class DotStylePx(
    val currentDotRadius: Float,
    val notLastDotRadius: Float,
    val regularDotRadius: Float,
    val dotMargin: Float,
    val visibleDotCount: Int,
    val currentDotColor: Color,
    val regularDotColor: Color
)

internal fun DotStyle.toPx(density: Density): DotStylePx = with(density) {
    DotStylePx(
        currentDotRadius = currentDotRadius.toPx(),
        notLastDotRadius = notLastDotRadius.toPx(),
        regularDotRadius = regularDotRadius.toPx(),
        dotMargin = dotMargin.toPx(),
        visibleDotCount = visibleDotCount,
        currentDotColor = currentDotColor,
        regularDotColor = regularDotColor
    )
}
