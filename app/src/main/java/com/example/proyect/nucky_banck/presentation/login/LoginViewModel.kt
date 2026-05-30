package com.example.proyect.nucky_banck.presentation.login

import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.usecase.LoginUseCase
import com.example.proyect.nucky_banck.R
import com.example.proyect.nucky_banck.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow()

    fun onCedulaChange(cedula: String) {

        val error = when {
            cedula.isBlank() -> null
            !cedula.all { it.isDigit() } -> "Solo se permiten números"
            cedula.length < 8 -> "Mínimo 8 dígitos"
            cedula.length > 10 -> "Máximo 10 dígitos"
            else -> null
        }
        _uiState.update { it.copy(cedula = cedula, cedulaError = error) }
    }

    fun onPasswordChange(password: String) {

        val error = when {
            password.isBlank() -> null
            password.length < 6 -> ""
            else               -> null
        }
        _uiState.update { it.copy(password = password, passwordError = error) }
    }

    fun onLoginClicked(
        onSuccess: () -> Unit,
        onError: (Int) -> Unit
    ) {

        _uiState.update { it.copy(isLoading = true) }

        loginUseCase(
            _uiState.value.cedula,
            _uiState.value.password
        ) { success, message ->

            _uiState.update { it.copy(isLoading = false) }

            if (success) {
                onSuccess()
            } else {

                _uiState.update { it.copy(passwordError = "") }
                onError(message)
            }
        }
    }
}