package com.zaurh.sparrow.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.zaurh.sparrow.domain.repository.AuthRepository
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.presentation.account.user.state.UserDataState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


data class SignInState(
    val loading: Boolean = false,
    val error: String? = null
)

data class SignUpState(
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn = _isSignedIn.asStateFlow()

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _firebaseUserState = MutableStateFlow<FirebaseUser?>(null)
    val firebaseUserState = _firebaseUserState.asStateFlow()

    private val _currentUserState = MutableStateFlow(UserDataState())
    val currentUserState = _currentUserState.asStateFlow()


    init {
        authenticate()
    }

    fun signUp(email: String, password: String, confirmPassword: String, onSuccess: () -> Unit) {
        _signUpState.update { it.copy(loading = true) }
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _signUpState.update { it.copy(loading = false, error = "Please fill all fields.") }
            return
        }
        if (password != confirmPassword) {
            _signUpState.update { it.copy(loading = false, error = "Passwords do not match.") }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signUp(email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _signUpState.update { it.copy(loading = false, error = result.message) }
                    }

                    is Resource.Loading -> {
                        _signUpState.update { it.copy(loading = true) }
                    }

                    is Resource.Success -> {
                        _signUpState.update { it.copy(loading = false, error = "") }
                        _firebaseUserState.value = result.data
                        _isSignedIn.value = true
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                        getCurrentUser()

                    }
                }
            }
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        _signInState.update { it.copy(loading = true) }
        if (email.isBlank() || password.isBlank()) {
            _signInState.update {
                it.copy(
                    loading = false,
                    error = "Please fill email and password."
                )
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signIn(email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _signInState.update { it.copy(loading = false, error = result.message) }
                    }

                    is Resource.Loading -> {
                        _signInState.update { it.copy(loading = true) }
                    }

                    is Resource.Success -> {
                        _signInState.update { it.copy(loading = false, error = "") }
                        _firebaseUserState.value = result.data
                        _isSignedIn.value = true
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                        getCurrentUser()

                    }
                }
            }
        }
    }

    private fun authenticate() {
        authRepository.addAuthStateListener { firebaseUser ->
            _firebaseUserState.value = firebaseUser
            _isSignedIn.value = firebaseUser != null
            firebaseUser?.uid?.let {
                getCurrentUser()
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserData(firebaseUserState.value?.uid ?: "").collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _currentUserState.update { it.copy(data = result.data) }
                    }
                }
            }
        }
    }


    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signOut()
            _isSignedIn.value = false
            _firebaseUserState.value = null
            _currentUserState.value = UserDataState()
        }
    }

}