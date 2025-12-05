package com.example.proxservices_app.ui.screen.worker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.proxservices_app.R

// --- IMPORTACIONES ASUMIDAS (Asegúrate que estas rutas son correctas) ---
// Estos elementos deben existir en algún lugar de tu proyecto.
import com.example.proxservices_app.ui.theme.PrincipalAzul

// Si tus colores están en ui.screen.client:
import com.example.proxservices_app.ui.screen.client.CardBackground
import com.example.proxservices_app.ui.screen.client.TextBlack
import com.example.proxservices_app.ui.screen.client.TextGray
import com.example.proxservices_app.ui.screen.client.WarningYellow
import com.example.proxservices_app.ui.screen.client.DarkBlueStatus
import com.example.proxservices_app.ui.screen.client.CompletedGreen
import com.example.proxservices_app.ui.screen.client.StarYellow

// Si tus helpers están en ui.screen.client:
import com.example.proxservices_app.ui.screen.client.WorkerAvatar
import com.example.proxservices_app.ui.screen.client.DetailRow


// --- 1. MODELOS DE DATOS ESENCIALES (Aseguramos que no haya conflicto con el cliente) ---

// Este enum define las pestañas
enum class ServiceTab {
    PENDIENTES, EN_PROGRESO, COMPLETADOS
}

// Este data class define el ítem de trabajo
data class JobItem(
    val id: Int,
    val clientName: String,
    val serviceType: String,
    val address: String,
    val identifier: String,
    val dateTime: String,
    val estimatedTime: String?,
    val price: String,
    val status: ServiceTab,
    val avatarColor: Color,
    val rating: Int? = null,
    val timeCompleted: String? = null
)

// --- DATOS SIMULADOS (MOCK DATA) ---
// Usamos remember para mantener la lista y evitar la advertencia "is never used" en el parámetro navController
private val mockJobs = mutableStateListOf(
    JobItem(
        id = 1, clientName = "Carlos Herrera", serviceType = "Plomero Certificado",
        address = "Av. Principal 123", identifier = "#SP-2024-001", dateTime = "15 Nov, 2024 • 10:00 AM",
        estimatedTime = "2-3 horas", price = "A definir", status = ServiceTab.PENDIENTES, avatarColor = Color(0xFF9CCC65)
    ),
    JobItem(
        id = 2, clientName = "María González", serviceType = "Electricista Residencial",
        address = "Calle Olivos 50", identifier = "#SP-2024-002", dateTime = "15 Nov, 2024 • 10:00 AM",
        estimatedTime = "1 hora", price = "$350", status = ServiceTab.EN_PROGRESO, avatarColor = Color(0xFFFFB74D)
    ),
    JobItem(
        id = 3, clientName = "Ana García", serviceType = "Carpintero General",
        address = "Av. Siempre Viva", identifier = "#SP-2024-003", dateTime = "14 Nov, 2024 • 14:00 PM",
        estimatedTime = null, price = "$500", status = ServiceTab.COMPLETADOS, avatarColor = Color(0xFF4DB6AC),
        timeCompleted = "1.5", rating = 4
    )
)


// =========================================================================
//                  PANTALLA PRINCIPAL: MIS TRABAJOS
// =========================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerJobsScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(ServiceTab.PENDIENTES) }
    val servicesState = remember { mockJobs }

    val getCount: (ServiceTab) -> Int = { tab ->
        servicesState.count { it.status == tab }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Trabajos", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBackground)
            )
        },
        containerColor = Color(0xFFF4F6F6) // Reemplaza ScreenBackground por Color directo si da conflicto
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            ServiceTabs(selectedTab, { selectedTab = it }, getCount)

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val jobsToShow = servicesState.filter { it.status == selectedTab }

                items(jobsToShow, key = { job -> job.id }) { job ->
                    when (selectedTab) {
                        ServiceTab.PENDIENTES -> WorkerJobPendienteCard(job)
                        ServiceTab.EN_PROGRESO -> WorkerJobProgresoCard(job)
                        ServiceTab.COMPLETADOS -> WorkerJobCompletadoCard(job)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
//                         COMPONENTE DE TABS SUPERIOR
// -------------------------------------------------------------------------

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
//                      TARJETA TRABAJADOR: PENDIENTES
// -------------------------------------------------------------------------

@Composable
fun WorkerJobPendienteCard(job: JobItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                WorkerAvatar(job.avatarColor, isVerified = true)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(job.clientName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                    Text(job.serviceType, fontSize = 14.sp, color = TextGray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarningYellow)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(job.status.name, color = TextBlack, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(R.drawable.ic_limpieza, job.address)
            DetailRow(R.drawable.ic_limpieza, job.identifier)
            DetailRow(R.drawable.ic_limpieza, job.dateTime)
            job.estimatedTime?.let { DetailRow(R.drawable.ic_limpieza, "Tiempo estimado: $it") }
            DetailRow(R.drawable.ic_limpieza, "Precio estimado: ${job.price}")


            Spacer(modifier = Modifier.height(16.dp))

            // Botones: Rechazar y Aceptar
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { /* Rechazar Solicitud */ },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Rechazar", color = PrincipalAzul, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { /* Aceptar Solicitud */ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aceptar", color = CardBackground, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
//                      TARJETA TRABAJADOR: EN PROGRESO
// -------------------------------------------------------------------------

@Composable
fun WorkerJobProgresoCard(job: JobItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                WorkerAvatar(job.avatarColor, isVerified = true)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(job.clientName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                    Text(job.serviceType, fontSize = 14.sp, color = TextGray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBlueStatus)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("EN PROGRESO", color = CardBackground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(R.drawable.ic_limpieza, job.address)
            DetailRow(R.drawable.ic_limpieza, job.identifier)
            DetailRow(R.drawable.ic_limpieza, job.dateTime)

            DetailRow(R.drawable.ic_limpieza, "Precio estimado: ${job.price}")

            Spacer(modifier = Modifier.height(16.dp))

            // Botones: Chat y Completar Trabajo
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { /* Chatear con cliente */ },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PrincipalAzul),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Chatear", color = PrincipalAzul, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { /* Marcar como Completado */ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CompletedGreen),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Completar Trabajo", color = CardBackground, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
//                      TARJETA TRABAJADOR: COMPLETADOS
// -------------------------------------------------------------------------

@Composable
fun WorkerJobCompletadoCard(job: JobItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                WorkerAvatar(job.avatarColor, isVerified = true)
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(job.clientName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
                    Text(job.serviceType, fontSize = 14.sp, color = TextGray)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CompletedGreen)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(job.status.name, color = CardBackground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(R.drawable.ic_limpieza, job.address)
            DetailRow(R.drawable.ic_limpieza, job.identifier)
            DetailRow(R.drawable.ic_limpieza, job.dateTime)

            job.timeCompleted?.let {
                DetailRow(R.drawable.ic_limpieza, "Completado en $it horas", color = CompletedGreen, fontWeight = FontWeight.SemiBold)
            }

            DetailRow(R.drawable.ic_limpieza, "Precio final: ${job.price}")


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

                Row(
                    modifier = Modifier.weight(1f).height(50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Simulación de estrellas para el rating
                    repeat(5) { index ->
                        Icon(
                            painter = painterResource(id = R.drawable.ic_limpieza), // Placeholder de estrella
                            contentDescription = null,
                            tint = if (job.rating != null && index < job.rating) StarYellow else TextGray.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp).padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerJobsScreenPreview() {
    WorkerJobsScreen(navController = rememberNavController())
}