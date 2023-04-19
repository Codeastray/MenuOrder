package com.example.menuorder.data

import android.content.Context

interface AppContainer {
    val menuRepository: MenuRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineMenuRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [MenuRepository]
     */
    override val menuRepository: MenuRepository by lazy {
        OfflineMenuRepository(
            MenuDatabase.getDatabase(context).DishDao(),
            MenuDatabase.getDatabase(context).DrinkDao()
        )
    }
}