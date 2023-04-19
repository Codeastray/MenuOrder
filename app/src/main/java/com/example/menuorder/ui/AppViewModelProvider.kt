package com.example.menuorder.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.menuorder.MenuApplication
import com.example.menuorder.data.Dish
import com.example.menuorder.data.MenuRepository
import com.example.menuorder.data.OfflineMenuRepository
import com.example.menuorder.ui.home.MenuInsertViewModel

import com.example.menuorder.ui.home.MenuTotalViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {

            MenuTotalViewModel(
                this.createSavedStateHandle(),
                menuApplication().container.menuRepository,

            )
        }

        initializer {
            val menuRepository = menuApplication().container.menuRepository
            val offlineMenuRepository = menuRepository as OfflineMenuRepository
            val dishDao = offlineMenuRepository.dishDao
            val drinkDao = offlineMenuRepository.drinkDao
            MenuInsertViewModel(
                this.createSavedStateHandle(),
                menuApplication().container.menuRepository,
                        dishDao = dishDao,
                drinkDao = drinkDao
            )
        }
    }



    }

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.menuApplication(): MenuApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MenuApplication)
