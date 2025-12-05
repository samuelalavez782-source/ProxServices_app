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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proxservices_app.R
import com.example.proxservices_app.ui.screen.client.BusquedaScreen
import com.example.proxservices_app.ui.screen.client.ClientHomeScreen
import com.example.proxservices_app.ui.screen.client.ClientMessagesScreen
import com.example.proxservices_app.ui.screen.client.ClientProfileScreen
import com.example.proxservices_app.ui.screen.client.ClientServicesScreen
import com.example.proxservices_app.ui.screen.client.FilterState
import com.example.proxservices_app.ui.screen.client.FiltrosAvanzadosScreen
import com.example.proxservices_app.ui.screen.client.CategoryResultsScreen
import com.example.proxservices_app.ui.screen.client.ConfirmarContratacionScreen
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.GrisTextoPrincipal
import com.example.proxservices_app.ui.theme.PrincipalAzul

// Definimos ClientDestinations aquí
object ClientDestinations {
    const val HOME = "client_home"
    const val BUSQUEDA = "client_busqueda"
    const val MESSAGES = "client_messages"
    const val SERVICES = "client_services"
    const val PROFILE = "client_profile"
    const val FILTROS = "client_filtros"
    // RUTA CON ARGUMENTO DE CATEGORÍA
    const val CATEGORY_RESULTS_ROUTE = "client_category_results/{categoryName}"

    // 🚀 RUTA CORREGIDA: Incluye profesional y oficio
    const val CONFIRM_CONTRATACION = "confirmarContratacion"
    const val CONFIRM_CONTRATACION_WITH_ARGS = "$CONFIRM_CONTRATACION/{professionalName}/{professionalOficio}"

    // Función de ayuda para construir la ruta real
    fun categoryResults(categoryName: String) = "client_category_results/$categoryName"
    fun confirmContratacion(professionalName: String, professionalOficio: String) =
        "$CONFIRM_CONTRATACION/$professionalName/$professionalOficio"

}

// ...


@Composable
fun ClientNav() {
    val navController = rememberNavController()

    var currentFiltersState by remember { mutableStateOf(FilterState()) }

    // ❌ CÓDIGO ELIMINADO: Estas variables no pueden estar aquí
    // val professionalName = backStackEntry.arguments?.getString("name") ?: "Profesional Desconocido"
    // val professionalOficio = backStackEntry.arguments?.getString("oficio") ?: "Servicio General"

    // clientNavItems usa la data class NavItem que debe estar en NavItem.kt
    val clientNavItems = listOf(
        NavItem("Home", { painterResource(id = R.drawable.ic_home) }, ClientDestinations.HOME),
        NavItem("Mensajes", { painterResource(id = R.drawable.ic_messages) }, ClientDestinations.MESSAGES),
        NavItem("Servicios", { painterResource(id = R.drawable.ic_servicios) }, ClientDestinations.SERVICES),
        NavItem("Perfil", { painterResource(id = R.drawable.ice_user) }, ClientDestinations.PROFILE)
    )

    Scaffold(
        containerColor = Blanco,
        bottomBar = {
            // ... (NavigationBar sin cambios)
            NavigationBar(containerColor = Blanco) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                clientNavItems.forEach { navItem ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route?.startsWith(navItem.route) == true
                    } == true

                    val iconSize = when (navItem.route) {
                        ClientDestinations.SERVICES, ClientDestinations.PROFILE -> 37.dp
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
                                Text(text = navItem.label, fontSize = 12.sp)
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
            startDestination = ClientDestinations.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            // RUTA HOME
            composable(ClientDestinations.HOME) {
                ClientHomeScreen(
                    onSearchClick = { navController.navigate(ClientDestinations.BUSQUEDA) }
                )
            }

            // ... (BusquedaScreen, FiltrosAvanzadosScreen, CategoryResultsScreen sin cambios)

            // RUTA DE BÚSQUEDA
            composable(ClientDestinations.BUSQUEDA) {
                BusquedaScreen(
                    navController = navController,
                    appliedFilters = currentFiltersState,
                    navigateToFilters = { navController.navigate(ClientDestinations.FILTROS) }
                )
            }

            // RUTA DE FILTROS AVANZADOS
            composable(ClientDestinations.FILTROS) {
                FiltrosAvanzadosScreen(
                    currentFilters = currentFiltersState,
                    onBack = { navController.popBackStack() },
                    onApplyFilters = { newFilters ->
                        currentFiltersState = newFilters
                        navController.popBackStack()
                    },
                    onResetFilters = {
                        currentFiltersState = FilterState()
                        navController.popBackStack()
                    }
                )
            }

            // **NUEVA RUTA DE RESULTADOS DE CATEGORÍA**
            composable(
                route = ClientDestinations.CATEGORY_RESULTS_ROUTE,
                arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Servicios"
                CategoryResultsScreen(
                    categoryName = categoryName,
                    navController = navController,
                    onBack = { navController.popBackStack() }
                )
            }

            // 🚀 RUTA DE CONFIRMACIÓN DE CONTRATACIÓN (CORREGIDA)
            composable(
                route = ClientDestinations.CONFIRM_CONTRATACION_WITH_ARGS,
                arguments = listOf(
                    navArgument("professionalName") { type = NavType.StringType },
                    navArgument("professionalOficio") { type = NavType.StringType } // 👈 ARGUMENTO AÑADIDO
                )
            ) { backStackEntry ->
                // Obtener argumentos
                val professionalName = backStackEntry.arguments?.getString("professionalName") ?: "Profesional Desconocido"
                val professionalOficio = backStackEntry.arguments?.getString("professionalOficio") ?: "Servicio General"

                // Definir la acción de navegación al confirmar
                val navigateToMisServicios: () -> Unit = {
                    navController.navigate(ClientDestinations.SERVICES) {
                        popUpTo(ClientDestinations.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                // Llamada al Composable con los 4 argumentos requeridos
                ConfirmarContratacionScreen(
                    professionalName = professionalName,
                    professionalOficio = professionalOficio, // 👈 Se pasa el argumento
                    onBack = { navController.popBackStack() },
                    onConfirmAndNavigate = navigateToMisServicios // 👈 Se pasa la función de navegación
                )
            }

            // Rutas de la barra inferior
            composable(ClientDestinations.MESSAGES) { ClientMessagesScreen() }
            composable(ClientDestinations.SERVICES) { ClientServicesScreen() }
            composable(ClientDestinations.PROFILE) { ClientProfileScreen() }
        }
    }
}