package com.example.proyect.nucky_banck.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyect.nucky_banck.presentation.home.HomeView
import com.example.proyect.nucky_banck.presentation.login.LoginView
import com.example.proyect.nucky_banck.presentation.register.RegisterView
import com.example.proyect.nucky_banck.presentation.transfer.TransferView

// Define todas las pantallas de la aplicación y cómo navegar entre ellas.
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = "login"   // La app siempre inicia en el Login
    ) {

        // Pantalla de Login
        composable("login") {
            LoginView(navController = navController)
        }

        // Pantalla de Home — recibe la cédula del usuario que inició sesión
        composable("home/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""

            // 1. Obtenemos la instancia del ViewModel de forma limpia
            val homeViewModel: com.example.proyect.nucky_banck.presentation.home.HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

            HomeView(
                cedula        = cedula,
                navController = navController,
                // Quitamos la línea de viewModel = homeViewModel para que no te dé error
                onLogout      = {
                    // 2. Ejecutamos el cierre de sesión directamente aquí
                    homeViewModel.logout()

                    // 3. Volvemos al login limpiando el historial de la pila
                    navController.navigate("login") {
                        popUpTo("home/{cedula}") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterView(navController = navController)
        }

        // Pantalla de Transferencia — recibe la cédula del usuario que transfiere
        composable("transfer/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            TransferView(
                cedula        = cedula,
                navController = navController
            )
        }
    }
}
