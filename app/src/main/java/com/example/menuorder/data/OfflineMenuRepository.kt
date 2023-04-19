package com.example.menuorder.data

import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OfflineMenuRepository(
    val dishDao: DishDao,
    val drinkDao: DrinkDao):MenuRepository {

    override fun getAllDishesStream(): Flow<List<Dish>> = dishDao.getAllDishes()

    override fun getDishStream(id: Int): Flow<Dish?> = dishDao.getDish(id)

    override  suspend fun insertDish(dish:Dish) = dishDao.insert(dish)

    override fun getDishByName(name: String): Flow<Dish> = dishDao.getDishByName(name)

    override suspend fun checkDishNameInsert(dish: Dish)= dishDao.checkDishNameInsert(dish)

    override suspend fun deleteDish(dish:Dish)= dishDao.delete(dish)

    override suspend fun updateDish(dish:Dish)= dishDao.update(dish)

    override  suspend fun deleteAllDishes() = dishDao.deleteAllDishes()

    override suspend fun checkDishDelete(dish: Dish)= dishDao.checkDishDelete(dish)
    //--------------------------------------------------------------------------------

    override fun getAllDrinkStream(): Flow<List<Drink>> = drinkDao.getAllDrinks()


    override fun getDrinkStream(id: Int): Flow<Drink?> = drinkDao.getDrink(id)


    override suspend fun insertDrink(drink: Drink) = drinkDao.insert(drink)

    override fun getDrinkByName(name: String): Flow<Drink> = drinkDao.getDrinkByName(name)

    override suspend fun checkDrinkNameInsert(drink: Drink)= drinkDao.checkDrinkNameInsert(drink)


    override suspend fun deleteDrink(drink: Drink)= drinkDao.delete(drink)


    override suspend fun updateDrink(drink: Drink)= drinkDao.update(drink)

    override  suspend fun deleteAllDrinks() = drinkDao.deleteAllDrinks()

    override  suspend fun checkDrinkDelete(drink: Drink) =  drinkDao.checkDrinkDelete(drink)
}