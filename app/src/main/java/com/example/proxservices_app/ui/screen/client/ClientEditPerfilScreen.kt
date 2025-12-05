package com.example.proxservices_app.ui.screen.client


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.R

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSave: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {

        // ----- TOP BAR -----
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF041C32)
                )
            }

            Spacer(modifier = Modifier.width(70.dp))

            Text(
                text = "Editar Perfil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF041C32)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ----- PHOTO -----
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(id = R.drawable.logo_pedro),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )

                IconButton(
                    onClick = { /* Abrir galería (si quieres luego lo hacemos) */ },
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color(0xFF00C2CB), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ----- FORM CARD -----
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Información Personal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF041C32)
                )

                Spacer(modifier = Modifier.height(18.dp))


                // --- Nombre ---
                FieldLabel("Nombre Completo")
                CustomTextField(value = fullName, onValueChange = { fullName = it })

                Spacer(modifier = Modifier.height(12.dp))


                // --- Correo ---
                FieldLabel("Correo Electrónico")
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(12.dp))


                // --- Teléfono ---
                FieldLabel("Teléfono")
                CustomTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(12.dp))


                // --- Dirección ---
                FieldLabel("Dirección")
                CustomTextField(value = address, onValueChange = { address = it })

                Spacer(modifier = Modifier.height(22.dp))
            }
        }

        // ----- SAVE BUTTON -----
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { onSave(fullName, email, phone, address) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C2CB)
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(52.dp)
            ) {
                Text(
                    text = "Guardar Cambios",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FieldLabel(label: String) {
    Text(
        text = label,
        color = Color(0xFF041C32),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF00C2CB)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    )
}
