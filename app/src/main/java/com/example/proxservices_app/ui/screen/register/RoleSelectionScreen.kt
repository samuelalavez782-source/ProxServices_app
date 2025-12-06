package com.example.proxservices_app.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val CyanPrimary = Color(0xFF00BCD4)
private val DarkButton = Color(0xFF0D47A1)

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.size(100.dp).background(CyanPrimary, shape = RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "¡Bienvenido a tu\nnueva oportunidad!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkButton,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))


        Button(
            onClick = { navController.navigate("register_client") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            shape = RoundedCornerShape(30.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Soy Cliente", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { navController.navigate("register_worker") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkButton),
            shape = RoundedCornerShape(30.dp)
        ) {
            Icon(Icons.Default.BusinessCenter, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Soy Trabajador", fontSize = 18.sp)
        }
    }
}