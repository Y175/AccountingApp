package com.example.accountingapp.ui.theme

import androidx.compose.ui.graphics.Color

// "Accounting Big Cat" Palette (Premium Cute / Pastel)

// Primary Headers
val FreshAirBlue = Color(0xFF8FD3F4)    // Home Header Blue
val ApricotOrange = Color(0xFFFFCC99)   // Statistics Header Orange
val LilacAccent = Color(0xFF9575CD)     // FAB / Accents

// Functional
val SoftGreen = Color(0xFF66BB6A)       // Income
val SoftRed = Color(0xFFEF5350)         // Expense
val SoftBlue = Color(0xFF42A5F5)        // Balance / Info
// 在你的 Color.kt 文件中添加：
val YellowPrimary = Color(0xFFFFD54F)  // 黄色主色
val BlackPrimary = Color(0xFF212121)   // 黑色主色
val GrayText = Color(0xFF757575)       // 灰色文字
// Chart Colors
val ChartBlue = Color(0xFF90CAF9)
val ChartOrange = Color(0xFFFFAB91)
val ChartPurple = Color(0xFFCE93D8)
val ChartGreen = Color(0xFFA5D6A7)

// Backgrounds & Surfaces
val WarmPaper = Color(0xFFFAFAFA)       // Main Background (Off-white)
val PureWhite = Color(0xFFFFFFFF)       // Card Surface
val LightGray = Color(0xFFF5F5F5)       // Secondary Background

// Text
val InkBlack = Color(0xFF455A64)        // Primary Text
val SlateGray = Color(0xFF78909C)       // Secondary Text
val LightText = Color(0xFFB0BEC5)       // Hints

// Dark Mode (Fallback / Muted)
val DarkSlate = Color(0xFF263238)
val OuterSpace = Color(0xFF37474F)

// Legacy Aliases (to prevent build breaks temporarily)
val CatPawPink = LilacAccent // Mapped to new accent
val CreamYellow = ApricotOrange
val MorandiGreen = SoftGreen
val MorandiRed = SoftRed
val MorandiBlue = SoftBlue
