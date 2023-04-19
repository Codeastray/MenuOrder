package com.example.menuorder.data

import android.content.ClipData
import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface DishDao {
    @Insert
    suspend fun insert(dish: Dish)

    suspend fun checkDishNameInsert(dish: Dish) {
        val existingDish = getDishByName(dish.name).firstOrNull()

        if (existingDish != null) {

            update(dish.copy(id = existingDish.id))
        } else {
            insert(dish)
        }
    }
    @Update
    suspend fun update(dish: Dish)
    @Delete
    suspend fun delete(dish: Dish)
    @Query("SELECT * from dishes WHERE id = :id")
    fun getDish(id: Int): Flow<Dish>
    @Query("SELECT * from dishes ORDER BY name ASC")
    fun getAllDishes(): Flow<List<Dish>>

    @Query("SELECT * from dishes WHERE name = :name")
    fun getDishByName(name: String): Flow<Dish>
    @Query("DELETE FROM dishes")
    suspend fun deleteAllDishes()

    suspend fun checkDishDelete(dish: Dish) {
        val existingDish = getDishByName(dish.name).firstOrNull()

        if (existingDish?.quantity == 0) {
            Log.d("existingDish", existingDish.toString())
            delete(dish.copy(id = existingDish.id))
        }
    }
}

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(drink: Drink)

    suspend fun checkDrinkNameInsert(drink: Drink) {
        val existingDrink = getDrinkByName(drink.name).firstOrNull()

        if (existingDrink != null) {

            update(drink.copy(id = existingDrink.id))
        } else {
            insert(drink)
        }
    }
    @Update
    suspend fun update(drink: Drink)
    @Delete
    suspend fun delete(drink: Drink)
    @Query("SELECT * from drinks WHERE id = :id")
    fun getDrink(id: Int): Flow<Drink>
    @Query("SELECT * from drinks ORDER BY name ASC")
    fun getAllDrinks(): Flow<List<Drink>>

    @Query("SELECT * from drinks WHERE name = :name")
    fun getDrinkByName(name: String): Flow<Drink>
    @Query("DELETE FROM drinks")
    suspend fun deleteAllDrinks()

    suspend fun checkDrinkDelete(drink: Drink) {
        val existingDrink = getDrinkByName(drink.name).firstOrNull()

        if (existingDrink?.quantity == 0) {
            Log.d("existingDish", existingDrink.toString())
            delete(drink.copy(id = existingDrink.id))
        }
    }

}