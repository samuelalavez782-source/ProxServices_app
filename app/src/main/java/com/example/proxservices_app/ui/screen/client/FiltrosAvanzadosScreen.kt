package com.example.proxservices_app.ui.screen.client


// ----------------------------------------------------
// IMPORTS
// ----------------------------------------------------
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.ui.theme.PrincipalAzul
import kotlin.math.roundToInt
// ----------------------------------------------------

// Modelos de datos para el estado del filtro

// Constantes de diseño

val DarkBlueText = Color(0xFF1E3A8A) // Un azul oscuro para los títulos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosAvanzadosScreen(
    currentFilters: FilterState,
    onBack: () -> Unit,
    onApplyFilters: (FilterState) -> Unit,
    onResetFilters: () -> Unit
) {
    // Estado local de los filtros
    var localFilters by remember { mutableStateOf(currentFilters) }

    Scaffold(
        topBar = {
            FiltrosTopBar(
                onBack = onBack,
                onReset = onResetFilters
            )
        },
        bottomBar = {
            FiltrosBottomBar(
                onApply = { onApplyFilters(localFilters) }
            )
        }
    ) { paddingValues ->
        // Contenedor principal con Scroll vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F0F0)) // Fondo gris claro de la pantalla de filtros
                .verticalScroll(rememberScrollState()) // ACTIVA EL SCROLL
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // 1. Sección de Oficio/Categoría
                    OficioSection(
                        selectedProfession = localFilters.selectedProfession,
                        onProfessionSelected = { localFilters = localFilters.copy(selectedProfession = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Sección de Distancia Máxima
                    DistanciaMaximaSection(
                        maxDistanceKm = localFilters.maxDistanceKm,
                        onDistanceChange = { localFilters = localFilters.copy(maxDistanceKm = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Sección de Calificación Mínima
                    CalificacionMinimaSection(
                        minRating = localFilters.minRating,
                        onRatingSelected = { localFilters = localFilters.copy(minRating = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. Sección de Disponibilidad
                    DisponibilidadSection(
                        isAvailable = localFilters.isAvailable,
                        onToggle = { localFilters = localFilters.copy(isAvailable = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// --- Componentes de la pantalla ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosTopBar(onBack: () -> Unit, onReset: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                "Filtros Avanzados",
                fontWeight = FontWeight.Bold,
                color = DarkBlueText
            )
        },
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(16.dp)
            )
        },
        actions = {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "Reiniciar",
                tint = PrincipalAzul,
                modifier = Modifier
                    .clickable(onClick = onReset)
                    .padding(16.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun OficioSection(selectedProfession: String, onProfessionSelected: (String) -> Unit) {
    // Datos de ejemplo basados en la imagen
    val professions = listOf("Plomeria", "Electricidad", "Limpieza", "Jardineria", "Carpinteria", "Albañileria", "Pintura", "Express")

    Column {
        Text("Oficio", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = DarkBlueText)
        Spacer(modifier = Modifier.height(8.dp))

        professions.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { profession ->
                    ProfessionItem(
                        name = profession,
                        icon = Icons.Default.Search, // Usar un icono de ejemplo o uno específico
                        isSelected = selectedProfession == profession,
                        onClick = { onProfessionSelected(profession) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
    }
}

@Composable
fun ProfessionItem(name: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFF0F0F0) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = name, tint = if (isSelected) PrincipalAzul else Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(name, color = if (isSelected) PrincipalAzul else Color.Black)
    }
}

@Composable
fun DistanciaMaximaSection(maxDistanceKm: Float, onDistanceChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Distancia Máxima", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = DarkBlueText)
            Text("${maxDistanceKm.roundToInt()} km", fontWeight = FontWeight.Bold, color = PrincipalAzul)
        }

        Slider(
            value = maxDistanceKm,
            onValueChange = onDistanceChange,
            valueRange = 1f..20f, // Rango de 1 a 20 km
            steps = 19,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = PrincipalAzul,
                activeTrackColor = PrincipalAzul
            )
        )

        // Opciones de distancia Cerca/Medio/Amplio (usando Box para simular botones)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DistanceButton("Cerca (1-3 km)", isSelected = maxDistanceKm <= 3f) { onDistanceChange(3f) }
            DistanceButton("Medio (5-10 km)", isSelected = maxDistanceKm in 5f..10f) { onDistanceChange(10f) }
            DistanceButton("Amplio (10-20 km)", isSelected = maxDistanceKm > 10f) { onDistanceChange(20f) }
        }
        Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
    }
}

@Composable
fun DistanceButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) PrincipalAzul else Color(0xFFF0F0F0)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = 12.sp)
    }
}

@Composable
fun CalificacionMinimaSection(minRating: Int, onRatingSelected: (Int) -> Unit) {
    val ratings = listOf(1, 2, 3, 4, 5)

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text("Calificación mínima", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = DarkBlueText)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Rating buttons (1+, 2+, 3+, etc.)
            ratings.forEach { rating ->
                RatingButton(
                    rating = rating,
                    isSelected = minRating == rating,
                    onClick = { onRatingSelected(rating) }
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))
    }
}

@Composable
fun RatingButton(rating: Int, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) PrincipalAzul else Color(0xFFF0F0F0)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${rating}+ ⭐",
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun DisponibilidadSection(isAvailable: Boolean, onToggle: (Boolean) -> Unit) {
    Column {
        Text("Disponibilidad", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = DarkBlueText)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mostrar solo trabajadores disponibles ahora", color = Color.Black)
            Switch(
                checked = isAvailable,
                onCheckedChange = onToggle,
                colors = androidx.compose.material3.SwitchDefaults.colors(
                    checkedTrackColor = PrincipalAzul
                )
            )
        }
    }
}

@Composable
fun FiltrosBottomBar(onApply: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Button(
            onClick = onApply,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrincipalAzul),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Aplicar Filtros", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFiltrosAvanzadosScreen() {
    MaterialTheme {
        FiltrosAvanzadosScreen(
            currentFilters = FilterState(),
            onBack = {},
            onApplyFilters = {},
            onResetFilters = {}
        )
    }
}