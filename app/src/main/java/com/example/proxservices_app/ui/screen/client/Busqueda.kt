package com.example.proxservices_app.ui.screen.client

// ----------------------------------------------------
// IMPORTS
// ----------------------------------------------------
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource // Para usar R.drawable.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proxservices_app.R // Asegúrate de que esta ruta sea correcta
import com.example.proxservices_app.ui.navigation.ClientDestinations
import java.util.Locale

val sampleCategorries = listOf(
    // Reemplaza R.drawable.* con iconos existentes si falla
    Category(1, "Limpieza", R.drawable.ic_limpieza),
    Category(2, "Plomería", R.drawable.ic_plomeria),
    Category(3, "Electricidad", R.drawable.ic_electricidad),
    Category(4, "Jardinería", R.drawable.ic_jardineria),
    Category(5, "Pintura", R.drawable.ic_pintura),
    Category(6, "Carpintería", R.drawable.ic_carpinteria),
)

val allServiceResults = listOf(
    ServiceResult("Carlos Pérez", "Electricista", 4.8f, true),
    ServiceResult("Ana Rodríguez", "Plomera", 4.5f, false),
    ServiceResult("Javier Soto", "Jardinero", 4.9f, true),
    ServiceResult("Laura Gómez", "Limpiadora", 4.7f, true),
    ServiceResult("Sofía Núñez", "Pintora", 4.7f, false),
    ServiceResult("Miguel Díaz", "Carpintero", 4.6f, true),
    ServiceResult("Ricardo Flores", "Plomero", 4.3f, true),
)

val PrimaryTurquoise = Color(0xFF00C890)
val SearchBarBackground = Color(0xFFF0F0F0)
val Transparent = Color.Transparent

// ----------------------------------------------------
// COMPOSABLE PRINCIPAL
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaScreen(
    navController: NavController,
    appliedFilters: FilterState,
    onCategorySelected: (Category) -> Unit = {},
    navigateToFilters: () -> Unit // Función de navegación
) {
    var searchText by remember { mutableStateOf("") }

    val onCategorySelected: (Category) -> Unit = { category ->
        val route = ClientDestinations.categoryResults(category.name)
        navController.navigate(route)
    }
    // Lógica de filtrado APLICANDO los filtros
    val filteredResults = remember(searchText, appliedFilters, allServiceResults) {
        var results = allServiceResults

        // 1. Aplicar filtro de búsqueda por texto
        if (searchText.isNotBlank()) {
            results = results.filter { result ->
                result.profession.lowercase(Locale.ROOT).contains(searchText.lowercase(Locale.ROOT))
            }
        }

        // 2. Aplicar filtros avanzados

        // Filtro de profesión
        if (appliedFilters.selectedProfession.isNotBlank()) {
            results = results.filter {
                it.profession.equals(appliedFilters.selectedProfession, ignoreCase = true)
            }
        }

        // Filtro de calificación mínima
        if (appliedFilters.minRating > 0) {
            results = results.filter { it.rating >= appliedFilters.minRating }
        }

        // Filtro de disponibilidad
        if (appliedFilters.isAvailable) {
            results = results.filter { it.isAvailable }
        }

        results
    }


    Scaffold(
        containerColor = Color.White,
        contentColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                ) {
                    // 1. Barra de Búsqueda y Filtro
                    SearchAndFilterBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        onFilterClick = navigateToFilters
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // 2. Carrusel de Categorías
                    CategoryCarousel(
                        categories = sampleCategorries,
                        onCategorySelected = onCategorySelected
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // VISUALIZACIÓN DE LOS FILTROS APLICADOS
                    val activeFiltersCount =
                        (if (appliedFilters.minRating > 0) 1 else 0) +
                                (if (appliedFilters.isAvailable) 1 else 0) +
                                (if (appliedFilters.selectedProfession.isNotBlank()) 1 else 0)

                    if (activeFiltersCount > 0) {
                        Text(
                            text = "Filtros activos: $activeFiltersCount aplicados",
                            color = PrimaryTurquoise,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // 3. Lista de Resultados
                    RealSearchResultsList(results = filteredResults)
                }
            }
        }
    }
}

// ----------------------------------------------------
// FUNCIONES AUXILIARES (DEFINIDAS AQUÍ PARA EVITAR ERRORES DE SCOPE)
// ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            placeholder = { Text("Buscar trabajadores o...", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = Color.Gray)
            },
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SearchBarBackground,
                unfocusedContainerColor = SearchBarBackground,
                disabledContainerColor = SearchBarBackground,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Unspecified, // <-- SOLUCIÓN
                unfocusedIndicatorColor = Color.Unspecified, // <-- SOLUCIÓN
                disabledIndicatorColor = Color.Unspecified,
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Button(
            onClick = onFilterClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTurquoise),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.height(56.dp)
        ) {
            Text("Filtros", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun CategoryCarousel(
categories: List<Category>,
onCategorySelected: (Category) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFCCCCCC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = category.name, style = MaterialTheme.typography.labelSmall, color = Color.Black)
    }
}

@Composable
fun RealSearchResultsList(results: List<ServiceResult>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (results.isEmpty()) {
            item {
                Text(
                    text = "No se encontraron profesionales para la búsqueda o filtros aplicados.",
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(results) { result ->
                ServiceResultCard(result = result)
            }
        }
    }
}

@Composable
fun ServiceResultCard(result: ServiceResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SearchBarBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(PrimaryTurquoise)
        ) {
            Text(
                text = result.profession.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = result.name,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = result.profession + if (result.isAvailable) " (Disponible)" else "",
                style = MaterialTheme.typography.bodySmall,
                color = if (result.isAvailable) PrimaryTurquoise else Color.Gray
            )
        }
        Text(
            text = "${result.rating} ⭐",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}
