package com.example.accountingapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asAndroidPath
import com.example.accountingapp.ui.theme.ApricotOrange

@Composable
fun LineChart(
    dataPoints: List<Double>,
    modifier: Modifier = Modifier
) {
    if (dataPoints.isEmpty()) return

    val transitionProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    
    LaunchedEffect(dataPoints) {
        transitionProgress.snapTo(0f)
        transitionProgress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val width = size.width
        val height = size.height
        val maxVal = dataPoints.maxOrNull() ?: 1.0
        val minVal = 0.0 

        val points = dataPoints.mapIndexed { index, value ->
            val x = (width / (dataPoints.size - 1)) * index
            val y = height - ((value - minVal) / (maxVal - minVal) * height).toFloat()
            Offset(x, y)
        }

        val path = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach {
                    lineTo(it.x, it.y)
                }
            }
        }
        
        // Measure path length
        val pathMeasure = android.graphics.PathMeasure(path.asAndroidPath(), false)
        val pathLength = pathMeasure.length
        
        // Create animated path
        val animatedPath = Path()
        val androidAnimatedPath = android.graphics.Path()
        pathMeasure.getSegment(0f, pathLength * transitionProgress.value, androidAnimatedPath, true)
        animatedPath.asAndroidPath().set(androidAnimatedPath)

        drawPath(
            path = animatedPath,
            color = ApricotOrange,
            style = Stroke(width = 4.dp.toPx())
        )

        // Draw circles only when animation is done or based on progress? 
        // Let's draw them with alpha based on progress
        if (transitionProgress.value > 0.8f) {
            val alpha = (transitionProgress.value - 0.8f) * 5 // 0 to 1
            points.forEach { point ->
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = point,
                    alpha = alpha
                )
                drawCircle(
                    color = ApricotOrange,
                    radius = 4.dp.toPx(),
                    center = point,
                    alpha = alpha
                )
            }
        }
    }
}
