package com.example.trackit.data.Weight

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class WeightRepository(private val itemDao: WeightDao) {
    fun getAllWeightStream(): Flow<List<WeightEntry>> = itemDao.getAllWeightEntries()

    suspend fun insertWeight(item: WeightEntry) = itemDao.insertWeightEntry(item)

    suspend fun deleteWeight(item: WeightEntry) = itemDao.deleteWeightEntryById(item.id)

    fun getWeights(date: LocalDate) = itemDao.getWeights(date)

    fun getLastTenDatesWithWeight(): Flow<List<LocalDate>> =
        itemDao.getLastNineDatesWithWeight()

    fun getWeightByDate(date: LocalDate): Flow<Double> =
        itemDao.getWeightByDate(date)

    fun getLastWeight(): Flow<Double> = itemDao.getLastWeight()
}
