package com.example.proxservices_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proxservices_app.ui.screen.client.ClientHomeScreen
import com.example.proxservices_app.ui.screen.login.LoginScreen
import com.example.proxservices_app.ui.screen.register.RegisterClientScreen
import com.example.proxservices_app.ui.screen.register.RegisterWorkerScreen
import com.example.proxservices_app.ui.screen.welcome.WelcomeScreen
import com.example.proxservices_app.ui.screen.worker.WorkerHomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "welcome") {


        composable("welcome") {
            WelcomeScreen(navController)
        }


        composable("login") {
            LoginScreen(navController)
        }


        composable("register_client") {
            RegisterClientScreen(navController)
        }
        composable("register_worker") {
            RegisterWorkerScreen(navController)
        }


        composable("client_home") {
            ClientHomeScreen(
                onNavigateToWorkerView = { workerId ->
                    // Aquí iría la navegación al perfil del trabajador
                    // Ejemplo: navController.navigate("worker_profile/$workerId")
                    println("Navegar al perfil del trabajador: $workerId")
                },
                onSearchClick = {
                    // Aquí iría la navegación a la pantalla de búsqueda
                    // Ejemplo: navController.navigate("search")
                    println("Click en buscar")
                }
            )
        }

        composable("worker_home") {
            WorkerNav()
        }
    }
}

