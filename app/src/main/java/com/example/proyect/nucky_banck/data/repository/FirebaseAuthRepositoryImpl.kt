package com.example.proyect.nucky_banck.data.repository

import com.example.proyect.nucky_banck.data.datasource.FirebaseUserDataSource
import com.example.proyect.nucky_banck.domain.model.User
import com.example.proyect.nucky_banck.domain.model.Transfer
import com.example.proyect.nucky_banck.domain.repository.AuthRepository
import com.example.proyect.nucky_banck.R
import com.google.firebase.auth.FirebaseAuth

// Es el intermediario entre los UseCases y Firebase (FirebaseUserDataSource).
// Implementa todos los métodos definidos en AuthRepository.
class FirebaseAuthRepositoryImpl(private val dataSource: FirebaseUserDataSource = FirebaseUserDataSource()
) : AuthRepository {

    override fun login(cedula: String, password: String, onResult: (Boolean, Int) -> Unit) {
        dataSource.getUser(cedula)
            .addOnSuccessListener { dataUser ->
                if (!dataUser.exists()) {
                    onResult(false, R.string.error_login_failed)
                    return@addOnSuccessListener
                }
                val dbPassword = dataUser.child("password").value.toString()

                if (dbPassword == password) {
                    onResult(true, 0)
                } else {
                    onResult(false, R.string.error_login_failed)
                }

            }.addOnFailureListener {
                onResult(false, R.string.error_login_failed)
            }
    }


    override fun register(user: User, onResult: (Boolean, Int) -> Unit) {
        dataSource.getUser(user.cedula)
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    onResult(false, R.string.error_user_exists)
                    return@addOnSuccessListener
                }

                val userData = mapOf(
                    "nombre" to user.fullName,
                    "cedula" to user.cedula,
                    "password" to user.password,
                    "saldo" to "100000.0")

                dataSource.saveUser(user.cedula, userData)
                    .addOnSuccessListener {
                        onResult(true, R.string.register_success_message)
                    }
                    .addOnFailureListener {
                        onResult(false, R.string.error_register_failed)
                    }

            }.addOnFailureListener {
                onResult(false, R.string.error_register_failed)
            }
    }


    override fun getUser(cedula: String, onResult: (User?) -> Unit) {
        dataSource.getUser(cedula)
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    onResult(null)
                    return@addOnSuccessListener
                }
                val saldoGuardado = snapshot.child("saldo").value
                val saldo = saldoGuardado?.toString()?.toDoubleOrNull() ?: 100000.0

                val user = User(
                    fullName = snapshot.child("nombre").value.toString(),
                    cedula = snapshot.child("cedula").value.toString(),
                    password = snapshot.child("password").value.toString(),
                    saldo = saldo
                )

                onResult(user)

            }.addOnFailureListener {
                onResult(null)
            }
    }


    override fun transferir(cedulaOrigen: String, cedulaDestino: String, monto: Double, onResult: (Boolean, String) -> Unit) {
        if (cedulaOrigen == cedulaDestino) {
            onResult(false, "No puedes transferirte a ti mismo")
            return
        }
        dataSource.getUser(cedulaDestino)
            .addOnSuccessListener { snapshotDestino ->
                if (!snapshotDestino.exists()) {
                    onResult(false, "El usuario destino no existe")
                    return@addOnSuccessListener
                }

                val saldoDestino = snapshotDestino.child("saldo").value?.toString()?.toDoubleOrNull() ?: 100000.0

                dataSource.getUser(cedulaOrigen)
                    .addOnSuccessListener { snapshotOrigen ->

                        val saldoActual = snapshotOrigen.child("saldo").value?.toString()?.toDoubleOrNull() ?: 100000.0

                        if (monto > saldoActual) {
                            onResult(false, "Saldo insuficiente. Tu saldo es: ${"$%,.2f".format(saldoActual)}")
                            return@addOnSuccessListener
                        }

                        val nuevoSaldoOrigen = saldoActual - monto
                        val nuevoSaldoDestino = saldoDestino + monto
                        dataSource.actualizarSaldo(cedulaOrigen, nuevoSaldoOrigen)
                            .addOnSuccessListener {
                                dataSource.actualizarSaldo(cedulaDestino, nuevoSaldoDestino
                                ).addOnSuccessListener{
                                    onResult(true, "Transferencia exitosa. Nuevo saldo: ${"$%,.2f".format(nuevoSaldoOrigen)}")
                                }.addOnFailureListener {
                                        onResult(false, "Error al actualizar el saldo de destino")
                                }
                            }.addOnFailureListener {
                                onResult(false, "Error al actualizar tu saldo")
                            }

                    }.addOnFailureListener {
                        onResult(false, "Error al obtener tu saldo")
                    }

            }.addOnFailureListener {
                onResult(false, "Error al verificar el usuario de destino")
            }
    }


    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
