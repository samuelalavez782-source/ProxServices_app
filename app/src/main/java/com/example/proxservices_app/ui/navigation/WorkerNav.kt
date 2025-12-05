package com.example.proxservices_app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.proxservices_app.R
// Importamos tus pantallas
import com.example.proxservices_app.ui.screen.worker.WorkerHomeScreen
import com.example.proxservices_app.ui.screen.worker.WorkerJobConfirmationScreen
import com.example.proxservices_app.ui.screen.worker.WorkerJobsScreen
import com.example.proxservices_app.ui.screen.worker.WorkerMessagesScreen
import com.example.proxservices_app.ui.screen.worker.WorkerPointsScreen
// IMPORTACIONES NECESARIAS
import com.example.proxservices_app.ui.screen.worker.WorkerNotificationsScreen
// IMPORTAMOS LA NUEVA PANTALLA DE CANJE (Asegúrate de haber creado este archivo)
import com.example.proxservices_app.ui.screen.worker.WorkerPointRedemptionScreen
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.GrisTextoPrincipal
import com.example.proxservices_app.ui.theme.PrincipalAzul

// --- 1. DEFINICIÓN DE RUTAS CON ID DE ICONO ---
sealed class WorkerNavItem(val label: String, val route: String, val iconId: Int) {
    object Home : WorkerNavItem("Home", "worker_home", R.drawable.ic_home)
    object Messages : WorkerNavItem("Mensajes", "worker_messages", R.drawable.ic_messages)
    object Jobs : WorkerNavItem("Trabajos", "worker_jobs", R.drawable.ice_jobs)
    object Points : WorkerNavItem("Puntos", "worker_points", R.drawable.ic_puntos)

    // ¡OK! Ruta para la nueva pantalla de canje
    object PointRedemption : WorkerNavItem("Canjear", "point_redemption", R.drawable.ic_puntos)

    // ¡OK! Ruta de Notificaciones
    object Notifications : WorkerNavItem("Notificaciones", "worker_notifications", R.drawable.ic_bell)

    object JobConfirmation : WorkerNavItem("Confirmacion", "job_confirmation/{jobId}", R.drawable.ice_jobs) {
        fun createRoute(jobId: String) = "job_confirmation/$jobId"
    }
}


@Composable
fun WorkerNav() {
    val navController = rememberNavController()

    // Lista de ítems para la barra inferior (No incluye Notificaciones ni Confirmación ni Canje)
    val workerNavItems = listOf(
        WorkerNavItem.Home,
        WorkerNavItem.Messages,
        WorkerNavItem.Jobs,
        WorkerNavItem.Points
    )

    Scaffold(
        containerColor = Blanco,
        bottomBar = {
            NavigationBar(
                containerColor = Blanco,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                workerNavItems.forEach { navItem ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.route == navItem.route } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = navItem.iconId),
                                contentDescription = navItem.label,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        label = {
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = slideInVertically { it } + fadeIn(),
                                exit = slideOutVertically { it } + fadeOut()
                            ) {
                                Text(
                                    text = navItem.label,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrincipalAzul,
                            selectedTextColor = PrincipalAzul,
                            unselectedIconColor = GrisTextoPrincipal,
                            unselectedTextColor = Color.Transparent,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // --- 2. NAVHOST: DONDE ESTÁ LA CONEXIÓN ---
        NavHost(
            navController = navController,
            startDestination = WorkerNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // A. WorkerHomeScreen (ACTUALIZADO CON CALLBACK DE NOTIFICACIONES)
            composable(WorkerNavItem.Home.route) {
                WorkerHomeScreen(
                    onNavigateToConfirmation = { jobId ->
                        navController.navigate(WorkerNavItem.JobConfirmation.createRoute(jobId))
                    },
                    onNavigateToNotifications = {
                        navController.navigate(WorkerNavItem.Notifications.route)
                    }
                )
            }

            // B. WorkerMessagesScreen y WorkerJobsScreen (sin cambios)
            composable(WorkerNavItem.Messages.route) { WorkerMessagesScreen(navController = navController) }
            composable(WorkerNavItem.Jobs.route) { WorkerJobsScreen(navController = navController) }

            // C. WorkerPointsScreen (ACTUALIZADO CON CALLBACK DE CANJE)
            composable(WorkerNavItem.Points.route) {
                WorkerPointsScreen(
                    navController = navController,
                    // ¡NUEVO CALLBACK! Conecta el botón "Canjear" a la nueva ruta
                    onNavigateToRedemption = { navController.navigate(WorkerNavItem.PointRedemption.route) }
                )
            }

            // D. NUEVA PANTALLA DE CANJE DE PUNTOS
            composable(WorkerNavItem.PointRedemption.route) {
                // Asume que WorkerPointRedemptionScreen(navController: NavHostController) existe
                WorkerPointRedemptionScreen(navController = navController)
            }


            // E. NUEVA PANTALLA DE NOTIFICACIONES
            composable(WorkerNavItem.Notifications.route) {
                WorkerNotificationsScreen(navController = navController)
            }

            // F. WorkerJobConfirmationScreen (Con los dos parámetros corregidos)
            composable(
                route = WorkerNavItem.JobConfirmation.route,
                arguments = listOf(
                    navArgument("jobId") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId")

                WorkerJobConfirmationScreen(
                    jobId = jobId ?: "ERROR_ID",
                    navController = navController
                )
            }
        }
    }
}//fin



