package com.example.menuorder.data

import android.content.ClipData
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getAllDishesStream(): Flow<List<Dish>>

    fun getDishStream(id: Int): Flow<Dish?>

    suspend fun insertDish(dish:Dish)

    suspend fun deleteDish(dish:Dish)

    suspend fun updateDish(dish:Dish)

    fun getDishByName(name: String): Flow<Dish?>

    suspend fun checkDishNameInsert(dish: Dish)

    suspend fun deleteAllDishes()

    suspend fun checkDishDelete(dish: Dish)
 //--------------------------------------------------------------------------------

    fun getAllDrinkStream(): Flow<List<Drink>>


    fun getDrinkStream(id: Int): Flow<Drink?>


    suspend fun insertDrink(drink: Drink)


    suspend fun deleteDrink(drink: Drink)


    suspend fun updateDrink(drink: Drink)

    fun getDrinkByName(name: String): Flow<Drink?>

    suspend fun checkDrinkNameInsert(drink: Drink)

    suspend fun deleteAllDrinks()

    suspend fun checkDrinkDelete(drink: Drink)
}