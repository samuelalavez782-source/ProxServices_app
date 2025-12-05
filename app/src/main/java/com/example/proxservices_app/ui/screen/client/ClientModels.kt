package com.example.proxservices_app.ui.screen.client

import android.graphics.Color


// Clase FilterState para la navegación y estado de filtros
data class FilterState(
    val selectedProfession: String = "",
    val maxDistanceKm: Float = 20f,
    val minRating: Int = 0,
    val isAvailable: Boolean = false
)
data class Profesional(
    val id: String, // Clave para LazyColumn
    val nombre: String,     // <-- Ahora aquí
    val oficio: String,     // <-- Ahora aquí
    val rating: Float,
    val avatarColor: Color, // Por ejemplo, Color(0xFFE57373)
    val distanciaKm: Float,
    val descripcionServicio: String // Como "Plomería residencial"
    // ... más campos
)

// Archivo de modelos (ClientModels.kt, por ejemplo)
// También puedes mover otras data classes aquí si lo deseas
data class Category(val id: Int, val name: String, val iconResId: Int)
data class ServiceResult(val name: String, val profession: String, val rating: Float, val isAvailable: Boolean)

// Define aquí otros modelos que necesites compartir entre tus pantallas de cliente.