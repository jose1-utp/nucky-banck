package com.example.proyect.nucky_banck.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyect.nucky_banck.presentation.home.HomeView
import com.example.proyect.nucky_banck.presentation.home.HomeViewModel
import com.example.proyect.nucky_banck.presentation.login.LoginView
import com.example.proyect.nucky_banck.presentation.register.RegisterView
import com.example.proyect.nucky_banck.presentation.transfer.TransferView

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginView(navController = navController)
        }

        composable("home/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""

            val homeViewModel: HomeViewModel = viewModel()

            HomeView(
                cedula = cedula,
                navController = navController,
                onLogout = {
                    homeViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home/{cedula}") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterView(navController = navController)
        }

        composable("transfer/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            TransferView(
                cedula = cedula,
                navController = navController
            )
        }
    }
}
