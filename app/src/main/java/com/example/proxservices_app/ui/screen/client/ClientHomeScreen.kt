package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.proxservices_app.R

// IMPORTS DEL MAPA
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

import kotlinx.coroutines.launch

// Importa los colores del tema (usa los que ya definiste en ui.theme)
import com.example.proxservices_app.ui.theme.PrincipalAzul
import com.example.proxservices_app.ui.theme.AzulClaroFondo
import com.example.proxservices_app.ui.theme.Blanco
import com.example.proxservices_app.ui.theme.GrisFondo
import com.example.proxservices_app.ui.theme.GrisTextoSecundario

/* ---------------- DATA CLASSES ---------------- */
/**
 * Cambié Worker para incluir el id (esto evita el crash cuando navegas al perfil).
 * Ahora onNavigateToWorkerView debe recibir el id (ej: "carlos_01").
 */
data class Categorry(val title: String, val drawable: Int, val isHighlighted: Boolean = false)
data class Worker(val id: String, val initials: String, val name: String, val service: String)

// sampleCategories: igual que antes
private val sampleCategories = listOf(
    Categorry("Limpieza", R.drawable.logo_limpieza, isHighlighted = true),
    Categorry("Plomería", R.drawable.logo_plomeria),
    Categorry("Jardinería", R.drawable.logo_jardineria),
    Categorry("Electricidad", R.drawable.logo_electricidad),
    Categorry("Carpintería", R.drawable.logo_carpinteria),
    Categorry("Pintura", R.drawable.logo_pintura)
)

// sampleWorkers ahora incluye el id adecuado que coincide con los WorkerProfile.id del otro archivo
private val sampleWorkers = listOf(
    Worker("maria_01", "MG", "María González", "Limpieza del Hogar"),
    Worker("carlos_01", "CM", "Carlos Mendoza", "Plomería"),
    Worker("ana", "AR", "Ana Rodríguez", "Jardinería")
)

/* ---------------- SHAPE PARA EL HEADER CON CURVA ---------------- */
class CurvedHeaderShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.78f)
            cubicTo(
                size.width * 0.75f, size.height * 1.05f,
                size.width * 0.25f, size.height * 0.95f,
                0f, size.height * 0.85f
            )
            close()
        }
        return Outline.Generic(path)
    }
}

/* ---------------- PANTALLA PRINCIPAL ---------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(onNavigateToWorkerView: (String) -> Unit, onSearchClick: () -> Unit) {

    // alturas constantes para coordinar header y contenido
    val headerHeight = 250.dp
    val overlayPaddingTop = 150.dp // conserva el comportamiento visual que tenías

    val initialLocation = LatLng(19.5960, -99.3080)
    val userLocation = LatLng(19.5950, -99.3090)

    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 14f)
    }

    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = GrisFondo) {

        Box(modifier = Modifier.fillMaxSize()) {

            /* ---------------- HEADER (fijo, con zIndex para que no se "pierda" al deslizar) ---------------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .clip(CurvedHeaderShape())
                    .background(PrincipalAzul)
                    .align(Alignment.TopCenter)
                    .zIndex(1f) // asegurar que quede encima del LazyColumn
                    .padding(bottom = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Blanco, modifier = Modifier.size(30.dp))

                    Image(
                        painter = painterResource(id = R.drawable.logo_usuario),
                        contentDescription = "Perfil",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .border(2.dp, Blanco, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(start = 20.dp, top = 95.dp)) {
                    Text("Hola de nuevo", color = Blanco.copy(alpha = 0.85f), fontSize = 18.sp)
                    Text("Maria", color = Blanco, fontSize = 33.sp, fontWeight = FontWeight.ExtraBold)
                }
            }

            /* ---------------- CONTENIDO SCROLL ---------------- */
            // ponemos el LazyColumn **debajo** del header visualmente (zIndex menor),
            // y le damos paddingTop igual a overlayPaddingTop para que no tape el header.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = overlayPaddingTop),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {

                /* BUSCADOR (tarjeta blanca que se "superpone" sobre el header por diseño) */
                item {
                    Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .offset(y = (-32).dp) // ligera superposición hacia arriba para efecto "pegado" al header
                                .clickable { onSearchClick() },
                            shape = RoundedCornerShape(45.dp),
                            elevation = CardDefaults.cardElevation(15.dp),
                            colors = CardDefaults.cardColors(containerColor = Blanco)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = GrisTextoSecundario)
                                Spacer(Modifier.width(12.dp))
                                Text("Buscar trabajadores o...", color = GrisTextoSecundario, fontSize = 16.sp)
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(8.dp)) } // ajuste para que no se vea raro al deslizar

                /* MAPA */
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Blanco)
                    ) {
                        Box(Modifier.height(240.dp)) {

                            GoogleMap(
                                modifier = Modifier.fillMaxSize().pointerInput(Unit) {},
                                cameraPositionState = camera,
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = true,
                                    scrollGesturesEnabled = true,
                                    zoomGesturesEnabled = true,
                                    rotationGesturesEnabled = true,
                                    tiltGesturesEnabled = true
                                )
                            ) {
                                Marker(
                                    state = rememberMarkerState(position = initialLocation)
                                )
                            }

                            Card(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 12.dp, bottom = 12.dp)
                                    .fillMaxWidth(0.8f)
                                    .height(45.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Blanco.copy(alpha = 0.9f)),
                                elevation = CardDefaults.cardElevation(3.dp)
                            ) {
                                Box(
                                    Modifier.fillMaxSize().padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text("Tu ubicación actual...", color = GrisTextoSecundario, fontSize = 14.sp)
                                }
                            }

                            LocationFloatingButton(
                                onClick = {
                                    scope.launch {
                                        camera.animate(
                                            CameraUpdateFactory.newCameraPosition(
                                                CameraPosition.fromLatLngZoom(userLocation, 16f)
                                            ),
                                            1000
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 12.dp, bottom = 12.dp)
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(20.dp)) }

                /* CATEGORÍAS */
                item {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                        Text("Categorías Destacadas", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        Spacer(Modifier.height(12.dp))
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .heightIn(max = 450.dp)
                            .padding(horizontal = 20.dp),
                        userScrollEnabled = false,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sampleCategories) { CategoryCard(it) }
                    }
                }

                item { Spacer(Modifier.height(20.dp)) }

                /* SERVICIOS CERCANOS */
                item {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                        Text("Servicios cercanos a ti", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        Spacer(Modifier.height(12.dp))
                    }
                }

                // aqui PASAMOS EL ID (no el nombre) para que el NavHost busque por id y no falle
                items(sampleWorkers.size) { idx ->
                    WorkerCard(
                        sampleWorkers[idx],
                        onViewProfileClick = { workerId ->
                            onNavigateToWorkerView(workerId) // ahora pasa el id
                        }
                    )
                }

                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

/* ---------------- COMPONENTES ---------------- */

@Composable
private fun LocationFloatingButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(PrincipalAzul)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Blanco, modifier = Modifier.size(28.dp))
    }
}

@Composable
private fun CategoryCard(cat: Categorry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .border(
                width = if (cat.isHighlighted) 2.dp else 0.dp,
                color = if (cat.isHighlighted) PrincipalAzul else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            // usa fondo suave del tema para la tarjeta destacada
            containerColor = if (cat.isHighlighted) AzulClaroFondo else Blanco
        ),
        elevation = CardDefaults.cardElevation(if (cat.isHighlighted) 5.dp else 2.dp)
    ) {
        // Alineamos todo al centro para que la imagen quede en medio del recuadro
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (cat.isHighlighted) Blanco else AzulClaroFondo),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = cat.drawable),
                    contentDescription = null,
                    modifier = Modifier.size(42.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(cat.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Disponible", color = GrisTextoSecundario, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun WorkerCard(worker: Worker, onViewProfileClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(PrincipalAzul),
                contentAlignment = Alignment.Center
            ) {
                Text(worker.initials, color = Blanco, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }

            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(worker.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(worker.service, fontSize = 14.sp, color = GrisTextoSecundario)
            }

            Text(
                "Ver Perfil",
                color = PrincipalAzul,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onViewProfileClick(worker.id) } // PASAMOS EL ID
            )
        }
    }
}
