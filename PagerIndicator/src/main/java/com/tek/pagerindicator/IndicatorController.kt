package com.tek.pagerindicator

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import com.tek.pagerindicator.DotStylePx

// internal representation of DotStyle in pixels

internal class IndicatorController(
    private val count: Int,
    private val size: IntSize,
    private val dotStyle: DotStylePx,
    private val startIndex: Int = 0,
    startRange: IntRange = startIndex..dotStyle.visibleDotCount.minus(1)

) : IndicatorRangeProcessor, IndicatorMovementProcessor {
    private var selectedIndex = mutableStateOf(startIndex)

    internal val colorTargets = SnapshotStateList<Color>()
    internal val colors = mutableListOf<State<Color>>()

    internal val sizeTargets = SnapshotStateList<Float>()
    internal val sizes = mutableListOf<State<Float>>()

    internal val offsetTargets = SnapshotStateList<Offset>()
    internal val offSets = mutableListOf<State<Offset>>()

    private var visibleRange = startRange

    init {
        Log.e("indicatorController", "init")
        for (i in 0 until count) {
            colorTargets.add(colorFinder(i))
            sizeTargets.add(sizeFinder(i))
            offsetTargets.add(Offset.Zero)
        }
        computeOffsets()
    }

    fun clearAll() {
        sizes.clear()
        offSets.clear()
        colors.clear()
    }

    fun pageChanged(index: Int) {
        Log.e("indicatorController", "pageChanged")

        if (index == selectedIndex.value)
            return
        if (selectedIndex.value > index)
            prev()
        else
            next()
    }

    private fun next() {
        if (selectedIndex.value + 1 == visibleRange.last && selectedIndex.value + 1 != count - 1) {
            processRangeNext()
        }
        selectedIndex.value++
        for (i in 0 until count) {
            sizeTargets[i] = sizeFinder(i)
            colorTargets[i] = colorFinder(i)
        }
        computeOffsets()
    }

    private fun prev() {
        if (selectedIndex.value - 1 == visibleRange.first && selectedIndex.value - 1 != 0) {
            processRangePrev()
        }
        selectedIndex.value--
        for (i in 0 until count) {
            sizeTargets[i] = sizeFinder(i)
            colorTargets[i] = colorFinder(i)
        }
        computeOffsets()

    }


    private fun colorFinder(index: Int): Color {
        return when (index) {
            selectedIndex.value -> dotStyle.currentDotColor
            else -> dotStyle.regularDotColor

        }
    }

    private fun sizeFinder(index: Int): Float {
        return when (index) {
            selectedIndex.value -> dotStyle.currentDotRadius
            in visibleRange -> dotStyle.regularDotRadius
            else -> 0f
        }
    }

    private fun widthForRange(radii: FloatArray, range: IntRange): Float {
        var total = radii[range.first] * 2f
        for (i in range.first + 1..range.last) {
            total += radii[i] * 2f + dotStyle.dotMargin
        }
        return total
    }

    private fun computeOffsets() {
        val radii = FloatArray(count) { sizeFinder(it) }
        val centers = FloatArray(count)
        val first = visibleRange.first
        val last = visibleRange.last

        val total = widthForRange(radii, first..last)
        val centerCoord = size.width / 2f
        centers[first] = centerCoord - total / 2f + radii[first]

        for (i in first + 1 until count) {
            centers[i] = centers[i - 1] + radii[i - 1] + radii[i] + dotStyle.dotMargin
        }
        for (i in first - 1 downTo 0) {
            centers[i] = centers[i + 1] - (radii[i + 1] + radii[i] + dotStyle.dotMargin)
        }

        for (i in 0 until count) {
            val off = Offset(centers[i], size.center.y.toFloat())
            if (offsetTargets.size > i) offsetTargets[i] = off else offsetTargets.add(off)
        }
    }

    override fun processRangeNext() {
        visibleRange = visibleRange.first.plus(1)..visibleRange.last.plus(1)
    }

    override fun processRangePrev() {
        visibleRange = visibleRange.first.minus(1)..visibleRange.last.minus(1)

    }

    override fun processMovementForward() {
        sizeTargets[selectedIndex.value] = dotStyle.regularDotRadius
        colorTargets[selectedIndex.value] = dotStyle.regularDotColor
        selectedIndex.value++
        sizeTargets[selectedIndex.value] = dotStyle.currentDotRadius
        colorTargets[selectedIndex.value] = dotStyle.currentDotColor
        computeOffsets()
    }

    override fun processMovementBackward() {
        sizeTargets[selectedIndex.value] = dotStyle.regularDotRadius
        colorTargets[selectedIndex.value] = dotStyle.regularDotColor
        selectedIndex.value--
        sizeTargets[selectedIndex.value] = dotStyle.currentDotRadius
        colorTargets[selectedIndex.value] = dotStyle.currentDotColor
        computeOffsets()

    }

}

@Composable
internal fun rememberIndicatorController(
    count: Int,
    size: IntSize,
    dotStyle: DotStylePx,
    startIndex: Int,
    startRange: IntRange
): IndicatorController {
    return remember {
        IndicatorController(count, size, dotStyle, startIndex, startRange)
    }
}