package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importante para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proxservices_app.R // Asegúrate de que esta importación sea correcta
import com.example.proxservices_app.ui.navigation.ClientDestinations
import com.example.proxservices_app.ui.screen.client.Servicio
// --- COLORES Y DATOS ---

// Colores
val LightBackground = Color(0xFFF7F7F7)
val AccentBlue = Color(0xFF00B0FF)

// Modelos de Datos
data class ProfessionalResult(
    val id: String,
    val name: String,
    val professionDetails: String,
    val rating: Int, // Usamos Int para calificación de 1 a 5
    val isAvailable: Boolean,
    val initials: String,
    val precio: Int,
    val circleColor: Color
)

// Datos de ejemplo para probar los filtros
val sampleProfessionals = listOf(
    // Rating 5, Disponible (Debe aparecer con ambos filtros)
    ProfessionalResult("1", "Carlos Hererra", "Plomería residencial\nInstalaciones sanitarias", 5, true, "CH", 45,PrimaryTurquoise),
    // Rating 4, No disponible (Debe aparecer solo con "Mejor Calificados")
    ProfessionalResult("2", "María González", "Plomería residencial", 4, false, "MG",35 ,AccentBlue),
    // Rating 3, Disponible (Debe aparecer solo con "Disponible ahora")
    ProfessionalResult("3", "Pedro Sánckz", "Plomería residencial", 3, true, "PS",25, PrimaryTurquoise),
    // Rating 2, No disponible (No debe aparecer con ningún filtro)
    ProfessionalResult("4", "Ana Torres", "Electricista", 2, false, "AT",10, AccentBlue),
    // Rating 5, Disponible
    ProfessionalResult("5", "Luis Ramos", "Jardinero", 5, true, "LR", 40,PrimaryTurquoise),
)

// Constantes para los estados de filtro
enum class FilterType {
    ALL, // Siempre seleccionado inicialmente o por defecto
    NEAREST, // Más cerca (Lógica de ubicación no implementada aquí)
    TOP_RATED, // Mejor Calificados (4 o más)
    AVAILABLE // Disponible ahora
}


// --- COMPOSABLE PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryResultsScreen(
    navController: NavController,
    categoryName: String,
    onBack: () -> Unit
) {
    // ESTADO: Almacena el filtro activo actualmente.
    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }

    // LÓGICA DE FILTRADO (Se recalcula cada vez que 'selectedFilter' cambia)
    val filteredProfessionals = remember(selectedFilter, sampleProfessionals) {
        when (selectedFilter) {
            FilterType.TOP_RATED -> sampleProfessionals.filter { it.rating >= 4 }
            FilterType.AVAILABLE -> sampleProfessionals.filter { it.isAvailable }
            // Los filtros 'ALL' y 'NEAREST' muestran todos los resultados
            FilterType.ALL, FilterType.NEAREST -> sampleProfessionals
        }
    }


    Scaffold(
        topBar = {
            CategoryResultsTopBar(categoryName, onBack)
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightBackground)
        ) {
            // 1. Cabecera con Imagen y Texto
            item { CategoryHeaderImage(categoryName) }

            // 2. Botones de Filtro
            item {
                FilterPillRow(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { filter ->
                        selectedFilter = if (selectedFilter == filter) FilterType.ALL else filter
                    }
                )
            }

            // 3. Resultados de la lista
            item {
                Text(
                    text = "Profesionales (${filteredProfessionals.size})", // Muestra la cuenta
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            // 4. Lista de Profesionales Filtrados
            items(filteredProfessionals) { profesional: ProfessionalResult -> // <--- TIPO CORREGIDO
                ProfessionalCard(
                    professional = profesional,
                    // 🚨 onProfessionalClick en ProfessionalCard pasa 'name', así que lo capturamos como '_'
                    onProfessionalClick = { _ ->
                        navController.navigate(route = ClientDestinations.confirmContratacion(
                            // Usamos las propiedades existentes en ProfessionalResult:
                            profesional.name,          // <--- Usa .name
                            profesional.professionDetails // <--- Usa .professionDetails
                        ))
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}


// --- FUNCIONES DE FILTRO RÁPIDO (MODIFICADAS) ---

@Composable
fun FilterPillRow(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mejor Calificados
        FilterPill(
            text = "Mejor calificados",
            isSelected = selectedFilter == FilterType.TOP_RATED,
            color = AccentBlue,
            onClick = { onFilterSelected(FilterType.TOP_RATED) } // MANDA EL FILTRO
        )

        // Disponible Ahora
        FilterPill(
            text = "Disponible ahora",
            isSelected = selectedFilter == FilterType.AVAILABLE,
            color = PrimaryTurquoise,
            onClick = { onFilterSelected(FilterType.AVAILABLE) } // MANDA EL FILTRO
        )

        // Más cerca (Por defecto, usa FilterType.ALL)
        // Nota: El botón "Más cerca" se mantiene como ejemplo, pero la lógica de ubicación es compleja
        FilterPill(
            text = "Más cerca",
            isSelected = selectedFilter == FilterType.NEAREST,
            color = PrimaryTurquoise,
            onClick = { onFilterSelected(FilterType.NEAREST) }
        )
        FilterPill(
            text = "Todos",
            isSelected = selectedFilter == FilterType.ALL,
            color = PrimaryTurquoise,
            onClick = { onFilterSelected(FilterType.ALL) }
        )
    }
}

@Composable
fun FilterPill(text: String, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) color.copy(alpha = 0.1f) else Color.White,
        border = if (isSelected) null else null,
        modifier = Modifier.clickable(onClick = onClick) // Usa el onClick para cambiar el estado
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (isSelected) color else Color.Black
            )
        }
    }
}

// --- Otras funciones auxiliares (sin cambios significativos) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryResultsTopBar(categoryName: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(categoryName, fontSize = 18.sp)
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(horizontal = 8.dp)
            )
        },
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun CategoryHeaderImage(categoryName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_limpieza),
            contentDescription = "Fondo de categoría",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .align(Alignment.CenterStart),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Profesionales\ndisponibles\ncerca de ti",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.graphicsLayer { alpha = 0.9f }
            )
        }
    }
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Filletts Nano", fontSize = 12.sp, color = Color.Gray)
        Text("fill tremabia sunetor ancollstdal patiang spadey o18", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun ProfessionalCard(
    professional: ProfessionalResult,
                     onProfessionalClick: (name: String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onProfessionalClick(professional.name) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(professional.circleColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = professional.initials,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Información del Profesional
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = professional.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = professional.professionDetails,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${professional.precio}",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Blue

                )
            }
            Text(
                text = "${professional.rating} ⭐",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black

            )

        }
    }
}