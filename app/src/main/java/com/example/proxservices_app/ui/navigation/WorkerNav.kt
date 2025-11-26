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
// Importamos las nuevas pantallas reales desde su ubicación correcta
import com.example.proxservices_app.ui.screen.worker.WorkerHomeScreen
import com.example.proxservices_app.ui.screen.worker.WorkerJobsScreen
import com.example.proxservices_app.ui.screen.worker.WorkerMessagesScreen
import com.example.proxservices_app.ui.screen.worker.WorkerPointsScreen
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.GrisTextoPrincipal
import com.example.proxservices_app.ui.theme.PrincipalAzul

// Este archivo, WorkerNav.kt, define la estructura de navegación principal para la sección "Trabajador" de la aplicación.
// Utiliza un Scaffold para organizar la pantalla, que incluye una barra de navegación inferior (NavigationBar).
// El NavHost gestiona las diferentes pantallas (Composables) a las que se puede navegar.
// Cada ítem de la barra (NavigationBarItem) corresponde a una ruta de navegación y tiene una animación
// que muestra el texto solo cuando el ítem está seleccionado, proporcionando una interfaz limpia y moderna.

@Composable
fun WorkerNav() {
    val navController = rememberNavController()

    val workerNavItems = listOf(
        NavItem("Home", { painterResource(id = R.drawable.ic_home) }, "worker_home"),
        NavItem("Mensajes", { painterResource(id = R.drawable.ic_messages) }, "worker_messages"),
        // --- CORRECCIÓN DEL NOMBRE DEL ICONO ---
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
        // El NavHost ahora llama a las pantallas reales que importamos
        NavHost(
            navController = navController,
            startDestination = "worker_home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("worker_home") { WorkerHomeScreen() }
            composable("worker_messages") { WorkerMessagesScreen() }
            composable("worker_jobs") { WorkerJobsScreen() }
            composable("worker_points") { WorkerPointsScreen() }
        }
    }
}


















