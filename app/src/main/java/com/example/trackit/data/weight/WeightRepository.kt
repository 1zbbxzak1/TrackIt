package com.example.trackit.data.weight

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime


class WeightRepository(private val weightDao: WeightDao) {
    fun getAllWeight(): Flow<List<WeightEntry>> = weightDao.getAllWeight()

    suspend fun insertWeight(item: WeightEntry) = weightDao.insertWeight(item)

    suspend fun deleteWeight(item: WeightEntry) = weightDao.deleteWeightById(item.id)

    fun getWeights(date: LocalDate) = weightDao.getWeights(date)

    fun getLastTenDates(): Flow<List<DateTimeWrapper>> =
        weightDao.getLastTenDates()

    fun getWeightByDate(date: LocalDate, time: LocalTime): Flow<Double> =
        weightDao.getWeightByDate(date, time)

    fun getLastWeight(): Flow<String> = weightDao.getLastWeight()
}
