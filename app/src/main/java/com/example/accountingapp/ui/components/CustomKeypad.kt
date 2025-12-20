package com.example.accountingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape // Or RoundedCornerShape(50) for squircle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.ui.theme.*

@Composable
fun CustomKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onDateClick: () -> Unit,
    onOkClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9")
        )

        val colors = listOf(PastelPink, PastelBlue, PastelOrange, PastelYellow, PastelGreen, PastelPurple)

        var colorIndex = 0

        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { num ->
                    KeypadButton(
                        text = num,
                        backgroundColor = colors[colorIndex % colors.size],
                        modifier = Modifier.weight(1f)
                    ) { onNumberClick(num) }
                    colorIndex++
                }
            }
        }

        // Bottom Row: Backspace, Calendar, Done
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Delete
            KeypadButton(
                icon = Icons.Default.Backspace,
                backgroundColor = PastelPurple.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f),
                onClick = onDeleteClick
            )
            
            // Calendar / Date
            KeypadButton(
                icon = Icons.Default.CalendarToday,
                backgroundColor = PastelYellow,
                modifier = Modifier.weight(1f),
                onClick = onDateClick
            )
            
            // Done
            KeypadButton(
                text = "确认",
                backgroundColor = LilacAccent, // Deep Purple
                textColor = Color.White,
                modifier = Modifier.weight(1f),
                onClick = onOkClick
            )
        }
    }
}

@Composable
fun KeypadButton(
    text: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(64.dp) // Taller buttons
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = 24.sp
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}
