package com.example.accountingapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Transaction::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "accounting_database"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.transactionDao())
                }
            }
        }

        suspend fun populateDatabase(dao: TransactionDao) {
            if (dao.getCategoryCount() == 0) {
                // 支出分类及其图标
                val expenseCategories = mapOf(
                    "餐饮" to "Restaurant",
                    "购物" to "ShoppingCart",
                    "日用" to "Home",
                    "交通" to "DirectionsCar",
                    "蔬菜" to "Grass",
                    "水果" to "LocalFlorist",
                    "零食" to "Fastfood",
                    "运动" to "FitnessCenter",
                    "休闲娱乐" to "Theaters",
                    "通讯" to "PhoneAndroid",
                    "服装" to "Checkroom",
                    "美容" to "Face",
                    "转账" to "SwapHoriz",
                    "住房" to "Home",
                    "首饰" to "Diamond",
                    "学习" to "School",
                    "旅行" to "Flight",
                    "饮品" to "LocalCafe",
                    "数码" to "Smartphone",
                    "医疗" to "LocalHospital",
                    "网络虚拟" to "Cloud",
                    "办公" to "Work",
                    "孩子" to "ChildCare",
                    "长辈" to "Elderly",
                    "宠物" to "Pets",
                    "社交" to "Groups",
                    "追星" to "Star",
                    "亲友" to "People",
                    "借出" to "TrendingUp",
                    "家具家电" to "Chair",
                    "汽车" to "DirectionsCar",
                    "维修" to "Build",
                    "烟酒" to "LocalBar",
                    "礼金" to "CardGiftcard",
                    "保险" to "Security",
                    "其他" to "MoreHoriz"
                )

                expenseCategories.forEach { (name, iconName) ->
                    dao.insertCategory(
                        Category(
                            name = name,
                            iconName = iconName,
                            type = TransactionType.EXPENSE
                        )
                    )
                }

                // 收入分类及其图标
                val incomeCategories = mapOf(
                    "工资" to "AccountBalance",
                    "红包" to "CardGiftcard",
                    "收款" to "AccountBalanceWallet",
                    "兼职" to "WorkOutline",
                    "奖金" to "EmojiEvents",
                    "理财" to "TrendingUp",
                    "卖二手" to "Sell",
                    "收礼金" to "Redeem",
                    "退款" to "Undo",
                    "收租" to "MeetingRoom",
                    "借入" to "TrendingDown",
                    "彩票" to "ConfirmationNumber",
                    "保险理赔" to "Security",
                    "分红" to "Paid",
                    "报销" to "Receipt",
                    "其他" to "MoreHoriz"
                )

                incomeCategories.forEach { (name, iconName) ->
                    dao.insertCategory(
                        Category(
                            name = name,
                            iconName = iconName,
                            type = TransactionType.INCOME
                        )
                    )
                }
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun transactionTypeToString(type: TransactionType): String {
        return type.name
    }
}
