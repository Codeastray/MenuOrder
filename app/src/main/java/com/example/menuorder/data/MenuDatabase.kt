package com.example.menuorder.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Dish::class,Drink::class], version = 1, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun DishDao(): DishDao
    abstract fun DrinkDao(): DrinkDao
    companion object {
        @Volatile
        private var Instance: MenuDatabase? = null
        fun getDatabase(context: Context): MenuDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MenuDatabase::class.java, "menu_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}