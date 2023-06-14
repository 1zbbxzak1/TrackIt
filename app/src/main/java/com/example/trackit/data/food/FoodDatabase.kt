package com.example.trackit.data.food

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackit.data.DateConverter
import com.example.trackit.data.FoodTypeConverters
import com.example.trackit.ui.Nutrition.*


@Database(
    entities = [Breakfast::class, Lunch::class, Dinner::class, Snack::class, Total::class],
    version = 2
)
@TypeConverters(DateConverter::class, FoodTypeConverters::class)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun breakfastDao(): BreakfastDao
    abstract fun lunchDao(): LunchDao
    abstract fun dinnerDao(): DinnerDao
    abstract fun snackDao(): SnackDao
    abstract fun totalDao(): TotalDao

    companion object {
        @Volatile
        private var instance: FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "food_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}