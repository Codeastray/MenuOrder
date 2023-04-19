package com.example.menuorder.ui.home

import android.content.ClipData

import com.example.menuorder.data.Dish
import com.example.menuorder.data.Drink

data class MenuUiState  (
    val dish_id: Int = 0,
    val dish_name: String = "",
    val dish_price: String = "",
    val dish_quantity: Int = 0,

    val drink_id: Int = 0,
    val drink_name: String = "",
    val drink_price: String = "",
    val drink_quantity: Int = 0,

    val actionEnabled: Boolean = false
)

fun MenuUiState.toDish(): Dish = Dish(
    id = dish_id,
    name = dish_name,
    price = dish_price.toIntOrNull() ?: 0,
    quantity = dish_quantity
)

fun MenuUiState.toDrink(): Drink = Drink(
    id = drink_id,
    name = drink_name,
    price = drink_price.toIntOrNull() ?: 0,
    quantity = drink_quantity
)

fun Dish.toMenuUiState(actionEnabled: Boolean = false): MenuUiState = MenuUiState(
    dish_id = id,
    dish_name = name,
    dish_price = price.toString(),
    dish_quantity = quantity,
    actionEnabled = actionEnabled
)

fun Drink.toMenuUiState(actionEnabled: Boolean = false): MenuUiState = MenuUiState(
    drink_id = id,
    drink_name = name,
    drink_price = price.toString(),
    drink_quantity = quantity,
    actionEnabled = actionEnabled
)

fun MenuUiState.isValid(): Boolean {
    return dish_name.isNotBlank()
            && dish_price.isNotBlank()
            && dish_quantity > 0
            && drink_name.isNotBlank()
            && drink_price.isNotBlank()
            && drink_quantity > 0
}