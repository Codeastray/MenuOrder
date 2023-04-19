package com.example.menuorder.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import com.example.menuorder.data.*
import kotlinx.coroutines.flow.*

class MenuTotalViewModel(
    savedStateHandle: SavedStateHandle,
    menuRepository: MenuRepository,
    private val dishDao: DishDao,
    private val drinkDao: DrinkDao,
) : ViewModel() {
    val toppingName: Int = checkNotNull(savedStateHandle[MenuTotalDestination.toppingName])
    val totalDishUiState: StateFlow<TotalDishUiState> =
        menuRepository.getAllDishesStream().map { TotalDishUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TotalDishUiState()
            )
    val totalDrinkUiState: StateFlow<TotalDrinkUiState> =
        menuRepository.getAllDrinkStream().map { TotalDrinkUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TotalDrinkUiState()
            )

    suspend fun deleteAllItem() {
        dishDao.deleteAllDishes()
        drinkDao.deleteAllDrinks()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TotalDishUiState(val dishList: List<Dish> = listOf())
data class TotalDrinkUiState(val drinkList: List<Drink> = listOf())