package com.example.proxservices_app.ui.navigation

import androidx.compose.runtime.Composable

enum class UserRole { CLIENT, WORKER, UNAUTHENTICATED }

@Composable
fun NavGraph() {
    val userRole = UserRole.WORKER

    when (userRole) {
        UserRole.CLIENT -> ClientNav()
        UserRole.WORKER -> WorkerNav()
        UserRole.UNAUTHENTICATED -> {
        }
    }
}


