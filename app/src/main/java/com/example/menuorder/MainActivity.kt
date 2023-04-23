package com.example.menuorder

import android.content.ComponentCallbacks2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.menuorder.Navigation.MenuNavGraph
import com.example.menuorder.ui.AppViewModelProvider
import com.example.menuorder.ui.home.MenuInsertViewModel
import com.example.menuorder.ui.theme.MenuOrderTheme

class MainActivity : ComponentActivity() {
    lateinit var viewModel: MenuInsertViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MenuOrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel = viewModel(factory = AppViewModelProvider.Factory)
                    MenuNavGraph(viewModel = viewModel)
                }
            }
            viewModel.deleteAllItem()
        }

    }



}

