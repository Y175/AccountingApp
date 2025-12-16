package com.example.accountingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accountingapp.ui.theme.YellowPrimary

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
            .height(240.dp)
            .background(Color.White)
    ) {
        val keys = listOf(
            listOf("1", "2", "3", "date"),
            listOf("4", "5", "6", "+"), // + is placeholder for maybe math or just nothing
            listOf("7", "8", "9", "-"),
            listOf(".", "0", "del", "ok")
        )

        Row(modifier = Modifier.fillMaxSize()) {
            // Numbers area (3 columns)
            Column(modifier = Modifier.weight(3f)) {
                Row(modifier = Modifier.weight(1f)) {
                    KeypadButton("1", modifier = Modifier.weight(1f)) { onNumberClick("1") }
                    KeypadButton("2", modifier = Modifier.weight(1f)) { onNumberClick("2") }
                    KeypadButton("3", modifier = Modifier.weight(1f)) { onNumberClick("3") }
                }
                Row(modifier = Modifier.weight(1f)) {
                    KeypadButton("4", modifier = Modifier.weight(1f)) { onNumberClick("4") }
                    KeypadButton("5", modifier = Modifier.weight(1f)) { onNumberClick("5") }
                    KeypadButton("6", modifier = Modifier.weight(1f)) { onNumberClick("6") }
                }
                Row(modifier = Modifier.weight(1f)) {
                    KeypadButton("7", modifier = Modifier.weight(1f)) { onNumberClick("7") }
                    KeypadButton("8", modifier = Modifier.weight(1f)) { onNumberClick("8") }
                    KeypadButton("9", modifier = Modifier.weight(1f)) { onNumberClick("9") }
                }
                Row(modifier = Modifier.weight(1f)) {
                    KeypadButton(".", modifier = Modifier.weight(1f)) { onNumberClick(".") }
                    KeypadButton("0", modifier = Modifier.weight(1f)) { onNumberClick("0") }
                    KeypadButton("DEL", modifier = Modifier.weight(1f)) { onDeleteClick() }
                }
            }

            // Right column (Date, OK)
            Column(modifier = Modifier.weight(1f)) {
                KeypadButton("日期", modifier = Modifier.weight(1f)) { onDateClick() }
                // OK Button spans 3 rows height effectively if we structure it differently, 
                // but for simple grid let's just make it big or standard.
                // The requirements say "Date Time Selector", "Amount Input".
                // Let's stick to a simple layout: 4 rows.
                // Row 1: Date (or +)
                // Row 2: -
                // Row 3: OK (Height 2x)
                
                // Actually, let's just make a big OK button at the bottom right.
                KeypadButton("OK", modifier = Modifier.weight(3f), backgroundColor = YellowPrimary) { onOkClick() }
            }
        }
    }
}

@Composable
fun KeypadButton(
    text: String, 
    modifier: Modifier = Modifier, 
    backgroundColor: Color = Color.White,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(1.dp), // Grid line effect
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
