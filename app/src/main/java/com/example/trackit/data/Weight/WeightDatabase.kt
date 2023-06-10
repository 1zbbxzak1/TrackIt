package com.example.trackit.data.Weight

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WeightEntry::class], version = 2)
@TypeConverters(DateConverter::class, LocalTimeConverter::class)
abstract class WeightDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao

    companion object {
        @Volatile
        private var instance: WeightDatabase? = null

        fun getDatabase(context: Context): WeightDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    WeightDatabase::class.java,
                    "weight_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}