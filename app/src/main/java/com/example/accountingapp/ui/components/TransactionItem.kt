package com.example.accountingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.data.Transaction
import com.example.accountingapp.data.TransactionType
import com.example.accountingapp.util.CategoryIcons
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.scale
import com.example.accountingapp.ui.theme.InkBlack

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "scale"
    )

    // 获取类目颜色
    val categoryColor = CategoryIcons.getColor(transaction.categoryIcon)

    // Floating Pill Card
    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp) // 添加水平和垂直间距
            .scale(scale)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                indication = rememberRipple(color = categoryColor.copy(alpha = 0.1f)),
                interactionSource = interactionSource
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        color = com.example.accountingapp.ui.theme.PureWhite,
        shadowElevation = 1.dp, // 减小阴影
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp), // 调整内边距
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(44.dp) // 稍微减小图标容器
                    .clip(CircleShape)
                    .background(categoryColor.copy(alpha = 0.12f)), // 使用类目颜色的浅色背景
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = CategoryIcons.getIcon(transaction.categoryIcon),
                    contentDescription = transaction.categoryName,
                    tint = categoryColor, // 使用类目颜色
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Category and Note
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.categoryName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold, // 从 Bold 改为 SemiBold
                    color = com.example.accountingapp.ui.theme.InkBlack,
                    fontSize = 15.sp
                )
                if (transaction.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.labelSmall,
                        color = com.example.accountingapp.ui.theme.SlateGray.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Amount
            val amountColor = if (transaction.type == TransactionType.INCOME)
                com.example.accountingapp.ui.theme.SoftGreen
            else
                com.example.accountingapp.ui.theme.SoftRed

            val amountText = if (transaction.type == TransactionType.EXPENSE)
                "-¥${String.format("%.0f", transaction.amount)}"
            else
                "+¥${String.format("%.0f", transaction.amount)}"

            Text(
                text = amountText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor,
                fontSize = 16.sp
            )
        }
    }
}