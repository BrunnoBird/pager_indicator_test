package com.tek.pagerindicator

import androidx.compose.ui.graphics.Color

data class DotStyle(
    val currentDotRadius: Float,
    val notLastDotRadius: Float,
    val regularDotRadius: Float,
    val dotMargin: Float,
    val visibleDotCount: Int,
    val currentDotColor: Color,
    val regularDotColor: Color,
    /**
     * Number of dots to hide while a page transition is running.
     */
    val hideDuringScrollCount: Int = 0
) {
    init {
        require(visibleDotCount > 2) { "Visible dot count must be greater than 2" }
        require(currentDotRadius > 0f) { "Current dot radius must be greater than 0F" }
        require(notLastDotRadius > 0f) { "Not last dot radius must be greater than 0F" }
        require(regularDotRadius > 0f) { "Regular dot radius must be greater than 0F" }
        require(dotMargin > 0f) { "Dot margin must be greater than 0F" }
        require(hideDuringScrollCount >= 0) { "Hide dot count must be >= 0" }
        require(hideDuringScrollCount <= visibleDotCount) {
            "Hide dot count must be <= visible dot count"
        }
    }

    companion object {
        private const val defaultVisibleDotCount = 5
        private const val defaultRegularRadius = 4f
        private const val defaultDotNotLastRadius = 2f
        private const val defaultCurrentDotRadius = 8f
        private const val defaultDotMargin = defaultRegularRadius.times(3f)
        private val defaultCurrentDotColor = Color(0xFF0d6efd)
        private val defaultRegularDotColor = Color(0xFF6c757d)
        val defaultDotStyle = DotStyle(
            defaultCurrentDotRadius,
            defaultDotNotLastRadius,
            defaultRegularRadius,
            defaultDotMargin,
            defaultVisibleDotCount,
            defaultCurrentDotColor,
            defaultRegularDotColor,
            hideDuringScrollCount = 0
        )
    }
}