package com.example.proyect.nucky_banck.domain.model

data class User(
    val fullName: String = "",
    val cedula: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val saldo: Double = 100000.0,
    val isLoading: Boolean = false,

    // Errores en tiempo real
    val cedulaError: String? = null,
    val passwordError: String? = null,
    val fullNameError: String? = null,
    val confirmPasswordError: String? = null
)