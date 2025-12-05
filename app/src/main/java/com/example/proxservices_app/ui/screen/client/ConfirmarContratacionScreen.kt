package com.example.proxservices_app.ui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle

// 🚀 IMPORTS AÑADIDOS
import com.example.proxservices_app.ui.screen.client.ServiceRepository // Para guardar el servicio
// Color asumido para el botón 'Disponible'


// --- Datos y Colores ---
val LightGrayBackground = Color(0xFFF7F7F7)
val ConfirmationBlue = Color(0xFF00C890) // Color del botón de confirmación

// --- Constantes de Estado ---
const val DEFAULT_DATE_TEXT = "Seleccionar fecha"
const val DEFAULT_ADDRESS_TEXT = "Escribir dirección"

// --- COMPOSABLE PRINCIPAL: Confirmar Contratación ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarContratacionScreen(
    professionalName: String,
    professionalOficio: String, // 🚀 AÑADIDO: Necesario para guardar el servicio
    onBack: () -> Unit,
    onConfirmAndNavigate: () -> Unit // 🚀 AÑADIDO: Función para guardar y navegar a Mis Servicios
) {
    // ESTADOS PARA DATOS
    var problemDescription by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(DEFAULT_DATE_TEXT) }
    var selectedAddress by remember { mutableStateOf(DEFAULT_ADDRESS_TEXT) }

    val estimatedCost = "A definir por el profesional" // Precio fijo para el nuevo servicio

    // ESTADOS PARA ERRORES DE VALIDACIÓN
    var dateError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }

    // LÓGICA DE VALIDACIÓN, GUARDADO Y NAVEGACIÓN
    val onConfirmClick: () -> Unit = {
        dateError = selectedDate == DEFAULT_DATE_TEXT
        addressError = selectedAddress == DEFAULT_ADDRESS_TEXT || selectedAddress.isBlank()

        if (!dateError && !addressError) {

            // 🚀 PASO CLAVE: Guardar el servicio en la lista global
            ServiceRepository.confirmContratacion(
                nombre = professionalName,
                oficio = professionalOficio,
                direccion = selectedAddress,
                fechaHora = selectedDate,
                precioEstimado = estimatedCost
            )

            // Navegar a la pantalla de servicios pendientes
            onConfirmAndNavigate()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Contratación", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(horizontal = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = LightGrayBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                ProfessionalConfirmationCard(professionalName, professionalOficio) // 🚀 Pasamos el oficio
                Spacer(modifier = Modifier.height(16.dp))

                // 2. Sección de Detalles del Servicio
                ServiceDetailsSection(
                    problemDescription = problemDescription,
                    onDescriptionChange = { problemDescription = it },
                    selectedDate = selectedDate,
                    onDateChange = { selectedDate = it },
                    dateError = dateError,
                    selectedAddress = selectedAddress,
                    onAddressChange = { selectedAddress = it },
                    addressError = addressError,
                    onConfirmClick = onConfirmClick // 🚀 Llama a la lógica de guardado
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Sección de Costo Estimado
                EstimatedCostSection()

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --------------------------------------------------------------------------------------
// --- Sección de Detalles del Servicio (Sin cambios funcionales, solo se mueve aquí) ---
// --------------------------------------------------------------------------------------

@Composable
fun ServiceDetailsSection(
    problemDescription: String,
    onDescriptionChange: (String) -> Unit,
    selectedDate: String,
    onDateChange: (String) -> Unit,
    dateError: Boolean,
    selectedAddress: String,
    onAddressChange: (String) -> Unit,
    addressError: Boolean,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Detalles del Servicio", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))

        // 1. Tipo de servicio (Fijo)
        DetailField("Tipo de servicio", modifier = Modifier.padding(bottom = 4.dp))
        SimpleBox(text = "Reparación de plomería", isSelectable = false)

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Descripción del problema
        DetailField("Descripción del problema", modifier = Modifier.padding(bottom = 4.dp))
        DescriptionTextField(problemDescription, onDescriptionChange)

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Fecha preferida (Ahora es clickable y tiene error)
        DetailField("Fecha preferida", modifier = Modifier.padding(bottom = 4.dp))
        SimpleBox(
            text = selectedDate,
            onTextChange = onDateChange,
            isPlaceholder = selectedDate == DEFAULT_DATE_TEXT,
            isEditable = false,
            isSelectable = true,
            isError = dateError,
            onClick = {
                if (selectedDate == DEFAULT_DATE_TEXT) {
                    onDateChange("05/12/2025")
                }
            }
        )
        if (dateError) {
            Text("Selecciona una fecha válida", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }


        Spacer(modifier = Modifier.height(16.dp))

        // 4. Dirección del servicio (Ahora es editable y tiene error)
        DetailField("Lugar del servicio", modifier = Modifier.padding(bottom = 4.dp))
        SimpleBox(
            text = selectedAddress,
            onTextChange = onAddressChange,
            isPlaceholder = selectedAddress == DEFAULT_ADDRESS_TEXT,
            isAddress = true,
            isEditable = true,
            isSelectable = false,
            isError = addressError
        )
        if (addressError) {
            Text("Ingresa una dirección válida", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Botón Confirmar Contratación
        Button(
            onClick = onConfirmClick, // Llama a la lógica de guardado y validación
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ConfirmationBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Confirmar Contratación", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

// --------------------------------------------------------------------------------------
// --- Componentes Auxiliares (Actualizado ProfessionalConfirmationCard) ----------------
// --------------------------------------------------------------------------------------

@Composable
fun DescriptionTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(LightGrayBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            ),
            modifier = Modifier.fillMaxSize(),
            singleLine = false,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "Descripción del problema...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

// 🚀 ACTUALIZACIÓN: Recibe el oficio para mostrarlo
@Composable
fun ProfessionalConfirmationCard(name: String, oficio: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono/Avatar del profesional
            Image(
                painter = painterResource(id = R.drawable.ic_limpieza),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Estrellas (simulación)
                    repeat(4) { Icon(painter = painterResource(id = R.drawable.ic_limpieza), contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp)) }
                    Icon(painter = painterResource(id = R.drawable.ic_limpieza), contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.8", fontSize = 14.sp, color = Color.Gray)
                }
                Text(oficio, fontSize = 14.sp, color = Color.Gray) // 🚀 Muestra el oficio
                Spacer(modifier = Modifier.height(4.dp))
                Text("3.2 km de distancia", fontSize = 12.sp, color = Color.Gray)
            }
            Button(
                onClick = { /* Acción para disponibilidad */ },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTurquoise),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Disponible", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
// --- SimpleBox (Sin cambios) ---
@Composable
fun SimpleBox(
    text: String,
    onTextChange: (String) -> Unit = {},
    height: Dp = 48.dp,
    isPlaceholder: Boolean = false,
    isAddress: Boolean = false,
    isEditable: Boolean = false,
    isSelectable: Boolean = false,
    isError: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val boxColor = if (isError) Color(0xFFFDD8D8) else LightGrayBackground
    val borderColor = if (isError) MaterialTheme.colorScheme.error else Color.Transparent

    val baseModifier = Modifier
        .fillMaxWidth()
        .height(height)
        .clip(RoundedCornerShape(8.dp))
        .background(boxColor)
        .padding(horizontal = 12.dp)
        .border(
            width = if (isError) 1.dp else 0.dp,
            color = borderColor,
            shape = RoundedCornerShape(8.dp)
        )

    if (isEditable) {
        Box(
            modifier = baseModifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = if (text == DEFAULT_ADDRESS_TEXT) "" else text,
                onValueChange = onTextChange,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = if (isAddress) 14.sp else 16.sp,
                    fontWeight = if (isAddress) FontWeight.Normal else FontWeight.SemiBold
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (text == DEFAULT_ADDRESS_TEXT) {
                        Text(DEFAULT_ADDRESS_TEXT, color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }
    } else {
        val clickableModifier = if (isSelectable && onClick != null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier
        }

        Box(
            modifier = baseModifier.then(clickableModifier),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                color = if (isPlaceholder || text == DEFAULT_DATE_TEXT || text == DEFAULT_ADDRESS_TEXT) Color.Gray else Color.Black,
                fontSize = if (isAddress) 14.sp else 16.sp,
                fontWeight = if (isAddress) FontWeight.Normal else FontWeight.SemiBold
            )
        }
    }
}

// --- EstimatedCostSection (Sin cambios) ---

@Composable
fun EstimatedCostSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Costo Estimado", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 4.dp))

        Text("A definir por el profesional", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.Black)
        Text("El pago se realiza al finalizar el servicio.", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text("• Recibirás actualizaciones en tiempo real", fontSize = 12.sp, color = Color.Gray)
            Text("• Puedes cancelar antes de que el profesional acepte", fontSize = 12.sp, color = Color.Gray)
            Text("• Tu información está protegida", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun DetailField(text: String, modifier: Modifier = Modifier) {
    Text(text, fontSize = 14.sp, color = Color.Gray, modifier = modifier)
}