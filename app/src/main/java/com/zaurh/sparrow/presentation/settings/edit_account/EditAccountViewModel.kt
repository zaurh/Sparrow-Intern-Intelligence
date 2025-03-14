package com.zaurh.sparrow.presentation.settings.edit_account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.presentation.settings.edit_account.state.UpdateUserState
import com.zaurh.sparrow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _usernameValue = MutableStateFlow("")
        val usernameValue = _usernameValue.asStateFlow()

    private val _bioValue = MutableStateFlow("")
    val bioValue = _bioValue.asStateFlow()

    private val _updateUserState = MutableStateFlow(UpdateUserState())
        val updateUserState = _updateUserState.asStateFlow()


    fun updateUsernameValue(value: String){
        _usernameValue.value = value
    }

    fun updateBioValue(value: String){
        _bioValue.value = value
    }

    fun updateUserData(
        userData: UserData,
        onSuccess: () -> Unit
    ){
        viewModelScope.launch {
            userRepository.updateUserData(userData).collect { result ->
                when(result){
                    is Resource.Error -> {
                        _updateUserState.value = UpdateUserState(error = result.message)
                    }
                    is Resource.Loading -> {
                        _updateUserState.value = UpdateUserState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _updateUserState.update { it.copy(isLoading = false, error = "") }
                        withContext(Dispatchers.Main){
                            onSuccess()
                        }
                    }
                }
            }
        }
    }

    fun uploadImage(
        file: File,
        context: Context,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.uploadImage(file, context).collect { result ->
                when (result) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> { result.data?.let { onSuccess(it) } }
                }
            }
        }
    }



}