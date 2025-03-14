package com.zaurh.sparrow.presentation.auth.sign_up

import androidx.lifecycle.ViewModel
import com.zaurh.sparrow.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _emailState = MutableStateFlow("")
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow("")
    val passwordState = _passwordState.asStateFlow()

    private val _confirmPasswordState = MutableStateFlow("")
    val confirmPasswordState = _confirmPasswordState.asStateFlow()

    fun onEmailChange(email: String) {
        _emailState.value = email
    }

    fun onPasswordChange(password: String) {
        _passwordState.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPasswordState.value = confirmPassword
    }


}