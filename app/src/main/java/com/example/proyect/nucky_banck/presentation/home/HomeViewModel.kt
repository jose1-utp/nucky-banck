package com.example.proyect.nucky_banck.presentation.home

import androidx.lifecycle.ViewModel
import com.example.proyect.nucky_banck.data.repository.FirebaseAuthRepositoryImpl
import com.example.proyect.nucky_banck.domain.model.User
import com.example.proyect.nucky_banck.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class HomeViewModel(
    private val homeUseCase: HomeUseCase = HomeUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {


    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow()

    fun loadUserData(cedula: String) {

        homeUseCase(cedula) { user ->

            user?.let {

                _uiState.update { state ->
                    state.copy(
                        fullName = user.fullName,
                        cedula   = user.cedula,
                        saldo    = user.saldo
                    )
                }
            }
        }
    }
}
