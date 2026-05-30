package com.example.proyect.nucky_banck.domain.repository

import com.example.proyect.nucky_banck.domain.model.User
import com.example.proyect.nucky_banck.domain.model.Transfer
// Interfaz que define los métodos que usarán los UseCases.
// La implementación real está en data/repository/FirebaseAuthRepositoryImpl.kt
interface AuthRepository {

    // Verifica si la cédula y contraseña son correctas en Firebase
    fun login(cedula: String, password: String, onResult: (Boolean, Int) -> Unit)

    // Guarda un nuevo usuario en Firebase
    fun register(user: User, onResult: (Boolean, Int) -> Unit)

    // Carga los datos de un usuario desde Firebase
    fun getUser(cedula: String, onResult: (User?) -> Unit)

    // Realiza una transferencia: descuenta saldo al usuario origen
    fun transferir(cedulaOrigen: String, cedulaDestino: String, monto: Double, onResult: (Boolean, String) -> Unit)

    fun signOut()
}
