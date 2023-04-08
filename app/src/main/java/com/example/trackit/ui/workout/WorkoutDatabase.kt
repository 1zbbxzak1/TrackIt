package com.example.trackit.ui.workout

import android.content.Context
import androidx.room.*

@Database(entities = [WorkoutEntity::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class, ExerciseConverter::class)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun itemDao(): WorkoutItemsDao

    companion object {
        @Volatile
        private var Instance: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WorkoutDatabase::class.java, "workout_items_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}