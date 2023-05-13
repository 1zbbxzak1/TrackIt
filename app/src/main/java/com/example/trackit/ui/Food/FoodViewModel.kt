package com.example.trackit.ui.Food

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackit.data.food.FoodRepository
import com.example.trackit.data.workout.WorkoutEntity
import com.example.trackit.ui.Nutrition.FoodData
import com.example.trackit.ui.workout.WorkoutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class FoodViewModel(private val foodRepository: FoodRepository) : ViewModel() {


    private val _foodList = MutableLiveData<List<FoodData>>()
    val foodList: LiveData<List<FoodData>>
        get() = _foodList

    fun insert(foodData: FoodData) {
        viewModelScope.launch {
            foodRepository.insert(foodData)
        }
    }

    fun update(foodData: FoodData) {
        viewModelScope.launch {
            foodRepository.update(foodData)
        }
    }

    fun delete(food: FoodData) {
        viewModelScope.launch {
            foodRepository.delete(food)
        }
    }
}

//    val selectedDate = mutableStateOf(LocalDate.now())
//
//    private val _foodUiState = MutableStateFlow(FoodUiState())
//
//    val foodUiState: StateFlow<FoodUiState> = _foodUiState
//
//    fun updateDate(date: LocalDate){
//        selectedDate.value = date
//        viewModelScope.launch {
//            _foodUiState.value = foodRepository.getItemsOnDate(date).map { FoodUiState(it) }.first()
//        }
//    }
//
//    fun insert(foodData: FoodData) {
//        viewModelScope.launch {
//            foodRepository.insert(foodData)
//        }
//    }
//
//
//    suspend fun updateFood(food: FoodData){
//        foodRepository.update(food)
//        viewModelScope.launch {
//            _foodUiState.value = foodRepository.getItemsOnDate(selectedDate.value).map { FoodUiState(it) }.first()
//        }
//    }
//
//    suspend fun deleteFood(food: FoodData){
//        foodRepository.delete(food)
//        viewModelScope.launch {
//            _foodUiState.value = foodRepository.getItemsOnDate(selectedDate.value).map { FoodUiState(it) }.first()
//        }
//    }
//}
//
//data class FoodUiState(val itemList: List<FoodData> = listOf())