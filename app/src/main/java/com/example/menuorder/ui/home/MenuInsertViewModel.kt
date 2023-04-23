package com.example.menuorder.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menuorder.data.DishDao
import com.example.menuorder.data.DrinkDao
import com.example.menuorder.data.MenuRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuInsertViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val menuRepository: MenuRepository,
    private val dishDao: DishDao,
    private val drinkDao: DrinkDao,
) : ViewModel() {
    private val _badgeNumber = MutableStateFlow(mutableMapOf<Int, Int>())
    val badgeNumber: StateFlow<MutableMap<Int, Int>> = _badgeNumber.asStateFlow()
    private val _resetUI = MutableStateFlow(false)
    val resetUI: StateFlow<Boolean> = _resetUI.asStateFlow()
    fun clearBadgeNumber() {
        badgeNumber.value.clear()
    }

    fun setResetUI(value: Boolean) {
        _resetUI.value = value
    }

    fun dishCard(key: Int, value: Int) {
        _badgeNumber.value[key] = value
    }

    var menuUiState by mutableStateOf(MenuUiState())
        private set
    var menuUiStates = mutableListOf<MenuUiState>()
    val dishesList = mutableListOf<DishSet>()
    val drinksList = mutableListOf<DrinkSet>()
    fun updateUiState(newMenuUiState: MenuUiState) {
        menuUiState = newMenuUiState
        val (dish_id, dish_name, dish_price, dish_quantity, drink_id, drink_name, drink_price, drink_quantity) = menuUiState
        val dishSet = DishSet(dish_id, dish_name, dish_price, dish_quantity)
        val drinkSet = DrinkSet(drink_id, drink_name, drink_price, drink_quantity)
        val updatedDishesList = dishesList.map { dish ->
            if (dish.dish_name == dishSet.dish_name) {
                val newDishQuantity = dish.dish_quantity + dishSet.dish_quantity
                dish.copy(dish_price = dishSet.dish_price, dish_quantity = newDishQuantity)
            } else {
                dish
            }
        }
        dishesList.clear()
        dishesList.addAll(updatedDishesList)
        Log.d("model", dishesList.toString())
        if (!updatedDishesList.any { it.dish_name == dishSet.dish_name }) {
            dishesList.add(dishSet)
        }
        val updatedDrinksList = drinksList.map { drink ->
            if (drink.drink_name == drinkSet.drink_name) {
                val newDrinkQuantity = drink.drink_quantity + drinkSet.drink_quantity
                drink.copy(drink_price = drinkSet.drink_price, drink_quantity = newDrinkQuantity)
            } else {
                drink
            }
        }
        drinksList.clear()
        drinksList.addAll(updatedDrinksList)
        if (!updatedDrinksList.any { it.drink_name == drinkSet.drink_name }) {
            drinksList.add(drinkSet)
        }
        dishesList.removeAll { it.dish_name.isNullOrEmpty() }
        drinksList.removeAll { it.drink_name.isNullOrEmpty() }
    }
    fun getAllItem(): Flow<List<Any>> {
        val dishesStream = menuRepository.getAllDishesStream()
        val drinksStream = menuRepository.getAllDrinkStream()

        return combine(dishesStream, drinksStream) { dishes, drinks ->
            dishes + drinks
        }
    }


    fun deleteAllItem() {
        viewModelScope.launch {
            dishDao.deleteAllDishes()
            drinkDao.deleteAllDrinks()
        }

    }
    fun deleteUiState(name: String) {
        val deleteDishesList = dishesList.map { dish ->
            if (dish.dish_name == name) {
                dish.copy(dish_quantity = dish.dish_quantity - 1)
            } else {
                dish
            }
        }
        dishesList.clear()
        dishesList.addAll(deleteDishesList)
        val deleteDrinkList = drinksList.map { drink ->
            if (drink.drink_name == name) {
                drink.copy(drink_quantity = drink.drink_quantity - 1)
            } else {
                drink
            }
        }
        drinksList.clear()
        drinksList.addAll(deleteDrinkList)
    }

    fun clearAllList() {
        viewModelScope.launch {
            delay(1000)
            dishesList.clear()
            drinksList.clear()
        }
    }

    suspend fun saveItem() {
        viewModelScope.launch {
            dishesList.forEach { dishSet ->
                menuUiState = menuUiState.copy(
                    dish_id = dishSet.dish_id,
                    dish_name = dishSet.dish_name,
                    dish_price = dishSet.dish_price,
                    dish_quantity = dishSet.dish_quantity
                )
                menuRepository.checkDishNameInsert(menuUiState.toDish())
                if (menuUiState.dish_quantity == 0) {
                    menuRepository.checkDishDelete(menuUiState.toDish())
                }
            }
            drinksList.forEach { drinkSet ->
                menuUiState = menuUiState.copy(
                    drink_id = drinkSet.drink_id,
                    drink_name = drinkSet.drink_name,
                    drink_price = drinkSet.drink_price,
                    drink_quantity = drinkSet.drink_quantity
                )
                menuRepository.checkDrinkNameInsert(menuUiState.toDrink())
                if (menuUiState.drink_quantity == 0) { //在UI那邊還有設隱藏
                    menuRepository.checkDrinkDelete(menuUiState.toDrink())
                }
            }
        }
    }
}

data class DishSet(
    val dish_id: Int = 0,
    val dish_name: String,
    val dish_price: String,
    val dish_quantity: Int,
)

data class DrinkSet(
    val drink_id: Int = 0,
    val drink_name: String,
    val drink_price: String,
    val drink_quantity: Int,
)
