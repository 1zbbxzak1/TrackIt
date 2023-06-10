package com.example.trackit.data.Weight

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class WeightRepository(private val itemDao: WeightDao) {
    suspend fun getAllItemsStream(): List<WeightEntry> = itemDao.getAllWeightEntries()

    suspend fun insertWeight(item: WeightEntry) = itemDao.insertWeightEntry(item)

    suspend fun deleteWeight(item: WeightEntry) = itemDao.deleteWeightEntryById(item.id)

    suspend fun updateWeight(item: WeightEntry) = itemDao.updateWeightEntry(item)

    fun getWeights(date: LocalDate) = itemDao.getWeights(date)

    fun getLastTenDatesWithWeight(): Flow<List<LocalDate>> =
        itemDao.getLastNineDatesWithWeight()

    fun getWeightByDate(date: LocalDate): Flow<Double> =
        itemDao.getWeightByDate(date)

    fun getLastWeight(): Flow<Double> = itemDao.getLastWeight()
}
