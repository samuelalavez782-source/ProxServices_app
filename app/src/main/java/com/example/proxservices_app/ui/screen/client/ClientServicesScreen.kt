package com.example.proxservices_app.ui.screen.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
// ... otras importaciones
import com.example.proto.ui.screen.client.MisServiciosScreen
// CORRECCIÓN 1: IMPORTACIÓN DE LA CLASE DE DATOS SERVICIO
import com.example.proxservices_app.ui.screen.client.Servicio


@Composable
fun ClientServicesScreen(
    // Si usas navigation, pasarías el NavController aquí
) {
    // 1. **ESTADO:** Guarda el objeto Servicio completo.
    var navigateToReviewService by remember { mutableStateOf<Servicio?>(null) }

    // 2. ACCIÓN DE CANCELACIÓN (Ya no marca error al importar Servicio)
    val handleCancel: (Servicio) -> Unit = { servicioToCancel ->
        println("Cancelación registrada: ID ${servicioToCancel.id}")
        // Lógica real de API/Base de datos iría aquí.
    }

    // 3. ACCIÓN DE RESEÑA
    val handleReviewClick: (Servicio) -> Unit = { servicioToReview ->
        navigateToReviewService = servicioToReview // Esto cambia la pantalla a ReviewScreen
        // Asumiendo que Servicio tiene una propiedad 'id'
        println("Acción: Navegar a reseña para ID ${servicioToReview.id}")
    }

    // 4. Lógica de presentación
    if (navigateToReviewService != null) {
        // Muestra la pantalla de reseña si tenemos un servicio
        // CORRECCIÓN 2: Llama a ReviewScreen con la nueva firma
        ReviewScreen(
            servicio = navigateToReviewService!!,
            // onBack y servicioId han sido eliminados de la llamada:
            onClose = { navigateToReviewService = null }, // Usamos onClose para cerrar el modal
            onSubmit = { rating, comentario -> // Nota: el parámetro es 'comentario'
                println("Reseña enviada: Rating $rating, Comentario: $comentario para ${navigateToReviewService!!.id}")
                navigateToReviewService = null // Vuelve después de guardar
            }
        )
    } else {
        // Muestra la pantalla de Mis Servicios
        MisServiciosScreen(
            onBack = { /* Lógica de navegación de vuelta */ },
            onCancelService = handleCancel,
            onReviewClick = handleReviewClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}