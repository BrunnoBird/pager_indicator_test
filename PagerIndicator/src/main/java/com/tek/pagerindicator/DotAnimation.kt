package com.tek.pagerindicator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
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
            sizeAnim = tween<Float>(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            ),
            offsetAnim = tween<Offset>(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            colorAnim = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            alphaAnim = tween<Float>(
                durationMillis = 500,
                easing = LinearEasing
            )
        )

        val inDotAnimation = DotAnimation(
            sizeAnim = spring<Float>(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            offsetAnim = tween<Offset>(
                durationMillis = 4000,
                easing = FastOutSlowInEasing
            ),
            colorAnim = tween<Color>(
                durationMillis = 4000,
                easing = LinearEasing
            ),
            alphaAnim = tween<Float>(
                durationMillis = 4000,
                easing = LinearEasing
            )
        )

        val outDotAnimation = DotAnimation(
            sizeAnim = tween<Float>(
                durationMillis = 10000,
                easing = FastOutLinearInEasing
            ),
            offsetAnim = tween<Offset>(
                durationMillis = 10000,
                easing = FastOutLinearInEasing
            ),
            colorAnim = tween<Color>(
                durationMillis = 10000,
                easing = FastOutSlowInEasing
            ),
            alphaAnim = tween<Float>(
                durationMillis = 10000,
                easing = LinearEasing
            )
        )

        val deselectingDotAnimation = DotAnimation(
            sizeAnim = tween<Float>(
                durationMillis = 10000,
                easing = FastOutSlowInEasing
            ),
            offsetAnim = tween<Offset>(
                durationMillis = 10000,
                easing = FastOutSlowInEasing
            ),
            colorAnim = tween<Color>(
                durationMillis = 10000,
                easing = LinearEasing
            ),
            alphaAnim = tween<Float>(
                durationMillis = 10000,
                easing = LinearEasing
            )
        )
    }
}

data class DotAnimationSet(
    val default: DotAnimation = DotAnimation.defaultDotAnimation,
    val entering: DotAnimation = DotAnimation.inDotAnimation,
    val leaving: DotAnimation = DotAnimation.outDotAnimation,
    val deselecting: DotAnimation = DotAnimation.deselectingDotAnimation
)
