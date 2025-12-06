package com.example.proxservices_app.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String,
    val address: String? = null,
    val trade: String? = null
)

data class AuthResponse(
    val success: Boolean,
    val token: String?,
    val message: String,
    val userRole: String?
)


interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}


object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.67:8000/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}