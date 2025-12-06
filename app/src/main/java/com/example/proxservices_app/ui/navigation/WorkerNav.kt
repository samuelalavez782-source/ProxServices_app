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
import com.example.proxservices_app.R
import com.example.proxservices_app.ui.screen.worker.WorkerHomeScreen
import com.example.proxservices_app.ui.screen.worker.WorkerJobsScreen
import com.example.proxservices_app.ui.screen.worker.WorkerMessagesScreen
import com.example.proxservices_app.ui.screen.worker.WorkerPointsScreen
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.GrisTextoPrincipal
import com.example.proxservices_app.ui.theme.PrincipalAzul

@Composable
fun WorkerNav() {
    val navController = rememberNavController()

    val workerNavItems = listOf(
        NavItem("Home", { painterResource(id = R.drawable.ic_home) }, "worker_home"),
        NavItem("Mensajes", { painterResource(id = R.drawable.ic_messages) }, "worker_messages"),
        NavItem("Trabajos", { painterResource(id = R.drawable.ice_jobs) }, "worker_jobs"),
        NavItem("Puntos", { painterResource(id = R.drawable.ic_puntos) }, "worker_points")
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
                    val isSelected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = navItem.icon(),
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
        NavHost(
            navController = navController,
            startDestination = "worker_home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. WorkerHomeScreen
            // Requiere callbacks: onNavigateToConfirmation y onNavigateToNotifications
            composable("worker_home") {
                WorkerHomeScreen(
                    onNavigateToConfirmation = { jobId ->
                        // Aquí iría la navegación a la confirmación, ej:
                        // navController.navigate("confirmation/$jobId")
                    },
                    onNavigateToNotifications = {
                        // Navegación a notificaciones
                        // navController.navigate("notifications")
                    }
                )
            }

            // 2. WorkerMessagesScreen
            // Según el error, requiere 'navController'
            composable("worker_messages") {
                WorkerMessagesScreen(navController = navController)
            }

            // 3. WorkerJobsScreen
            // Según el error, requiere 'navController'
            composable("worker_jobs") {
                WorkerJobsScreen(navController = navController)
            }

            // 4. WorkerPointsScreen
            // Según el error, requiere 'navController' y 'onNavigateToRedemption'
            composable("worker_points") {
                WorkerPointsScreen(
                    navController = navController,
                    onNavigateToRedemption = {
                        // Lógica para navegar al canje
                    }
                )
            }
        }
    }
}