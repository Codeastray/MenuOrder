package com.example.menuorder.data

import com.example.menuorder.R

class MenuData() {
    fun loadMeals(): List<Meals> {
        return listOf<Meals>(
            Meals(R.string.dish1),
            Meals(R.string.dish2),
            Meals(R.string.dish3),
            Meals(R.string.dish4),
            Meals(R.string.dish5),
            Meals(R.string.dish6),
        ).onEachIndexed { index, meal ->
            meal.id = index
        }
    }

    fun loadDrinks(): List<Drinks> {
        return listOf<Drinks>(
            Drinks(R.string.drink1),
            Drinks(R.string.drink2),
            Drinks(R.string.drink3),
            Drinks(R.string.drink4),
            Drinks(R.string.drink5),
            Drinks(R.string.drink6)
        ).onEachIndexed { index, drink ->
            drink.id = index
        }
    }

    fun loadToppings(): List<Toppings> {
        return listOf<Toppings>(
            Toppings(R.string.topping1),
            Toppings(R.string.topping2),
            Toppings(R.string.topping3)
        ).onEachIndexed { index, topping ->
            topping.id = index
        }
    }
}
