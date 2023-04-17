package com.example.trackit.ui.workout

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import values.preCreatedCategoryList

@Database(entities = [WorkoutEntity::class], version = 2, exportSchema = false)
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

@Database(entities = [WorkoutCategory::class], version = 1, exportSchema = false)
@TypeConverters(ExerciseConverter::class)
abstract class WorkoutCategoryDatabase : RoomDatabase(){
    abstract fun itemDao(): WorkoutCategoryDao

    companion object {
        @Volatile
        private var Instance: WorkoutCategoryDatabase? = null

        fun getDatabase(context: Context): WorkoutCategoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WorkoutCategoryDatabase::class.java, "workout_categories_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insert pre-created objects into the database
                            GlobalScope.launch {
                                preCreatedCategoryList.forEach{category ->
                                    getDatabase(context).itemDao().insert(category)
                                }
                            }
                        }
                    })
                    .build()
                    .also { Instance = it }
            }
        }
    }
}