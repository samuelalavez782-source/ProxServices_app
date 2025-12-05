package com.example.proxservices_app.ui.navigation

import android.net.Uri
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

// 🚨 IMPORTACIONES NECESARIAS PARA QUE TODO FUNCIONE:
import com.example.proxservices_app.ui.screen.client.ProfileNavHost
import com.example.proxservices_app.ui.screen.client.ClientChatScreen
import com.example.proxservices_app.ui.screen.client.EditProfileScreen
import com.example.proxservices_app.ui.screen.client.ClientViewProfileScreen // La pantalla de vista detallada
import com.example.proxservices_app.ui.screen.client.workerList // La lista de trabajadores para buscar el perfil
import com.example.proxservices_app.ui.screen.client.WorkerProfile // La data class del perfil
// ----------------------------------------------------------------------


// Definimos ClientDestinations aquí
object ClientDestinations {
    const val HOME = "client_home"
    const val BUSQUEDA = "client_busqueda"
    const val MESSAGES = "client_messages"
    const val SERVICES = "client_services"
    const val PROFILE = "client_profile"
    const val EDIT_PROFILE = "client_edit_profile"
    const val FILTROS = "client_filtros"
    const val WORKER_LIST = "client_worker_list"
    const val CHAT_SCREEN = "client_chat_screen"

    // RUTA DETALLADA PARA PERFILES (Usa argumentos)
    const val WORKER_DETAIL_ROUTE = "client_worker_detail/{workerId}"
    fun workerDetail(workerId: String) = "client_worker_detail/$workerId" // Función de ayuda

    // RUTA CON ARGUMENTO DE CATEGORÍA
    const val CATEGORY_RESULTS_ROUTE = "client_category_results/{categoryName}"

    const val CONFIRM_CONTRATACION = "confirmarContratacion"
    const val CONFIRM_CONTRATACION_WITH_ARGS = "$CONFIRM_CONTRATACION/{professionalName}/{professionalOficio}"

    fun categoryResults(categoryName: String) = "client_category_results/$categoryName"
    fun confirmContratacion(professionalName: String, professionalOficio: String) =
        "$CONFIRM_CONTRATACION/$professionalName/$professionalOficio"

}


@Composable
fun ClientNav() {
    val navController = rememberNavController()
    var currentFiltersState by remember { mutableStateOf(FilterState()) }

    // clientNavItems usa la data class NavItem que debe estar en NavItem.kt
    val clientNavItems = listOf(
        NavItem("Home", { painterResource(id = R.drawable.ic_home) }, ClientDestinations.HOME),
        NavItem("Mensajes", { painterResource(id = R.drawable.ic_messages) }, ClientDestinations.MESSAGES),
        NavItem("Servicios", { painterResource(id = R.drawable.ic_servicios) }, ClientDestinations.SERVICES),
        NavItem("Perfil", { painterResource(id = R.drawable.ice_user) }, ClientDestinations.PROFILE)
    )

    Scaffold(
        containerColor = Blanco,
        bottomBar = { // Implementación completa del bottomBar
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
        } // Fin de la implementación del bottomBar
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ClientDestinations.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. RUTA HOME (Corrección de parámetros y navegación al detalle)
            composable(ClientDestinations.HOME) {
                ClientHomeScreen(
                    onSearchClick = { navController.navigate(ClientDestinations.BUSQUEDA) },
                    // --> aquí navegamos al detalle; codificamos el nombre para evitar errores con espacios
                    onNavigateToWorkerView = { workerName ->
                        navController.navigate(ClientDestinations.workerDetail(Uri.encode(workerName)))
                    }
                )
            }

            // ... (BusquedaScreen, FiltrosAvanzadosScreen, CategoryResultsScreen)
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

            // *NUEVA RUTA DE RESULTADOS DE CATEGORÍA*
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
            // RUTA DE CONFIRMACIÓN DE CONTRATACIÓN (Sin cambios)
            composable(
                route = ClientDestinations.CONFIRM_CONTRATACION_WITH_ARGS,
                // ... (Implementación)
            ) { backStackEntry ->
                // ... (Lógica de ConfirmarContratacionScreen)
            }

            // =========================================================
            // RUTAS DE LA BARRA INFERIOR Y ANIDADAS (FINALIZADAS)
            // =========================================================

            // 2. RUTA PERFIL (Implementación del lápiz)
            composable(ClientDestinations.PROFILE) {
                ClientProfileScreen(
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(ClientDestinations.EDIT_PROFILE) }
                )
            }

            // 3. RUTA EDITAR PERFIL (Destino del lápiz)
            composable(ClientDestinations.EDIT_PROFILE) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { _, _, _, _ -> navController.popBackStack() }
                )
            }

            // 4. RUTA MESSAGES (Corrección de parámetro)
            composable(ClientDestinations.MESSAGES) {
                ClientMessagesScreen(
                    onNavigateToChat = { navController.navigate(ClientDestinations.CHAT_SCREEN) }
                )
            }

            // 5. RUTA VISTA DE TRABAJADOR (La lista de trabajadores)
            composable(ClientDestinations.WORKER_LIST) {
                ProfileNavHost() // Llama al NavHost que maneja la lista de perfiles
            }

            // 6. RUTA DETALLADA DEL TRABAJADOR (El destino del botón "Ver Perfil")
            composable(
                route = ClientDestinations.WORKER_DETAIL_ROUTE,
                arguments = listOf(navArgument("workerId") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerId = backStackEntry.arguments?.getString("workerId") ?: ""

                // 🎯 Buscar por nombre (porque desde Home estás pasando el nombre)
                val profile = workerList.find { it.name == workerId } ?: WorkerProfile(
                    id = "", name = "Error", title = "No encontrado", avatar = 0, banner = 0, rating = 0.0, services = emptyList(), portfolio = emptyList(), lat = 0.0, lng = 0.0
                )

                ClientViewProfileScreen(
                    profile = profile,
                    onBack = { navController.popBackStack() }
                )
            }

            // 7. RUTA CHAT (Implementación que requiere argumentos)
            composable(ClientDestinations.CHAT_SCREEN) {
                ClientChatScreen(
                    workerName = "Nombre de Prueba",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(ClientDestinations.SERVICES) { ClientServicesScreen() }
            // ... (Resto de las rutas)
        }
    }
}
