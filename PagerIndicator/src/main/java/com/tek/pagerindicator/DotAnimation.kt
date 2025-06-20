package com.tek.pagerindicator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DotAnimation(
    val sizeAnim: AnimationSpec<Float>,
    val offsetAnim: AnimationSpec<Offset>,
    val colorAnim: AnimationSpec<Color>,
    val alphaAnim: AnimationSpec<Float>
) {
    companion object {
        val defaultDotAnimation = DotAnimation(
            tween<Float>(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            tween<Offset>(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            spring(),
            tween<Float>(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }
}