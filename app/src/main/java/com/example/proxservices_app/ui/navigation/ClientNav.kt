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
// Importamos las nuevas pantallas del cliente
import com.example.proxservices_app.ui.screen.client.ClientHomeScreen
import com.example.proxservices_app.ui.screen.client.ClientMessagesScreen
import com.example.proxservices_app.ui.screen.client.ClientProfileScreen
import com.example.proxservices_app.ui.screen.client.ClientServicesScreen
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.GrisTextoPrincipal
import com.example.proxservices_app.ui.theme.PrincipalAzul

// Este archivo, ClientNav.kt, define la estructura de navegación principal para la sección "Cliente" de la aplicación.
// Sigue el mismo patrón que WorkerNav.kt, usando un Scaffold y un NavHost para gestionar la navegación
// a través de una barra inferior, garantizando una experiencia de usuario consistente en toda la app.
@Composable
fun ClientNav() {
    val navController = rememberNavController()

    val clientNavItems = listOf(
        NavItem("Home", { painterResource(id = R.drawable.ic_home) }, "client_home"),
        NavItem("Mensajes", { painterResource(id = R.drawable.ic_messages) }, "client_messages"),
        NavItem("Servicios", { painterResource(id = R.drawable.ic_servicios) }, "client_services"),
        NavItem("Perfil", { painterResource(id = R.drawable.ice_user) }, "client_profile")
    )

    Scaffold(
        containerColor = Blanco,
        bottomBar = {
            NavigationBar(
                containerColor = Blanco,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                clientNavItems.forEach { navItem ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true
                    val iconSize = when (navItem.route) {
                        "client_services", "client_profile" -> 37.dp
                        else -> 32.dp
                    }
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
                                modifier = Modifier.size(iconSize)
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
            startDestination = "client_home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("client_home") { ClientHomeScreen() }
            composable("client_messages") { ClientMessagesScreen() }
            composable("client_services") { ClientServicesScreen() }
            composable("client_profile") { ClientProfileScreen() }
        }
    }
}









