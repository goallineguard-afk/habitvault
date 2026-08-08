package com.habitvault.presentation.common.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressRing(
    percentage: Int,
    modifier: Modifier = Modifier,
    size: Int = 140,
    strokeWidth: Float = 8f,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(percentage) {
        animatedProgress.animateTo(targetValue = percentage / 100f, animationSpec = tween(800))
    }
    Box(modifier = modifier.size(size.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size.dp)) {
            drawArc(
                color = trackColor, startAngle = -90f, sweepAngle = 360f,
                useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = color, startAngle = -90f,
                sweepAngle = 360f * animatedProgress.value,
                useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${percentage}%", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground)
            Text(text = "TODAY", fontSize = 10.sp, fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
