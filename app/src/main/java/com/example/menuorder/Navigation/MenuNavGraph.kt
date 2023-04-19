package com.example.menuorder.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.menuorder.ui.AppViewModelProvider
import com.example.menuorder.ui.home.*


@Composable
fun MenuNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: MenuInsertViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    NavHost(
        navController = navController,
        startDestination = MenuDestination.route,
        modifier = modifier
    ) {

        composable(route = MenuDestination.route) {
            MenuOrderApp(
                navigateToTotal = {
                    navController.navigate("${MenuTotalDestination.route}/${it}")
                },
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(
            route = MenuTotalDestination.routeWithArgs,
            arguments = listOf(navArgument(MenuTotalDestination.toppingName) {
                type = NavType.IntType
            })
        ) {
            MenuTotalScreen(
                navController = navController,
                navBackToMenuOrder = {
                    navController.popBackStack(
                        MenuDestination.route,
                        inclusive = false
                    )
                },
                insertViewModel = viewModel
            )
        }
    }
}