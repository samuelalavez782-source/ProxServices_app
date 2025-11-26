package com.example.proxservices_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter


data class NavItem(
    val label: String,
    val icon: @Composable () -> Painter,
    val route: String
)

