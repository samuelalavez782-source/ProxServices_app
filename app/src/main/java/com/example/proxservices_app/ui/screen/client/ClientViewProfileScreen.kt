package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.proxservices_app.R

// -----------------------------------------------------------------------------
// 1. DATA CLASS PARA LOS TRABAJADORES
// -----------------------------------------------------------------------------
data class WorkerProfile(
    val id: String,
    val name: String,
    val title: String,
    val avatar: Int,
    val banner: Int,
    val rating: Double,
    val services: List<String>,
    val portfolio: List<Int>,
    val lat: Double,
    val lng: Double
)

// -----------------------------------------------------------------------------
// 2. LISTA DE TRABAJADORES
// -----------------------------------------------------------------------------
val workerList = listOf(
    WorkerProfile(
        id = "carlos_01",
        name = "Carlos Herrera",
        title = "Electricista Certificado",
        avatar = R.drawable.logo_carloss,
        banner = R.drawable.logo_servicioele,
        rating = 4.7,
        services = listOf(
            "Instalaciones eléctricas",
            "Reparación de cortos",
            "Cambio de lámparas",
            "Mantenimiento general"
        ),
        portfolio = listOf(
            R.drawable.logo_servicioeleuno,
            R.drawable.logo_servicioeleuno,
            R.drawable.logo_servicioele,
        ),
        lat = 19.2500,
        lng = -99.1500
    ),

    WorkerProfile(
        id = "maria_01",
        name = "María González",
        title = "Especialista en Limpieza del Hogar",
        avatar = R.drawable.logo_maria,
        banner = R.drawable.logo_limpiezaafondo,
        rating = 4.9,
        services = listOf(
            "Limpieza profunda",
            "Limpieza de baños y cocina",
            "Limpieza de ventanas",
            "Organización de hogar"
        ),
        portfolio = listOf(
            R.drawable.logo_limpieza,
            R.drawable.logo_limpiezatwo,
            R.drawable.logo_limpiezatree
        ),
        lat = 19.2450,
        lng = -99.1420
    ),

    WorkerProfile(
        id = "ana",
        name = "Ana González",
        title = "Jardinera Profesional",
        avatar = R.drawable.logo_ana,
        banner = R.drawable.logo_jardineria,   // <-- ESTE SÍ EXISTE
        rating = 4.8,
        services = listOf(
            "Poda de árboles",
            "Corte y mantenimiento de césped",
            "Instalación de plantas",
            "Diseño de jardines"
        ),
        portfolio = listOf(
            R.drawable.logo_mone,
            R.drawable.logo_mtwo,
            R.drawable.logo_mtree
        ),
        lat = 19.2600,
        lng = -99.1340
    )
)

// -----------------------------------------------------------------------------
// 3. NAVHOST — LISTA + DETALLE
// -----------------------------------------------------------------------------
@Composable
fun ProfileNavHost() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "worker_list") {

        composable("worker_list") {

            WorkerListScreen(
                workers = workerList,
                onWorkerClick = { workerId ->
                    navController.navigate("profile/$workerId")
                }
            )
        }

        composable(
            route = "profile/{workerId}",
            arguments = listOf(navArgument("workerId") { type = NavType.StringType })
        ) { entry ->

            val workerId = entry.arguments?.getString("workerId") ?: ""

            val profile = workerList.find { it.id == workerId } ?: workerList.first()

            ClientViewProfileScreen(
                profile = profile,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 4. LISTA DE TRABAJADORES
// -----------------------------------------------------------------------------
@Composable
fun WorkerListScreen(workers: List<WorkerProfile>, onWorkerClick: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Trabajadores disponibles",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF002B5B)
        )

        Spacer(Modifier.height(16.dp))

        workers.forEach { w ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onWorkerClick(w.id) }
                    .padding(vertical = 8.dp)
                    .shadow(6.dp, RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = w.avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(w.name, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(w.title, fontSize = 14.sp, color = Color.Gray)
                }

                Text("${w.rating}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 5. PANTALLA DE PERFIL COMPLETA — CON FLECHA DE REGRESAR FUNCIONAL
// -----------------------------------------------------------------------------
@Composable
fun ClientViewProfileScreen(profile: WorkerProfile, onBack: () -> Unit) {

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scroll)
    ) {

        // ---------------- HEADER ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(Color.White, RoundedCornerShape(25.dp))
                .padding(vertical = 15.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // ✅ FLECHA QUE SÍ FUNCIONA
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = Color(0xFF002B5B),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )

            Text(
                text = "Detalles del profesional",
                color = Color(0xFF002B5B),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF002B5B),
                modifier = Modifier.size(26.dp)
            )
        }

        // ---------------- CARD PRINCIPAL ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {

            Image(
                painter = painterResource(id = profile.banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {

                Image(
                    painter = painterResource(id = profile.avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC107)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(10.dp))

                Text(profile.name, fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text(profile.title, fontSize = 14.sp, color = Color.White)

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐ ⭐ ⭐ ⭐ ☆", color = Color(0xFFFFD700))
                    Spacer(Modifier.width(6.dp))
                    Text("${profile.rating}", color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        // ---------------- INFORMACIÓN RÁPIDA ----------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {

            Text("Información Rápida", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002B5B))

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💲", fontSize = 30.sp)
                    Text("Desde", color = Color.Gray)
                    Text("$350 MXN", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C7C7))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⏱️", fontSize = 30.sp)
                    Text("Tiempo", color = Color.Gray)
                    Text("45–60 min", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C7C7))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📍", fontSize = 30.sp)
                    Text("Distancia", color = Color.Gray)
                    Text("1.2 km", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C7C7))
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        // ---------------- SERVICIOS ----------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(10.dp, RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {

            Text("Especialidades / Servicios", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002B5B))
            Spacer(Modifier.height(12.dp))

            profile.services.forEach {
                ServiceItem(it)
            }
        }

        Spacer(Modifier.height(25.dp))

        // ---------------- PORTAFOLIO ----------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(10.dp, RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {

            Text("Portafolio", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002B5B))

            Spacer(Modifier.height(10.dp))

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                profile.portfolio.forEach { imgRes ->
                    PortfolioItem(imgRes)
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        // ---------------- MAPA ----------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .shadow(10.dp, RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {

            Text("Ubicación", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002B5B))

            Spacer(Modifier.height(10.dp))

            val workerLocation = LatLng(profile.lat, profile.lng)

            val cameraPositionState = rememberCameraPositionState {
                position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(workerLocation, 15f)
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(15.dp)),
                cameraPositionState = cameraPositionState
            ) {

                Marker(
                    state = MarkerState(position = workerLocation),
                    title = "Ubicación de ${profile.name}"
                )
            }

            Spacer(Modifier.height(10.dp))

            Text("A 1.2 km de tu ubicación", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        Spacer(Modifier.height(40.dp))
    }
}

// -----------------------------------------------------------------------------
// 6. COMPONENTES
// -----------------------------------------------------------------------------
@Composable
fun ServiceItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
        Text("✔️", fontSize = 20.sp, color = Color(0xFF00C7C7))
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 16.sp)
    }
}

@Composable
fun PortfolioItem(imageRes: Int) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(110.dp)
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop
    )
}
