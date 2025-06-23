package com.tek.pagerindicator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class DotAnimation(
    val sizeAnim: AnimationSpec<Float>,
    val offsetAnim: AnimationSpec<Offset>,
    val colorAnim: AnimationSpec<Color>,
    val alphaAnim: AnimationSpec<Float>
) {
    companion object {
        val defaultDotAnimation = DotAnimation(
            sizeAnim = tween<Float>(
                durationMillis = 500,
                easing = LinearEasing
            ),
            offsetAnim = tween<Offset>(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            colorAnim = spring(),
            alphaAnim = tween<Float>(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    }
}
