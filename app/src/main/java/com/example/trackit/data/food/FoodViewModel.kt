package com.example.trackit.data.food

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackit.ui.Nutrition.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

interface MealViewModel<T> {
    val selectedDate: MutableState<LocalDate>
    val mealUiState: MutableStateFlow<T>

    fun updateSelectedDate(date: LocalDate)
}

class BreakfastViewModel(private val repository: FoodRepository) : ViewModel(),
    MealViewModel<BreakfastUiState> {

    override val selectedDate = mutableStateOf(LocalDate.now())
    private val _breakfastUiState = MutableStateFlow(BreakfastUiState())
    override val mealUiState: MutableStateFlow<BreakfastUiState> = _breakfastUiState

    suspend fun insertFood(item: Breakfast) {
        repository.insertBreakfast(item)
    }

    suspend fun deleteFood(item: Breakfast) {
        repository.deleteBreakfast(item)
        viewModelScope.launch {
            _breakfastUiState.value = repository.getBreakfast(selectedDate.value).map { BreakfastUiState(it) }.first()
        }
    }

    override fun updateSelectedDate(date: LocalDate) {
        selectedDate.value = date
        viewModelScope.launch {
            _breakfastUiState.value = repository.getBreakfast(date).map { BreakfastUiState(it) }.first()
        }
    }
}

class LunchViewModel(private val repository: FoodRepository) : ViewModel(),
    MealViewModel<LunchUiState> {

    override val selectedDate = mutableStateOf(LocalDate.now())
    private val _lunchUiState = MutableStateFlow(LunchUiState())
    override val mealUiState: MutableStateFlow<LunchUiState> = _lunchUiState

    suspend fun insertFood(item: Lunch) {
        repository.insertLunch(item)
    }

    suspend fun deleteFood(item: Lunch) {
        repository.deleteLunch(item)
        viewModelScope.launch {
            _lunchUiState.value = repository.getLunch(selectedDate.value).map { LunchUiState(it) }.first()
        }
    }

    override fun updateSelectedDate(date: LocalDate) {
        selectedDate.value = date
        viewModelScope.launch {
            _lunchUiState.value = repository.getLunch(date).map { LunchUiState(it) }.first()
        }
    }
}

class DinnerViewModel(private val repository: FoodRepository) : ViewModel(),
    MealViewModel<DinnerUiState> {

    override val selectedDate = mutableStateOf(LocalDate.now())
    private val _dinnerUiState = MutableStateFlow(DinnerUiState())
    override val mealUiState: MutableStateFlow<DinnerUiState> = _dinnerUiState

    suspend fun insertFood(item: Dinner){
        repository.insertDinner(item)
    }

    suspend fun deleteFood(item: Dinner){
        repository.deleteDinner(item)
        viewModelScope.launch {
            _dinnerUiState.value = repository.getDinner(selectedDate.value).map { DinnerUiState(it) }.first()
        }
    }

    override fun updateSelectedDate(date: LocalDate) {
        selectedDate.value = date
        viewModelScope.launch {
            _dinnerUiState.value = repository.getDinner(date).map { DinnerUiState(it) }.first()
        }
    }
}

class SnackViewModel(private val repository: FoodRepository) : ViewModel(),
    MealViewModel<SnackUiState> {

    override val selectedDate = mutableStateOf(LocalDate.now())
    private val _snackUiState = MutableStateFlow(SnackUiState())
    override val mealUiState: MutableStateFlow<SnackUiState> = _snackUiState

    suspend fun insertFood(item: Snack){
        repository.insertSnack(item)
    }

    suspend fun deleteFood(item: Snack){
        repository.deleteSnack(item)
        viewModelScope.launch {
            _snackUiState.value = repository.getSnack(selectedDate.value).map { SnackUiState(it) }.first()
        }
    }

    override fun updateSelectedDate(date: LocalDate) {
        selectedDate.value = date
        viewModelScope.launch {
            _snackUiState.value = repository.getSnack(date).map { SnackUiState(it) }.first()
        }
    }
}

class TotalViewModel(private val repository: TotalRepository) : ViewModel() {

    suspend fun getLastTenDates(): List<LocalDate> {
        return repository.getLastTenDates().first()
    }

    suspend fun getAverageCalories(): Int {
        return repository.getAverageCalories().firstOrNull() ?: 0
    }

    suspend fun getMostPopularFood(): String =
        repository.getPopularFood().firstOrNull()?.food?.name?.split(":")?.firstOrNull() ?: ""

    suspend fun getProteins(date: LocalDate): Int {
        return repository.getProteins(date).firstOrNull() ?: 0
    }

    suspend fun getFats(date: LocalDate): Int {
        return repository.getFats(date).firstOrNull() ?: 0
    }

    suspend fun getCarbs(date: LocalDate): Int {
        return repository.getCarbs(date).firstOrNull() ?: 0
    }

    suspend fun getCalories(date: LocalDate): Int {
        return repository.getCalories(date).firstOrNull() ?: 0
    }

    suspend fun upOrInNut(item: Total) {
        repository.upOrInNutrients(item)
    }

    suspend fun delNutrients(date: LocalDate, delProteins: Int, delFats: Int, delCarbs: Int, delCalories: Int) {
        repository.delNutrients(date, delProteins, delFats, delCarbs, delCalories)
    }
}

data class BreakfastUiState(val breakfastList: List<Breakfast> = listOf())

data class LunchUiState(val lunchList: List<Lunch> = listOf())

data class DinnerUiState(val dinnerList: List<Dinner> = listOf())

data class SnackUiState(val snackList: List<Snack> = listOf())

