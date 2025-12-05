// WorkerPointRedemptionScreen.kt

package com.example.proxservices_app.ui.screen.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.proxservices_app.R
import com.example.proxservices_app.ui.theme.PrincipalAzul // Asumiendo que usas este color
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.proxservices_app.ui.theme.textFiel

// ...

// --- MODELOS DE DATOS ---

enum class RedemptionCategory(val title: String) {
    TOOLS("Herramientas"),
    PROTECTION("Equipo de protección"),
    HARDWARE("Ferretería")
}

data class RedeemableItem(
    val id: Int,
    val name: String,
    val description: String,
    val cost: Int,
    val category: RedemptionCategory,
    val iconId: Int, // Para el icono de cada producto
    val iconColor: Color = Color(0xFFFFC107) // Amarillo por defecto
)

// --- DATOS SIMULADOS ---
object RedemptionData {
    val items = listOf(
        RedeemableItem(1, "Juego de Desarmadores Pro", "Útil herramienta básica para trabajos bien realizados.", 200, RedemptionCategory.TOOLS, R.drawable.ic_screwdriver),
        RedeemableItem(2, "Martillo Profesional", "Martillo de acero forjado para uso rudo.", 500, RedemptionCategory.TOOLS, R.drawable.ic_hammer),
        RedeemableItem(3, "Guantes de Seguridad", "Guantes industriales para manos en trabajos pesados.", 200, RedemptionCategory.PROTECTION, R.drawable.ic_gloves),
        RedeemableItem(4, "Casco Protector", "Casco resistente con certificación industrial.", 500, RedemptionCategory.PROTECTION, R.drawable.ic_helmet),
        RedeemableItem(5, "Tornillos Mixtos (Caja)", "Caja de tornillos de diferentes medidas y tipos.", 150, RedemptionCategory.HARDWARE, R.drawable.ic_screw_box)
    )

    // Función de ayuda (simulación)
    fun getItemsByCategory(category: RedemptionCategory): List<RedeemableItem> {
        return items.filter { it.category == category }
    }
}
// --- FIN MODELOS DE DATOS ---


// --- FUNCIÓN PRINCIPAL DE LA PANTALLA ---

@Composable
fun WorkerPointRedemptionScreen(navController: NavHostController) {
    // Estado para saber qué pestaña está seleccionada
    var selectedCategory by remember { mutableStateOf(RedemptionCategory.TOOLS) }
    val categories = RedemptionCategory.entries.toTypedArray()

    Scaffold(
        topBar = { RedemptionTopBar(navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)), // Fondo gris claro
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. Tarjeta de Puntos Disponibles y Título (Parte superior)
            item {
                Column {
                    // Título principal
                    Text(
                        text = "Canjear puntos",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Tarjeta de Puntos (usa el mismo diseño que en WorkerPointsScreen)
                    RedemptionPointsCard(points = "1,250")

                    // Título "Tiendas Colaboradoras"
                    Text(
                        text = "Tiendas Colaboradoras",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = Color.Black
                    )
                    Text(
                        text = "Canjea tus puntos por estas herramientas y productos para tu trabajo.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 12.dp),
                        color = Color.Gray
                    )
                }
            }

            // 2. Barra de Pestañas (Sticky Header para simular tu diseño)
            // Aunque ScrollableTabRow no es Sticky por defecto en LazyColumn,
            // lo pondremos como un ítem para mantener el flujo de Scroll
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = Color.Black
                    )
                    RedemptionCategoryTabRow(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onTabSelected = { selectedCategory = it }
                    )
                    Text(
                        text = "Canjes Disponibles",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = Color.Black
                    )
                }
            }

            // 3. Contenido Dinámico de la Pestaña
            items(RedemptionData.getItemsByCategory(selectedCategory)) { item ->
                RedeemableItemCard(item = item, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }
        }
    }
}

// --- Componente: Top Bar ---
@Composable
fun RedemptionTopBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black
            )
        }
        // El título principal se maneja en el LazyColumn para que se vea más grande y "Canjear Puntos" se mueva
    }
}


// --- Componente: Barra de Pestañas de Categoría ---
@Composable
fun RedemptionCategoryTabRow(
    categories: Array<RedemptionCategory>,
    selectedCategory: RedemptionCategory,
    onTabSelected: (RedemptionCategory) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        edgePadding = 16.dp, // Padding lateral para que no esté pegado
        containerColor = Color.Transparent,
        indicator = { /* No hay indicador inferior, se maneja con el Box */ }
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(
                        color = if (isSelected) PrincipalAzul else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(category) },
                    modifier = Modifier.background(Color.Transparent),
                    text = {
                        Text(
                            text = category.title,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}

// --- Componente: Tarjeta de Puntos Disponibles (Reutilizada) ---
@Composable
fun RedemptionPointsCard(points: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                Text("Puntos Disponibles", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("$points pts", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))

                // Botón Canjear Puntos Deshabilitado o con una acción simple ya que estamos en la pantalla de canje
                Button(
                    onClick = { /* Lógica de confirmación de canje */ },
                    // CAMBIO A TU COLOR EXISTENTE:
                    colors = ButtonDefaults.buttonColors(containerColor = textFiel),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Canjear", fontSize = 12.sp)
                }
            }
        }
    }
}


// --- Componente: Tarjeta de Item Canjeable ---
@Composable
fun RedeemableItemCard(item: RedeemableItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* Mostrar detalles del canje */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                // Icono del Producto (simulación con un Box amarillo)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(item.iconColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Aquí iría el Icono real R.drawable.ic_screwdriver, etc.
                    Text("⚙️", fontSize = 24.sp) // Símbolo de ejemplo temporal
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(item.name, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    Text(item.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2)
                }
            }

            // Puntos necesarios y Botón "Canjear"
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.cost} pts",
                    fontWeight = FontWeight.Bold,
                    color = PrincipalAzul
                )
                Spacer(Modifier.height(8.dp))
                // WorkerPointRedemptionScreen.kt - Dentro de RedemptionPointsCard
// ...
                // Botón Canjear Puntos Deshabilitado o con una acción simple ya que estamos en la pantalla de canje
                Button(
                    onClick = { /* Ya estamos en la pantalla de canje */ },
                    // CAMBIO A TU COLOR EXISTENTE:
                    colors = ButtonDefaults.buttonColors(containerColor = textFiel),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Canjear Puntos", color = Color.White)
                }
// ...
            }
        }
    }
}


// --- Preview ---
@Preview(showBackground = true)
@Composable
fun WorkerPointRedemptionScreenPreview() {
    WorkerPointRedemptionScreen(navController = rememberNavController())
}//fin