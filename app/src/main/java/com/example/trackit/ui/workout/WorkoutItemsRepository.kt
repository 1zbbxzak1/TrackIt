package com.example.trackit.ui.workout

import kotlinx.coroutines.flow.Flow

class WorkoutItemsRepository(private val itemDao: WorkoutItemsDao) {
    fun getAllItemsStream(): Flow<List<WorkoutEntity>> = itemDao.getAllItems()

    fun getItemStream(id: Int): Flow<WorkoutEntity?> = itemDao.getItem(id)

    suspend fun insertItem(item: WorkoutEntity) = itemDao.insert(item)

    suspend fun deleteItem(item: WorkoutEntity) = itemDao.delete(item)

    suspend fun updateItem(item: WorkoutEntity) = itemDao.update(item)
}