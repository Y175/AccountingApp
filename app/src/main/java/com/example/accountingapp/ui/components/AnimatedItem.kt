package com.example.accountingapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

@Composable
fun AnimatedItem(
    modifier: Modifier = Modifier,
    index: Int = 0, // Staggered index
    content: @Composable () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }
    val slideAnim = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        val delay = index * 50 // Stagger delay
        // Delay before starting
        kotlinx.coroutines.delay(delay.toLong())
        
        // Parallel animation
        launch {
            alphaAnim.animateTo(1f, animationSpec = tween(durationMillis = 300))
        }
        launch {
             slideAnim.animateTo(0f, animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessVeryLow
            ))
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = alphaAnim.value
                translationY = slideAnim.value
            }
    ) {
        content()
    }
}
