package com.habitvault.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitvault.core.domain.model.DisciplineScore

@Composable
fun DisciplineScoreBar(score: DisciplineScore) {
    val percentage = score.overall.coerceIn(0, 100)
    val barColor = when {
        percentage >= 90 -> Color(0xFF34D399)
        percentage >= 70 -> Color(0xFFF59E0B)
        else -> Color(0xFFEF4444)
    }

    CardContainer {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${score.overall}",
                        fontSize = 48.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Discipline Score",
                        fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                ScoreGradeBadge(grade = score.grade.name, isFailing = score.isFailing)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage / 100f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(barColor)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (score.isFailing) "Below 70 = Failing. No participation trophies." else "Keep pushing. Consistency compounds.",
                fontSize = 12.sp,
                color = if (score.isFailing) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ScoreGradeBadge(grade: String, isFailing: Boolean) {
    val bgColor = if (isFailing) Color(0xFFFEE2E2) else Color(0xFFD1FAE5)
    val textColor = if (isFailing) Color(0xFFDC2626) else Color(0xFF059669)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = grade, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
    }
}

@Composable
fun CardContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}
