package com.example.trackit.data.Weight

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class WeightRepository(private val itemDao: WeightDao) {
    fun getAllWeight(): Flow<List<WeightEntry>> = itemDao.getAllWeight()

    suspend fun insertWeight(item: WeightEntry) = itemDao.insertWeight(item)

    suspend fun deleteWeight(item: WeightEntry) = itemDao.deleteWeightById(item.id)

    fun getWeights(date: LocalDate) = itemDao.getWeights(date)

    fun getLastTenDates(): Flow<List<LocalDate>> =
        itemDao.getLastTenDates()

    fun getWeightByDate(date: LocalDate): Flow<Double> =
        itemDao.getWeightByDate(date)

    fun getLastWeight(): Flow<Double> = itemDao.getLastWeight()
}
