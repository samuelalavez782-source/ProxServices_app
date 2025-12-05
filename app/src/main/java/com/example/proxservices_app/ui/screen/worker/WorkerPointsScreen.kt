package com.example.proxservices_app.ui.screen.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // Importación clave para el scroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
// Importamos tus colores de tema
import com.example.proxservices_app.ui.theme.PrincipalAzul
import com.example.proxservices_app.ui.theme.textFiel

// Definimos el color amarillo de tu tema (lo usamos para los iconos de historial)
val AmarilloNotificacion = Color(0xFFFFC107)

// --- MODELOS DE DATOS ---

data class RedeemableCoupon(
    val name: String,
    val description: String,
    val cost: Int,
    val iconId: Int
)

data class PointTransaction(
    val description: String,
    val date: String,
    val amount: Int
)

// --- DATOS SIMULADOS ---
object WorkerPointsData {
    val coupons = listOf(
        RedeemableCoupon("Descuento en Ferretería", "25% de descuento en herramientas.", 500, 0),
        RedeemableCoupon("Cupón de Gasolina", "100 pesos en combustible.", 500, 0),
        RedeemableCoupon("Descuento en Supermercado", "8% en compras de alimentos.", 500, 0),
        RedeemableCoupon("Bonificación en Ropa", "$200 pesos en tienda de ropa.", 500, 0),
        RedeemableCoupon("Descuento en Restaurante", "20% en comida rápida.", 500, 0),
        RedeemableCoupon("Recarga Telefónica", "$50 pesos de saldo.", 500, 0),
    )

    val history = listOf(
        PointTransaction("Puntos obtenidos por trabajo", "Hoy 14:30", 200),
        PointTransaction("Puntos canjeados", "Ayer 16:45", -500),
        PointTransaction("Bonificación por reseña", "Hace 2 días", 50),
        PointTransaction("Puntos obtenidos por trabajo", "Hace 5 días", 300),
        PointTransaction("Puntos obtenidos por trabajo", "Hace 6 días", 150),
        PointTransaction("Puntos canjeados", "Hace 7 días", -100),
    )
}

// --- FUNCIÓN PRINCIPAL (USANDO LAZYCOLUMN) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerPointsScreen(
    navController: NavHostController,
    onNavigateToRedemption: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Puntos y Recompensas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        // USAMOS LAZYCOLUMN PARA PERMITIR EL SCROLL Y MOSTRAR TODO EL CONTENIDO
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Tarjeta principal de Puntos Disponibles
            item {
                PointsAvailableCard(
                    points = "1,250",
                    onRedeemClicked = onNavigateToRedemption
                )
            }

            // 2. Sección de Canjes Disponibles
            item {
                Text(
                    "Canjes Disponibles",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Grid de Canjes
            item {
                RedemptionGrid(coupons = WorkerPointsData.coupons)
            }

            // 3. Sección de Historial de Puntos
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Historial de puntos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista del Historial
            item {
                PointsHistoryList(history = WorkerPointsData.history)
            }

            // Espacio final
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}


// --- Componente: Tarjeta principal de Puntos Disponibles ---

@Composable
fun PointsAvailableCard(points: String, onRedeemClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PrincipalAzul, PrincipalAzul.copy(alpha = 0.8f))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Puntos Disponibles",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$points pts",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRedeemClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = textFiel),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Canjear Puntos", color = Color.White)
                }
            }
        }
    }
}


// --- Componente: Grid de Canjes Disponibles (2 columnas) ---

@Composable
fun RedemptionGrid(coupons: List<RedeemableCoupon>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Divide la lista en filas de 2 elementos
        coupons.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { coupon ->
                    RedemptionItemCard(coupon = coupon, modifier = Modifier.weight(1f))
                }
                // Si la fila tiene un solo elemento, añade un Spacer para mantener la alineación
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun RedemptionItemCard(coupon: RedeemableCoupon, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono del cupón (SIMULADO)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(PrincipalAzul.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🏷️", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(coupon.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Black)
            Text("${coupon.cost} pts", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { /* Navegar a la pantalla de Canje de ese item */ },
                colors = ButtonDefaults.buttonColors(containerColor = textFiel),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text("Canjear", fontSize = 10.sp, color = Color.White)
            }
        }
    }
}

// --- Componente: Historial de Puntos (Lista) ---

@Composable
fun PointsHistoryList(history: List<PointTransaction>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp)
    ) {
        history.forEach { transaction ->
            PointTransactionRow(transaction = transaction)
            if (transaction != history.last()) {
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun PointTransactionRow(transaction: PointTransaction) {
    val isGain = transaction.amount > 0
    val amountText = if (isGain) "+${transaction.amount} pts" else "${transaction.amount} pts"
    val amountColor = if (isGain) textFiel else Color.Red

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Ícono (Estrella)
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(AmarilloNotificacion.copy(alpha = 0.8f), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("⭐", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.description, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text(transaction.date, fontSize = 12.sp, color = Color.Gray)
        }

        Text(
            text = amountText,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WorkerPointsScreenPreview() {
    WorkerPointsScreen(
        navController = rememberNavController(),
        onNavigateToRedemption = { /* */ }
    )
}