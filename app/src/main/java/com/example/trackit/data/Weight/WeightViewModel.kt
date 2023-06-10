package com.example.trackit.data.Weight

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeightViewModel(private val repository: WeightRepository): ViewModel() {

    val selectedDate = mutableStateOf(LocalDate.now())

    private val _weightUiState = MutableStateFlow(WeightUiState())

    val weightUiState: MutableStateFlow<WeightUiState> = _weightUiState

    suspend fun updateWeight(item: WeightEntry){
        repository.updateWeight(item)
        _weightUiState.value = _weightUiState.value.copy()
        viewModelScope.launch {
            _weightUiState.value = repository.getWeights(selectedDate.value).map { WeightUiState(it) }.first().copy()
        }
    }

    suspend fun insertWeight(item: WeightEntry){
        repository.insertWeight(item)
    }

    suspend fun deleteWeight(item: WeightEntry){
        repository.deleteWeight(item)
        viewModelScope.launch {
            _weightUiState.value = repository.getWeights(selectedDate.value).map { WeightUiState(it) }.first()
        }
    }

    suspend fun getLastNineDatesWithWeight(): List<LocalDate> {
        return repository.getLastTenDatesWithWeight().first()
    }

    suspend fun getWeightByDate(date: LocalDate): Double {
        return repository.getWeightByDate(date).first()
    }

    suspend fun getAllWeights(date: LocalDate): List<WeightEntry> {
        return repository.getAllItemsStream()
    }

    suspend fun getLast(): Double {
        return repository.getLastWeight().firstOrNull() ?: 0.0
    }
}

data class WeightUiState(
    val itemList: List<WeightEntry> = listOf()
)