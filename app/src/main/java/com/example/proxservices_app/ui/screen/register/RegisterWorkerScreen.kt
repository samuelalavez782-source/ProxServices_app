package com.example.proxservices_app.ui.screen.register

import android.net.Uri
import android.provider.OpenableColumns
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.UploadFile
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proxservices_app.data.api.RetrofitInstance
import com.example.proxservices_app.data.api.RegisterRequest
import kotlinx.coroutines.launch

// Colores del diseño
private val WorkerHeaderColor = Color(0xFF0D47A1)
private val CyanPrimary = Color(0xFF00BCD4)
private val BackgroundColor = Color(0xFFF5F5F5)
private val ErrorColor = Color(0xFFB00020)
private val SuccessColor = Color(0xFF4CAF50)

@Composable
fun RegisterWorkerScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }


    var oficio by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var zona by remember { mutableStateOf("") }


    var refName by remember { mutableStateOf("") }
    var refPhone by remember { mutableStateOf("") }


    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }


    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var oficioError by remember { mutableStateOf<String?>(null) }
    var experienciaError by remember { mutableStateOf<String?>(null) }
    var zonaError by remember { mutableStateOf<String?>(null) }
    var fileError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val sizeIndex = c.getColumnIndex(OpenableColumns.SIZE)
                    val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                    if (sizeIndex != -1 && nameIndex != -1) {
                        val size = c.getLong(sizeIndex)
                        val name = c.getString(nameIndex)

                        if (size > 5 * 1024 * 1024) {
                            fileError = "El archivo excede los 5MB"
                            selectedFileUri = null
                            selectedFileName = ""
                        } else {
                            fileError = null
                            selectedFileUri = it
                            selectedFileName = name
                        }
                    } else {
                        selectedFileUri = it
                        selectedFileName = "Archivo seleccionado"
                        fileError = null
                    }
                }
            }
        }
    }


    fun validate(): Boolean {
        var isValid = true


        if (name.isBlank()) {
            nameError = "El nombre es obligatorio"
            isValid = false
        } else if (!name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"))) {
            nameError = "Sin caracteres especiales ni números"
            isValid = false
        } else {
            nameError = null
        }


        if (email.isBlank()) {
            emailError = "El correo es obligatorio"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Correo inválido"
            isValid = false
        } else {
            emailError = null
        }


        if (phone.isBlank()) {
            phoneError = "Obligatorio"
            isValid = false
        } else if (phone.length != 10 || !phone.all { it.isDigit() }) {
            phoneError = "Debe ser de 10 dígitos"
            isValid = false
        } else {
            phoneError = null
        }


        if (password.length < 8) {
            passwordError = "Mínimo 8 caracteres"
            isValid = false
        } else if (!password.any { it.isDigit() } || !password.any { it.isUpperCase() }) {
            passwordError = "Falta 1 mayúscula y 1 número"
            isValid = false
        } else {
            passwordError = null
        }


        if (confirmPassword != password) {
            confirmPasswordError = "No coinciden"
            isValid = false
        } else {
            confirmPasswordError = null
        }


        if (oficio.isBlank()) {
            oficioError = "Campo obligatorio"
            isValid = false
        } else {
            oficioError = null
        }

        if (experiencia.isBlank()) {
            experienciaError = "Campo obligatorio"
            isValid = false
        } else {
            experienciaError = null
        }

        if (zona.isBlank()) {
            zonaError = "Campo obligatorio"
            isValid = false
        } else {
            zonaError = null
        }


        if (selectedFileUri == null) {
            fileError = "Debes adjuntar un comprobante"
            isValid = false
        } else {
            fileError = null
        }

        return isValid
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    color = WorkerHeaderColor,
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Registra tu perfil\nprofesional",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }


        Column(modifier = Modifier.padding(24.dp)) {


            WorkerFormLabel("Nombre completo")
            WorkerCustomInput(
                value = name,
                onValueChange = { name = it; nameError = null },
                placeholder = "Ingresa tu nombre completo",
                isError = nameError != null,
                errorMessage = nameError
            )

            WorkerFormLabel("Correo electrónico")
            WorkerCustomInput(
                value = email,
                onValueChange = { email = it; emailError = null },
                placeholder = "ejemplo@correo.com",
                keyboardType = KeyboardType.Email,
                isError = emailError != null,
                errorMessage = emailError
            )

            WorkerFormLabel("Teléfono")
            WorkerCustomInput(
                value = phone,
                onValueChange = { if (it.length <= 10) { phone = it; phoneError = null } },
                placeholder = "+52 123 456 7890",
                keyboardType = KeyboardType.Phone,
                isError = phoneError != null,
                errorMessage = phoneError
            )

            WorkerFormLabel("Contraseña")
            WorkerCustomPasswordInput(
                value = password,
                onValueChange = { password = it; passwordError = null },
                placeholder = "Mínimo 8 caracteres",
                isError = passwordError != null,
                errorMessage = passwordError
            )

            WorkerFormLabel("Confirmar contraseña")
            WorkerCustomPasswordInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordError = null },
                placeholder = "Repite tu contraseña",
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError
            )

            WorkerFormLabel("Ubicación")
            WorkerCustomInput(
                value = location,
                onValueChange = { location = it },
                placeholder = "Ciudad, Estado, País"
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text("Comprobante de domicilio", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            val borderColor = if (fileError != null) ErrorColor else if (selectedFileUri != null) SuccessColor else CyanPrimary.copy(alpha = 0.5f)
            val backgroundColor = if (fileError != null) ErrorColor.copy(alpha = 0.05f) else if (selectedFileUri != null) SuccessColor.copy(alpha = 0.05f) else Color.White

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(backgroundColor, RoundedCornerShape(12.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .clickable { launcher.launch("*/*") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (selectedFileUri != null) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessColor, modifier = Modifier.size(32.dp))
                        Text(
                            text = selectedFileName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text("Archivo listo para subir", fontSize = 11.sp, color = SuccessColor)
                    } else {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                        Text("Subir recibo de luz/agua", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("PDF, JPG, PNG (máx. 5MB)", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
            if (fileError != null) {
                Text(fileError!!, color = ErrorColor, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp, top = 4.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))


            Text("Referencia de confianza", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    WorkerCustomInput(value = refName, onValueChange = { refName = it }, placeholder = "Nombre completo de la referencia")
                    Spacer(modifier = Modifier.height(8.dp))
                    WorkerCustomInput(value = refPhone, onValueChange = { if (it.length <= 10) refPhone = it }, placeholder = "Teléfono de la referencia", keyboardType = KeyboardType.Phone)
                }
            }


            WorkerFormLabel("Selección de oficio")
            WorkerCustomInput(
                value = oficio,
                onValueChange = { oficio = it; oficioError = null },
                placeholder = "Selecciona tu oficio principal",
                isError = oficioError != null,
                errorMessage = oficioError
            )

            WorkerFormLabel("Años de experiencia")
            WorkerCustomInput(
                value = experiencia,
                onValueChange = { experiencia = it; experienciaError = null },
                placeholder = "Ej. 5 años",
                keyboardType = KeyboardType.Text, // Puede ser "5 años"
                isError = experienciaError != null,
                errorMessage = experienciaError
            )

            WorkerFormLabel("Zona de trabajo / Cobertura")
            WorkerCustomInput(
                value = zona,
                onValueChange = { zona = it; zonaError = null },
                placeholder = "Ej: Ciudad de México, Zona Norte",
                isError = zonaError != null,
                errorMessage = zonaError
            )

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                    if (validate()) {
                        isLoading = true
                        scope.launch {
                            try {

                                val request = RegisterRequest(
                                    name = name,
                                    email = email,
                                    phone = phone,
                                    password = password,
                                    role = "worker",
                                    address = "$location | Zona: $zona",
                                    trade = "$oficio | Exp: $experiencia"
                                )
                                val response = RetrofitInstance.api.register(request)

                                if (response.success) {
                                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    navController.navigate("worker_home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                shape = RoundedCornerShape(25.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Crear Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                "Tu información será verificada para mantener la calidad y seguridad de nuestra comunidad.",
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("¿Ya tienes una cuenta?", fontSize = 14.sp, color = Color.Gray)
                Text(
                    "Iniciar Sesión",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanPrimary,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}



@Composable
private fun WorkerFormLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 4.dp, top = 12.dp)
    )
}

@Composable
private fun WorkerCustomInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black, // Texto negro al escribir
                unfocusedTextColor = Color.Black, // Texto negro
                focusedBorderColor = if (isError) ErrorColor else CyanPrimary,
                unfocusedBorderColor = if (isError) ErrorColor else Color.LightGray,
                errorBorderColor = ErrorColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            isError = isError,
            trailingIcon = if (isError) {
                { Icon(Icons.Filled.Error, "Error", tint = ErrorColor) }
            } else null
        )
        if (isError && errorMessage != null) {
            Text(errorMessage, color = ErrorColor, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp, top = 2.dp))
        }
    }
}

@Composable
private fun WorkerCustomPasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var visible by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { visible = !visible }) {
                    Icon(imageVector = image, contentDescription = null, tint = CyanPrimary)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = if (isError) ErrorColor else CyanPrimary,
                unfocusedBorderColor = if (isError) ErrorColor else Color.LightGray,
                errorBorderColor = ErrorColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true,
            isError = isError
        )
        if (isError && errorMessage != null) {
            Text(errorMessage, color = ErrorColor, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp, top = 2.dp))
        }
    }
}