package com.example.proto.ui.screen.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.R
// Importaciones de helpers y temas
import com.example.proxservices_app.ui.screen.client.CardBackground
import com.example.proxservices_app.ui.screen.client.CompletedBlue
import com.example.proxservices_app.ui.screen.client.CompletedGreen
import com.example.proxservices_app.ui.screen.client.DarkBlueStatus
import com.example.proxservices_app.ui.screen.client.DetailRow
import com.example.proxservices_app.ui.screen.client.ScreenBackground
import com.example.proxservices_app.ui.screen.client.ServiceTab
import com.example.proxservices_app.ui.screen.client.Servicio
import com.example.proxservices_app.ui.screen.client.StarYellow
import com.example.proxservices_app.ui.screen.client.TextBlack
import com.example.proxservices_app.ui.screen.client.TextGray
import com.example.proxservices_app.ui.screen.client.WarningYellow
import com.example.proxservices_app.ui.screen.client.WorkerAvatar
import com.example.proxservices_app.ui.theme.PrincipalAzul
// Importación del repositorio (asumiendo que está en el mismo paquete o es accesible)
import com.example.proxservices_app.ui.screen.client.ServiceRepository


// --- DATOS DE EJEMPLO UNIFICADOS ---
// ⚠️ ¡ELIMINAR ESTA SECCIÓN! Los datos deben estar en ServiceRepository.services
// private val initialServices = listOf(...)


// =========================================================================
//                  PANTALLA CONTENEDORA DE MIS SERVICIOS
// =========================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisServiciosScreen(
    onBack: () -> Unit = {},
    // Acciones recibidas desde ClientServicesScreen
    onCancelService: (Servicio) -> Unit,
    onReviewClick: (Servicio) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(ServiceTab.PENDIENTES) }

    // **CORRECCIÓN CLAVE 1: UNA SOLA FUENTE DE DATOS**
    // Usamos directamente la lista mutable del repositorio.
    val servicesState = ServiceRepository.services

    // Función que elimina el servicio y llama a la acción externa de cancelación
    val handleInternalCancel: (Servicio) -> Unit = { servicioToCancel ->
        // 1. Llama a la acción externa (ClientServicesScreen) para registrar/guardar el evento
        onCancelService(servicioToCancel)

        // 2. Actualiza la lista global (ServiceRepository.services)
        servicesState.remove(servicioToCancel)
    }

    // Función para obtener el conteo dinámico (usa servicesState)
    val getCount: (ServiceTab) -> Int = { tab ->
        servicesState.count { it.estado == tab }
    }

    val onTabSelected: (ServiceTab) -> Unit = { selectedTab = it }

    Scaffold(
        topBar = {
            // ... (TopAppBar sin cambios)
            TopAppBar(
                title = { Text("Mis Servicios", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextBlack,
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(horizontal = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBackground)
            )
        },
        containerColor = ScreenBackground
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Componente de Tabs
            ServiceTabs(selectedTab, onTabSelected, getCount)

            // Contenido de las Tarjetas
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val servicesToShow = servicesState.filter { it.estado == selectedTab }

                items(servicesToShow, key = { servicio -> servicio.id }) { servicio ->
                    when (selectedTab) {
                        ServiceTab.PENDIENTES -> ServicioPendienteCard(
                            servicio,
                            // Se pasa la función de cancelación
                            onCancel = { handleInternalCancel(servicio) }
                        )
                        ServiceTab.EN_PROGRESO -> ServicioProgresoCard(servicio)
                        ServiceTab.COMPLETADOS -> ServicioCompletadoCard(
                            servicio,
                            // Se pasa la función de reseña
                            onReviewClick = { onReviewClick(servicio) }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
//                         COMPONENTE DE TABS SUPERIOR (Sin Cambios)
// -------------------------------------------------------------------------
// ... (ServiceTabs y ServiceTabItem sin cambios)

@Composable
fun ServiceTabs(
    selectedTab: ServiceTab,
    onTabSelected: (ServiceTab) -> Unit,
    getCount: (ServiceTab) -> Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ServiceTabItem(ServiceTab.PENDIENTES, selectedTab, onTabSelected, getCount)
        ServiceTabItem(ServiceTab.EN_PROGRESO, selectedTab, onTabSelected, getCount)
        ServiceTabItem(ServiceTab.COMPLETADOS, selectedTab, onTabSelected, getCount)
    }
}

@Composable
fun RowScope.ServiceTabItem(
    tab: ServiceTab,
    selectedTab: ServiceTab,
    onTabSelected: (ServiceTab) -> Unit,
    getCount: (ServiceTab) -> Int
) {
    val isSelected = tab == selectedTab
    val tabName = when (tab) {
        ServiceTab.PENDIENTES -> "Pendientes"
        ServiceTab.EN_PROGRESO -> "En Progreso"
        ServiceTab.COMPLETADOS -> "Completados"
    }

    TextButton(
        onClick = { onTabSelected(tab) },
        modifier = Modifier
            .weight(1f)
            .height(40.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (isSelected) PrincipalAzul else Color.Transparent,
            contentColor = if (isSelected) CardBackground else TextBlack.copy(alpha = 0.8f)
        ),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        Text(
            text = "$tabName (${getCount(tab)})",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}


// -------------------------------------------------------------------------
//                      TARJETA PENDIENTES (Sin Cambios de lógica)
// -------------------------------------------------------------------------
// ... (ServicioPendienteCard, ServicioProgresoCard, ServicioCompletadoCard son correctos)
// ...
// ... (Despues de la definicion de ServiceTabItem, añade esto)

// -------------------------------------------------------------------------
//                      TARJETA PENDIENTES
// -------------------------------------------------------------------------

@Composable
fun ServicioPendienteCard(servicio: Servicio, onCancel: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Contenido del card)
            Row(verticalAlignment = Alignment.CenterVertically) {
                WorkerAvatar(servicio.avatarColor, isVerified = true)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(servicio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                    Text(servicio.oficio, fontSize = 14.sp, color = TextGray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarningYellow)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(servicio.estado.name, color = TextBlack, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(R.drawable.ic_limpieza, servicio.direccion)
            DetailRow(R.drawable.ic_limpieza, servicio.identificador)
            DetailRow(R.drawable.ic_limpieza, servicio.fechaHora)
            servicio.tiempoEstimado?.let { DetailRow(R.drawable.ic_limpieza, "Tiempo estimado: $it") }
            DetailRow(R.drawable.ic_limpieza, "Precio estimado: ${servicio.precioEstimado}")


            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar solicitud", color = PrincipalAzul, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { /* Ver detalles */ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver detalles", color = CardBackground, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
//                      TARJETA EN PROGRESO
// -------------------------------------------------------------------------

@Composable
fun ServicioProgresoCard(servicio: Servicio) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Contenido del card)
            Row(verticalAlignment = Alignment.Top) {
                WorkerAvatar(servicio.avatarColor, isVerified = true)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(servicio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                    Text(servicio.oficio, fontSize = 14.sp, color = TextGray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBlueStatus)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(servicio.estado.name.replace("_", " "), color = CardBackground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Detalles
            DetailRow(R.drawable.ic_limpieza, servicio.direccion)
            DetailRow(R.drawable.ic_limpieza, servicio.identificador)
            DetailRow(R.drawable.ic_limpieza, servicio.fechaHora)

            servicio.horaInicio?.let {
                DetailRow(R.drawable.ic_limpieza, "Iniciado a las $it", color = PrincipalAzul, fontWeight = FontWeight.SemiBold)
            }
            DetailRow(R.drawable.ic_limpieza, "Precio estimado: ${servicio.precioEstimado}")

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { /* Seguimiento */ },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Seguimiento", color = PrincipalAzul, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { /* Chatear */ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Chatear", color = CardBackground, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
//                      TARJETA COMPLETADOS
// -------------------------------------------------------------------------

@Composable
fun ServicioCompletadoCard(servicio: Servicio, onReviewClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ... (Contenido del card)
            Row(verticalAlignment = Alignment.Top) {
                WorkerAvatar(servicio.avatarColor, isVerified = true)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(servicio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                    Text(servicio.oficio, fontSize = 14.sp, color = TextGray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CompletedGreen)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(servicio.estado.name, color = CardBackground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Detalles
            DetailRow(R.drawable.ic_limpieza, servicio.direccion)
            DetailRow(R.drawable.ic_limpieza, servicio.identificador)
            DetailRow(R.drawable.ic_limpieza, servicio.fechaHora)

            servicio.tiempoCompletadoHoras?.let {
                DetailRow(R.drawable.ic_limpieza, "Completado en $it horas", color = CompletedBlue, fontWeight = FontWeight.SemiBold)
            }

            DetailRow(R.drawable.ic_limpieza, "Precio estimado: ${servicio.precioEstimado}")


            Spacer(modifier = Modifier.height(16.dp))

            // Botones o Reseña
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { /* Ver recibo */ },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver recibo", color = PrincipalAzul, fontWeight = FontWeight.SemiBold)
                }

                if (servicio.rating == null) {
                    Button(
                        onClick = onReviewClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrincipalAzul),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dejar Reseña", color = CardBackground, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Row(
                        modifier = Modifier.weight(1f).height(50.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            Icon(
                                painter = painterResource(id = R.drawable.ic_limpieza), // Placeholder de estrella
                                contentDescription = null,
                                tint = if (index < servicio.rating) StarYellow else TextGray.copy(alpha = 0.5f),
                                modifier = Modifier.size(24.dp).padding(horizontal = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}