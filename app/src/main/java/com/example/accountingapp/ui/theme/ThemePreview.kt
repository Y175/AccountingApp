package com.example.accountingapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun ThemePreview() {
    AccountingAppTheme(darkTheme = false) {
        ThemeShowcase()
    }
}

@Preview(name = "Dark Mode", showBackground = true, backgroundColor = 0xFF263238)
@Composable
fun ThemePreviewDark() {
    AccountingAppTheme(darkTheme = true) {
        ThemeShowcase()
    }
}

@Composable
fun ThemeShowcase() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "🎨 Design System Showcase", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Premium Cute / Morandi Style", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Colors ---
            SectionHeader("Colors")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ColorSwatch(name = "Home\n(Blue)", color = MaterialTheme.colorScheme.primary)
                ColorSwatch(name = "Stats\n(Orange)", color = MaterialTheme.colorScheme.secondary)
                ColorSwatch(name = "Accent\n(Lilac)", color = MaterialTheme.colorScheme.primaryContainer) // Lilac mapped here in DarkScheme? In Light it is tertiary
                ColorSwatch(name = "Surface", color = MaterialTheme.colorScheme.surface)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ColorSwatch(name = "Income\n(Green)", color = com.example.accountingapp.ui.theme.SoftGreen) 
                ColorSwatch(name = "Expense\n(Red)", color = com.example.accountingapp.ui.theme.SoftRed)
                ColorSwatch(name = "Info\n(Blue)", color = com.example.accountingapp.ui.theme.SoftBlue)
                ColorSwatch(name = "Text", color = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Typography ---
            SectionHeader("Typography (Nunito)")
            Text(text = "Display Large 57sp", style = MaterialTheme.typography.displayLarge)
            Text(text = "Headline Medium 28sp", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Title Medium 16sp (Bold)", style = MaterialTheme.typography.titleMedium)
            Text(text = "Body Medium 14sp (Normal)", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Label Small 11sp", style = MaterialTheme.typography.labelSmall)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Shapes ---
            SectionHeader("Shapes (Rounded)")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) { Text("Small\n16dp", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelSmall) }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) { Text("Medium\n24dp", color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.labelSmall) }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.large),
                    contentAlignment = Alignment.Center
                ) { Text("Large\n32dp", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelSmall) }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- UI Components Demo ---
            SectionHeader("UI Components")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                // Shape comes from Theme automatically (Medium)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Standard Card (Surface Variant)", style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                // Button simulation
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50)), // Pill shape
                    contentAlignment = Alignment.Center
                ) {
                    Text("Primary Action", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Outlined Button simulation
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f)
                        .background(Color.Transparent, RoundedCornerShape(50))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Secondary", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun ColorSwatch(name: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color, RoundedCornerShape(16.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, style = MaterialTheme.typography.labelSmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center, lineHeight = 12.sp)
    }
}
