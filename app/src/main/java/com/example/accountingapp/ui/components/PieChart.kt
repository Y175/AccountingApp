package com.example.accountingapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.PieChartData
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 50f,
    animationDuration: Int = 1000,
    showLabels: Boolean = true,
    showPercentage: Boolean = true
) {
    val total = data.sumOf { it.value }

    // 动画进度
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animationDuration)
        )
    }

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val textMeasurer = rememberTextMeasurer()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2 - strokeWidth
            val center = Offset(size.width / 2, size.height / 2)
            var startAngle = -90f

            data.forEach { item ->
                val sweepAngle = (item.value / total * 360).toFloat() * animatedProgress.value

                // 绘制圆弧
                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth)
                )

                // 绘制标签
                if (showLabels && animatedProgress.value > 0.5f) {
                    val angle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                    val labelRadius = radius + strokeWidth / 2

                    // 标签位置
                    val labelX = center.x + (labelRadius * cos(angle)).toFloat()
                    val labelY = center.y + (labelRadius * sin(angle)).toFloat()

                    // 外部标签位置（延伸）
                    val extendedRadius = labelRadius + 40.dp.toPx()
                    val extendedX = center.x + (extendedRadius * cos(angle)).toFloat()
                    val extendedY = center.y + (extendedRadius * sin(angle)).toFloat()

                    // 绘制指示线
                    drawLine(
                        color = item.color,
                        start = Offset(labelX, labelY),
                        end = Offset(extendedX, extendedY),
                        strokeWidth = 2.dp.toPx()
                    )

                    // 绘制文本
                    val percentage = if (showPercentage) {
                        " (${(item.value / total * 100).toInt()}%)"
                    } else {
                        ""
                    }

                    val text = "${item.name}$percentage"
                    val textLayoutResult = textMeasurer.measure(
                        text = text,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    )

                    // 根据角度调整文本位置
                    val textOffset = if (extendedX > center.x) {
                        Offset(extendedX + 5.dp.toPx(), extendedY - textLayoutResult.size.height / 2)
                    } else {
                        Offset(extendedX - textLayoutResult.size.width - 5.dp.toPx(), extendedY - textLayoutResult.size.height / 2)
                    }

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = textOffset
                    )
                }

                startAngle += sweepAngle
            }
        }
    }
}