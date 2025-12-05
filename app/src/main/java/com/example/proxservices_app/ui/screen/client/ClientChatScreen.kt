package com.example.proxservices_app.ui.screen.client


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proxservices_app.R
import com.example.proxservices_app.ui.theme.GrisFondo
import com.example.proxservices_app.ui.theme.PrincipalAzul
import com.example.proxservices_app.ui.theme.AccentTurquesa // Usaremos este color para el botón de enviar
import com.example.proxservices_app.ui.theme.GrisTextoSecundario

// Definición de colores del chat
val BubbleClient = AccentTurquesa // Turquesa
val BubbleWorker = Color(0xFFE0E0E0) // Gris claro

/* ---------------- DATOS DE MUESTRA DEL CHAT ---------------- */

enum class MessageOwner { WORKER, CLIENT }
data class ChatMessage(
    val text: String,
    val owner: MessageOwner,
    val time: String,
    val isRead: Boolean = true,
    val isImage: Boolean = false,
    val location: Boolean = false
)

private val sampleChat = listOf(
    ChatMessage(text = "Hoy", owner = MessageOwner.CLIENT, time = "", isImage = true), // Separador de tiempo
    ChatMessage(text = "Hola Carlos, ¿cómo estás? Necesito ayuda con la limpieza de mi oficina", owner = MessageOwner.CLIENT, time = "09:15", isRead = true),
    ChatMessage(text = "¡Hola! Muy bien, gracias. Claro que sí, estaré encantada de ayudarte. ¿Qué tipo de limpieza necesitas?", owner = MessageOwner.WORKER, time = "09:17", isRead = true),
    ChatMessage(text = "Es una oficina de unos 80m², necesito limpieza profunda de escritorios, baños y área común", owner = MessageOwner.CLIENT, time = "09:20", isRead = true),
    ChatMessage(text = "Perfecto, tengo experiencia con oficinas. ¿Cuándo te gustaría que fuera?", owner = MessageOwner.WORKER, time = "09:21", isRead = true),
    ChatMessage(text = "MI ubicación\nAv. Reforma 1234, Col. Centro", owner = MessageOwner.CLIENT, time = "09:22", isRead = true, location = true),
    ChatMessage(text = "¡Excelente! Está muy cerca de donde vivo. ¿Mañana por la mañana te parece bien?", owner = MessageOwner.WORKER, time = "09:24", isRead = true),
    ChatMessage(text = "Te envío algunas fotos del estado actual de la oficina", owner = MessageOwner.CLIENT, time = "09:25", isRead = true, isImage = true),
)

/* ---------------- PANTALLA PRINCIPAL ---------------- */

@Composable
fun ClientChatScreen(workerName: String, onNavigateBack: () -> Unit) {
    val listState = rememberLazyListState()
    val workerStatus = "En línea"
    var messageInput by remember { mutableStateOf("") }

    // Scroll al final al cargar
    LaunchedEffect(sampleChat.size) {
        if (sampleChat.isNotEmpty()) {
            listState.scrollToItem(sampleChat.size - 1)
        }
    }

    Scaffold(
        topBar = { ChatTopBar(workerName, workerStatus, onNavigateBack) },
        containerColor = GrisFondo,
        bottomBar = { ChatInputBar(messageInput, onMessageChange = { messageInput = it }) }
    ) { paddingValues ->
        // LazyColumn para el cuerpo del chat
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleChat) { message ->
                if (message.text == "Hoy" && message.isImage) {
                    TimeSeparator(message.text)
                } else {
                    ChatBubble(message)
                }
            }
        }
    }
}

/* ---------------- COMPONENTES DEL CHAT ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(workerName: String, workerStatus: String, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar del trabajador
                Image(
                    painter = painterResource(id = R.drawable.logo_usuario), // Usando un placeholder
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PrincipalAzul),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(workerName, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)
                    Text(workerStatus, fontSize = 13.sp, color = AccentTurquesa) // Usando AccentTurquesa para el status
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val horizontalAlignment = if (message.owner == MessageOwner.CLIENT) Alignment.End else Alignment.Start
    val bubbleColor = if (message.owner == MessageOwner.CLIENT) BubbleClient else BubbleWorker
    val textColor = if (message.owner == MessageOwner.CLIENT) Color.White else Color.Black
    val timeColor = if (message.owner == MessageOwner.CLIENT) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.5f)

    // El avatar del worker solo se muestra si el mensaje es del WORKER
    val showWorkerAvatar = message.owner == MessageOwner.WORKER

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = if (message.owner == MessageOwner.CLIENT) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // Avatar del trabajador (solo para mensajes del WORKER)
        if (showWorkerAvatar) {
            Image(
                painter = painterResource(id = R.drawable.logo_usuario), // Usando placeholder
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(PrincipalAzul)
                    .padding(end = 4.dp),
                contentScale = ContentScale.Crop
            )
        }

        // El contenido de la burbuja
        BubbleContent(message, bubbleColor, textColor, timeColor)

        // Marcas de lectura (solo para mensajes del CLIENTE)
        if (message.owner == MessageOwner.CLIENT) {
            Spacer(Modifier.width(4.dp))
            Icon(
                painter = painterResource(R.drawable.logo_doblepalomita), // Necesitas un drawable para el doble check (ic_check_double)
                contentDescription = "Leído",
                tint = Color.White,
                modifier = Modifier.size(16.dp).clip(CircleShape).background(BubbleClient).padding(3.dp)
            )
        }
    }
}

@Composable
fun BubbleContent(message: ChatMessage, bubbleColor: Color, textColor: Color, timeColor: Color) {
    // Definición de las esquinas redondeadas
    val cornerShape = RoundedCornerShape(16.dp)

    // Definición de la forma personalizada para la 'cola' de la burbuja
    val bubbleShape: Shape = when (message.owner) {
        MessageOwner.CLIENT -> cornerShape.copy(bottomEnd = CornerSize(4.dp)) // Esquina interior inferior derecha
        MessageOwner.WORKER -> cornerShape.copy(bottomStart = CornerSize(4.dp)) // Esquina interior inferior izquierda
    }

    // Contenido de ubicación (el recuadro con el mapa/ubicación)
    if (message.location) {
        LocationBubble(message, bubbleColor, timeColor)
        return
    }

    // Contenido de imagen (la foto de la oficina)
    if (message.isImage) {
        ImageBubble(message, timeColor, bubbleShape)
        return
    }

    // Contenido de texto estándar
    Card(
        shape = bubbleShape,
        colors = CardDefaults.cardColors(containerColor = bubbleColor),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.widthIn(max = 280.dp) // Ancho máximo
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = message.time,
                color = timeColor,
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun TimeSeparator(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = GrisTextoSecundario,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun LocationBubble(message: ChatMessage, bubbleColor: Color, timeColor: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bubbleColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.widthIn(max = 250.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Ubicación",
                tint = if (message.owner == MessageOwner.CLIENT) Color.White else PrincipalAzul,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (message.owner == MessageOwner.CLIENT) Color.White.copy(alpha = 0.2f) else GrisFondo)
                    .padding(8.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                // Título "Mi Ubicación"
                Text(
                    text = "MI ubicación",
                    fontWeight = FontWeight.Bold,
                    color = if (message.owner == MessageOwner.CLIENT) Color.White else PrincipalAzul,
                    fontSize = 14.sp
                )
                // Dirección
                Text(
                    text = message.text.substringAfter('\n'),
                    color = if (message.owner == MessageOwner.CLIENT) Color.White else Color.Black,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = message.time,
                    color = timeColor,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun ImageBubble(message: ChatMessage, timeColor: Color, bubbleShape: Shape) {
    Card(
        shape = bubbleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.widthIn(max = 250.dp)
    ) {
        Column {
            // Imagen Placeholder (Asume que R.drawable.chat_office_photo existe)
            Image(
                painter = painterResource(id = R.drawable.logo_usuario), // Usando placeholder
                contentDescription = "Foto del estado actual de la oficina",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            // Texto y Hora
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BubbleClient)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = message.time,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputBar(messageInput: String, onMessageChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono de archivo adjunto (Cámara/Fotos)
        IconButton(
            onClick = { /* TODO: Abrir selector de archivos */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)) // Gris claro
        ) {
            Icon(
                painter = painterResource(R.drawable.logo_camara), // Necesitas un drawable para la cámara
                contentDescription = "Adjuntar archivo",
                tint = GrisTextoSecundario
            )
        }
        Spacer(Modifier.width(8.dp))

        // Icono de Añadir (El signo de más grande)
        IconButton(
            onClick = { /* TODO: Abrir menú de adjuntos */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AccentTurquesa)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Más opciones",
                tint = Color.White
            )
        }
        Spacer(Modifier.width(12.dp))

        // Campo de texto de entrada
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFE0E0E0)) // Fondo del input gris claro
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = messageInput,
                onValueChange = onMessageChange,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                decorationBox = { innerTextField ->
                    if (messageInput.isEmpty()) {
                        Text(
                            "Escribe un mensaje...",
                            color = GrisTextoSecundario,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
        Spacer(Modifier.width(12.dp))

        // Botón de Enviar (Círculo Turquesa con avión)
        IconButton(
            onClick = { /* TODO: Enviar mensaje */ },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AccentTurquesa)
        ) {
            Icon(
                Icons.Default.Send,
                contentDescription = "Enviar",
                tint = Color.White
            )
        }
    }
}