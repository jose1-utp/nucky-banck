package com.example.proyect.nucky_banck.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

// Se conecta directamente con Firebase.
// Solo se encarga de traer y guardar datos, nada más.
class FirebaseUserDataSource {

    // Referencia al nodo "usuarios" en Firebase Realtime Database
    private val database = FirebaseDatabase.getInstance().getReference("usuarios")

    // Obtiene todos los datos de un usuario según su cédula
    fun getUser(cedula: String): Task<DataSnapshot> {
        return database.child(cedula).get()
    }

    // Guarda un nuevo usuario en Firebase
    fun saveUser(cedula: String, userData: Map<String, String>): Task<Void> {
        return database.child(cedula).setValue(userData)
    }

    // Actualiza el saldo de un usuario en Firebase
    fun actualizarSaldo(cedula: String, nuevoSaldo: Double): Task<Void> {
        return database.child(cedula).child("saldo").setValue(nuevoSaldo)
    }
}
