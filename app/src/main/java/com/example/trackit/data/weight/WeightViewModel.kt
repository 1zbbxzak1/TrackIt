package com.example.trackit.data.weight

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class WeightViewModel(private val repository: WeightRepository): ViewModel() {

    private val selectedDate = mutableStateOf(LocalDate.now())

    private val _weightUiState = MutableStateFlow(WeightUiState())

    val weightUiState =
        repository.getAllWeight().map { WeightUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = WeightUiState()
            )

    suspend fun insertWeight(item: WeightEntry){
        repository.insertWeight(item)
    }

    suspend fun deleteWeight(item: WeightEntry){
        repository.deleteWeight(item)
        viewModelScope.launch {
            _weightUiState.value = repository.getWeights(selectedDate.value).map { WeightUiState(it) }.first()
        }
    }

    suspend fun getLastTenDates(): List<DateTimeWrapper> {
        return repository.getLastTenDates().first()
    }

    suspend fun getWeightByDate(date: LocalDate, time: LocalTime): Double {
        return repository.getWeightByDate(date, time).first()
    }

    suspend fun getLast(): String {
        return repository.getLastWeight().firstOrNull() ?: ""
    }
}

data class WeightUiState(val weightList: List<WeightEntry> = listOf())