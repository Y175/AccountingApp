package com.example.accountingapp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
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

    fun getColor(iconName: String): Color {
        return when (iconName) {
            // 支出类 - 马卡龙色系
            "Restaurant" -> Color(0xFFFFB3BA) // 马卡龙粉红 - 餐饮
            "ShoppingCart" -> Color(0xFFFFDFBA) // 马卡龙杏色 - 购物
            "Home" -> Color(0xFFD4A5FF) // 马卡龙薰衣草 - 居家
            "DirectionsCar" -> Color(0xFFBAE1FF) // 马卡龙天蓝 - 交通
            "Grass" -> Color(0xFFBFECA8) // 马卡龙青柠 - 水电煤
            "LocalFlorist" -> Color(0xFFF8C8DC) // 马卡龙樱花粉 - 花卉
            "Fastfood" -> Color(0xFFFFE5B4) // 马卡龙奶油黄 - 快餐
            "FitnessCenter" -> Color(0xFFFFB8D1) // 马卡龙玫瑰粉 - 运动
            "Theaters" -> Color(0xFFCDB4FF) // 马卡龙紫罗兰 - 娱乐
            "PhoneAndroid" -> Color(0xFFB4D4FF) // 马卡龙婴儿蓝 - 通讯
            "Checkroom" -> Color(0xFFFFCCD5) // 马卡龙浅粉 - 服饰
            "Face" -> Color(0xFFFFD4E5) // 马卡龙蜜桃粉 - 美容
            "SwapHoriz" -> Color(0xFFD4D4D4) // 马卡龙灰 - 转账
            "Diamond" -> Color(0xFFFFE0F0) // 马卡龙浅紫粉 - 奢侈品
            "School" -> Color(0xFFB8D8FF) // 马卡龙冰蓝 - 教育
            "Flight" -> Color(0xFFB8E0FF) // 马卡龙薄荷蓝 - 旅行
            "LocalCafe" -> Color(0xFFD4BDAC) // 马卡龙焦糖 - 咖啡
            "Smartphone" -> Color(0xFFC4B5FD) // 马卡龙淡紫 - 数码
            "LocalHospital" -> Color(0xFFFFCCCC) // 马卡龙浅红 - 医疗
            "Cloud" -> Color(0xFFCCE7FF) // 马卡龙云蓝 - 保险
            "Work" -> Color(0xFFD1C4E9) // 马卡龙淡紫灰 - 办公
            "ChildCare" -> Color(0xFFFFD6E8) // 马卡龙婴儿粉 - 育儿
            "Elderly" -> Color(0xFFFFDDB8) // 马卡龙暖杏 - 养老
            "Pets" -> Color(0xFFFFE8C5) // 马卡龙奶茶 - 宠物
            "Groups" -> Color(0xFFDAD0FF) // 马卡龙丁香紫 - 社交
            "Star" -> Color(0xFFFFF4C4) // 马卡龙柠檬黄 - 会员
            "People" -> Color(0xFFD5E8FF) // 马卡龙天蓝 - 人情
            "TrendingUp" -> Color(0xFFB8E6D5) // 马卡龙薄荷绿 - 金融
            "Chair" -> Color(0xFFD4C4B0) // 马卡龙米褐 - 家具
            "Build" -> Color(0xFFC4D4D4) // 马卡龙银灰 - 维修
            "LocalBar" -> Color(0xFFE0CFFF) // 马卡龙葡萄紫 - 酒水
            "CardGiftcard" -> Color(0xFFFFD4E5) // 马卡龙礼物粉 - 礼物
            "Security" -> Color(0xFFCCD4DC) // 马卡龙钢蓝灰 - 保障

            // 收入类 - 马卡龙绿色系为主
            "AccountBalance" -> Color(0xFFA8DDB5) // 马卡龙薄荷绿 - 工资
            "AccountBalanceWallet" -> Color(0xFFB8E6C9) // 马卡龙嫩绿 - 奖金
            "WorkOutline" -> Color(0xFFB8D4FF) // 马卡龙蓝 - 兼职
            "EmojiEvents" -> Color(0xFFFFE4B3) // 马卡龙香槟金 - 奖金
            "Sell" -> Color(0xFFB8E0FF) // 马卡龙水蓝 - 销售
            "Redeem" -> Color(0xFFFFD4E5) // 马卡龙粉 - 红包
            "Undo" -> Color(0xFFB3E5FC) // 马卡龙青 - 退款
            "MeetingRoom" -> Color(0xFFD4C4B0) // 马卡龙棕 - 租金
            "TrendingDown" -> Color(0xFFDAC4FF) // 马卡龙淡紫 - 投资
            "ConfirmationNumber" -> Color(0xFFFFDDB8) // 马卡龙杏黄 - 中奖
            "Paid" -> Color(0xFFB8E6C9) // 马卡龙翠绿 - 收款
            "Receipt" -> Color(0xFFC4D4FF) // 马卡龙紫蓝 - 报销
            "MoreHoriz" -> Color(0xFFD4D4D4) // 马卡龙浅灰 - 其他

            else -> Color(0xFFE0E0E0) // 默认马卡龙灰
        }
    }
}