package com.example.proyect.nucky_banck.domain.model

data class User(
    val fullName: String = "",
    val cedula: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val saldo: Double = 100000.0,
    val isLoading: Boolean = false,

    // Campos para la transferencia
    val cedulaDestino: String = "",
    val monto: String = "",
    val cedulaDestinoError: String? = null,
    val montoError: String? = null,
    val generalError: String? = null,
    val transferSuccess: Boolean = false,
    val successMessage: String = "",

    // Errores en tiempo real
    val cedulaError: String? = null,
    val passwordError: String? = null,
    val fullNameError: String? = null,
    val confirmPasswordError: String? = null
)