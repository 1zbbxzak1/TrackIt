package com.example.trackit.ui.workout

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class WorkoutItemsRepository(private val itemDao: WorkoutItemsDao) {
    fun getAllItemsStream(): Flow<List<WorkoutEntity>> = itemDao.getAllItems()

    fun getItemStream(id: Int): Flow<WorkoutEntity?> = itemDao.getItem(id)

    suspend fun insertItem(item: WorkoutEntity) = itemDao.insert(item)

    suspend fun deleteItem(item: WorkoutEntity) = itemDao.delete(item.id)

    suspend fun updateItem(item: WorkoutEntity) = itemDao.update(item)

    fun getItemsOnDate(date: LocalDate) = itemDao.getItemsOnDate(date)
}

class WorkoutCategoryRepository(private val itemDao: WorkoutCategoryDao){
    fun getAllItemsStream(): Flow<List<WorkoutCategory>> = itemDao.getAllItems()

    fun getItemStream(id: Int): Flow<WorkoutCategory?> = itemDao.getItem(id)

    suspend fun insertItem(item: WorkoutCategory) = itemDao.insert(item)

    suspend fun deleteItem(item: WorkoutCategory) = itemDao.delete(item)

    suspend fun updateItem(item: WorkoutCategory) = itemDao.update(item)

    suspend fun insertPreCreatedCategories(categories: List<WorkoutCategory>){
        categories.forEach{ category -> itemDao.insert(category) }
    }
}