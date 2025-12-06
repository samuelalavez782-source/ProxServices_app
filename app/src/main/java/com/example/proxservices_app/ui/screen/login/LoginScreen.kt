package com.example.proxservices_app.ui.screen.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proxservices_app.data.api.RetrofitInstance
import com.example.proxservices_app.data.api.LoginRequest
import kotlinx.coroutines.launch


private val CyanPrimary = Color(0xFF00BCD4)
private val DarkText = Color(0xFF0D47A1)
private val ErrorColor = Color(0xFFB00020)

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    var emailError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    fun validateEmail(input: String) {
        email = input
        if (input.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            emailError = "Formato de correo inválido"
        } else {
            emailError = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {

            Text("LOGO", color = CyanPrimary, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Inicia sesión\npara continuar",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))


        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = email,
                onValueChange = { validateEmail(it) },
                label = { Text("Correo electrónico") },
                placeholder = { Text("tu@email.com", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black, // Texto negro al escribir
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = if (emailError != null) ErrorColor else CyanPrimary,
                    unfocusedBorderColor = if (emailError != null) ErrorColor else Color.LightGray,
                    errorBorderColor = ErrorColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                trailingIcon = if (emailError != null) {
                    { Icon(Icons.Filled.Error, "Error", tint = ErrorColor) }
                } else null
            )
            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = ErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Ver contraseña", tint = CyanPrimary)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = CyanPrimary,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = CyanPrimary,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { /* Acción recuperar */ }
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {

                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (emailError != null) {
                    Toast.makeText(context, "Corrige el correo antes de continuar", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                scope.launch {
                    try {
                        val request = LoginRequest(email, password)
                        val response = RetrofitInstance.api.login(request)

                        if (response.success) {
                            Toast.makeText(context, "Bienvenido: ${response.message}", Toast.LENGTH_SHORT).show()

                            val role = response.userRole ?: "client" // Default a client si es nulo

                            if (role.lowercase() == "worker") {
                                navController.navigate("worker_home") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            } else {
                                navController.navigate("client_home") {
                                    popUpTo("welcome") { inclusive = true }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {

                        Toast.makeText(context, "Error de conexión: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Entrar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("¿No tienes cuenta? ", color = Color.Gray)
            Text(
                "Crear una cuenta",
                color = CyanPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {

                    navController.navigate("welcome") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
    }
}