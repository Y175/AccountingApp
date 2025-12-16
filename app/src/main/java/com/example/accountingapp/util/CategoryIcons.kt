package com.example.accountingapp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryIcons {
    fun getIcon(iconName: String): ImageVector {
        return when (iconName) {
            // 支出类
            "Restaurant" -> Icons.Default.Restaurant
            "ShoppingCart" -> Icons.Default.ShoppingCart
            "Home" -> Icons.Default.Home
            "DirectionsCar" -> Icons.Default.DirectionsCar
            "Grass" -> Icons.Default.Grass
            "LocalFlorist" -> Icons.Default.LocalFlorist
            "Fastfood" -> Icons.Default.Fastfood
            "FitnessCenter" -> Icons.Default.FitnessCenter
            "Theaters" -> Icons.Default.Theaters
            "PhoneAndroid" -> Icons.Default.PhoneAndroid
            "Checkroom" -> Icons.Default.Checkroom
            "Face" -> Icons.Default.Face
            "SwapHoriz" -> Icons.Default.SwapHoriz
            "Diamond" -> Icons.Default.Diamond
            "School" -> Icons.Default.School
            "Flight" -> Icons.Default.Flight
            "LocalCafe" -> Icons.Default.LocalCafe
            "Smartphone" -> Icons.Default.Smartphone
            "LocalHospital" -> Icons.Default.LocalHospital
            "Cloud" -> Icons.Default.Cloud
            "Work" -> Icons.Default.Work
            "ChildCare" -> Icons.Default.ChildCare
            "Elderly" -> Icons.Default.Elderly
            "Pets" -> Icons.Default.Pets
            "Groups" -> Icons.Default.Groups
            "Star" -> Icons.Default.Star
            "People" -> Icons.Default.People
            "TrendingUp" -> Icons.Default.TrendingUp
            "Chair" -> Icons.Default.Chair
            "Build" -> Icons.Default.Build
            "LocalBar" -> Icons.Default.LocalBar
            "CardGiftcard" -> Icons.Default.CardGiftcard
            "Security" -> Icons.Default.Security

            // 收入类
            "AccountBalance" -> Icons.Default.AccountBalance
            "AccountBalanceWallet" -> Icons.Default.AccountBalanceWallet
            "WorkOutline" -> Icons.Default.WorkOutline
            "EmojiEvents" -> Icons.Default.EmojiEvents
            "Sell" -> Icons.Default.Sell
            "Redeem" -> Icons.Default.Redeem
            "Undo" -> Icons.Default.Undo
            "MeetingRoom" -> Icons.Default.MeetingRoom
            "TrendingDown" -> Icons.Default.TrendingDown
            "ConfirmationNumber" -> Icons.Default.ConfirmationNumber
            "Paid" -> Icons.Default.Paid
            "Receipt" -> Icons.Default.Receipt
            "MoreHoriz" -> Icons.Default.MoreHoriz

            else -> Icons.Default.Category // 默认图标
        }
    }
}