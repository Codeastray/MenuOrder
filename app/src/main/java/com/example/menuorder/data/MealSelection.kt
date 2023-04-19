package com.example.menuorder.data

import androidx.annotation.StringRes
import java.util.*

data class Meals(
    @StringRes
    val meal: Int,
    var id: Int = 0
)

data class Drinks(
    @StringRes
    val drink: Int,
    var id: Int = 0
)

data class Toppings(
    @StringRes
    val topping: Int,
    var id: Int = 0
)