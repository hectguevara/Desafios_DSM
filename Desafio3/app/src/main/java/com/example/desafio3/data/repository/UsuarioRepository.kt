package com.example.desafio3.data.repository

import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UsuarioRepository(private val dbHelper: DatabaseHelper) {

    private val auth = FirebaseAuth.getInstance()
    private val firebaseDb = FirebaseDatabase.getInstance().getReference("usuarios")

    fun registrarUsuario(
        correo: String,
        password: String,
        usuario: Usuario,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid ?: ""
                val usuarioCompleto = usuario.copy(id = userId, correo = correo)

                firebaseDb.child(userId).setValue(usuarioCompleto)
                    .addOnSuccessListener {
                        dbHelper.insertarUsuario(usuarioCompleto)
                        onSuccess(userId)
                    }
                    .addOnFailureListener { e ->
                        onError(e.message ?: "Error")
                    }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error")
            }
    }

    fun iniciarSesion(
        correo: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(correo, password)
            .addOnSuccessListener { result ->
                onSuccess(result.user?.uid ?: "")
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error")
            }
    }

    fun cerrarSesion() {
        auth.signOut()
    }

    fun obtenerUsuarioActual(): String? = auth.currentUser?.uid

    fun obtenerUsuarioLocal(userId: String): Usuario? = dbHelper.obtenerUsuarioPorId(userId)

    fun cargarUsuarioDeFirebase(
        userId: String,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseDb.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario != null) {
                    dbHelper.insertarUsuario(usuario)
                    onSuccess(usuario)
                } else {
                    onError("Error")
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error")
            }
    }
}

