package com.example.proxservices_app.ui.screen.client

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color // ✅ Color de Compose

// --- DEFINICIÓN DE CLASES ---
// (Inclúyelas aquí si no están en otro archivo del paquete)

enum class ServiceTab { PENDIENTES, EN_PROGRESO, COMPLETADOS }

data class Servicio(
    val id: String,
    val nombre: String,
    val oficio: String,
    val avatarColor: Color=Color(0xFFE0E0E0),
    val rating: Float? = null,
    val direccion: String,
    val identificador: String,
    val fechaHora: String,
    val tiempoEstimado: String? = null,
    val horaInicio: String? = null,
    val tiempoCompletadoHoras: Int? = null,
    val precioEstimado: String,
    var estado: ServiceTab
)

// --- REPOSITORIO DE SERVICIOS ---

object ServiceRepository {

    // Lista mutable inicializada con los datos de ejemplo que incluyen más personas.
    val services: SnapshotStateList<Servicio> = mutableStateListOf(

        // =========================================================
        // PENDIENTES (3 Servicios)
        // =========================================================
        Servicio(id = "SP-001", nombre = "Carlos Herrera", oficio = "Plomero Certificado", avatarColor = Color(0xFFE57373), direccion = "Av. Principal 123", identificador = "#SP-2024-001", fechaHora = "15 Nov, 2024 • 10:00 AM", tiempoEstimado = "2-3 horas", precioEstimado = "A definir", estado = ServiceTab.PENDIENTES),
        Servicio(id = "SP-002", nombre = "María Gonzales", oficio = "Electricista Residencial", avatarColor = Color(0xFF81C784), direccion = "Calle Olivos 50", identificador = "#SP-2024-002", fechaHora = "15 Nov, 2024 • 10:00 AM", tiempoEstimado = "2-3 horas", precioEstimado = "A definir", estado = ServiceTab.PENDIENTES),
        Servicio(id = "SP-003", nombre = "Javier Soto", oficio = "Técnico en Refrigeración", avatarColor = Color(0xFF64B5F6), direccion = "Col. Centro 45", identificador = "#SP-2024-003", fechaHora = "05 Dic, 2025 • 1:00 PM", tiempoEstimado = "1 hora", precioEstimado = "500 MXN", estado = ServiceTab.PENDIENTES),

        // =========================================================
        // EN PROGRESO (2 Servicios)
        // =========================================================
        Servicio(id = "EP-004", nombre = "Sofía Múñoz", oficio = "Jardinera", avatarColor = Color(0xFFFFCC80), direccion = "Calle del Sol 10", identificador = "#EP-2024-004", fechaHora = "03 Dic, 2025 • 9:00 AM", horaInicio = "9:15 AM", precioEstimado = "400 MXN", estado = ServiceTab.EN_PROGRESO),
        Servicio(id = "EP-005", nombre = "Miguel Díaz", oficio = "Técnico de A/C", avatarColor = Color(0xFF4DB6AC), direccion = "Av. Circuito Interior", identificador = "#EP-2024-005", fechaHora = "03 Dic, 2025 • 1:00 PM", horaInicio = "1:05 PM", precioEstimado = "A definir", estado = ServiceTab.EN_PROGRESO),

        // =========================================================
        // COMPLETADOS (2 Servicios)
        // =========================================================
        Servicio(id = "SC-006", nombre = "Laura Gómez", oficio = "Plomera Industrial", avatarColor = Color(0xFF90A4AE), direccion = "Av. Industrial 200", identificador = "#SC-2024-006", fechaHora = "20 Oct, 2024 • 11:00 AM", tiempoCompletadoHoras = 3, precioEstimado = "700 MXN", rating = 5.0f, estado = ServiceTab.COMPLETADOS),
        Servicio(id = "SC-007", nombre = "Ana Rodríguez", oficio = "Carpintera", avatarColor = Color(0xFFAED581), direccion = "Colonia Morelos", identificador = "#SC-2024-007", fechaHora = "05 Nov, 2024 • 4:00 PM", tiempoCompletadoHoras = 1, precioEstimado = "250 MXN", rating = null, estado = ServiceTab.COMPLETADOS)
    )

    /**
     * Agrega un nuevo servicio a la lista y lo marca como PENDIENTE.
     */
    fun confirmContratacion(
        nombre: String,
        oficio: String,
        direccion: String,
        fechaHora: String,
        precioEstimado: String
    ) {
        val newId = (services.size + 1).toString()
        val newService = Servicio(
            id = newId,
            nombre = nombre,
            oficio = oficio,
            direccion = direccion,
            identificador = "#SP-2025-$newId",
            fechaHora = fechaHora,
            tiempoEstimado = "1-2 horas",
            tiempoCompletadoHoras = null,
            precioEstimado = precioEstimado,
            estado = ServiceTab.PENDIENTES,
            rating = null,
            horaInicio = null,
            avatarColor = Color(0xFF00B0FF)
        )
        services.add(newService)
    }
}