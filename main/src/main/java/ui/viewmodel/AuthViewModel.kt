@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.model.*
import data.repository.AuthRepository
import ui.state.AuthUiState
import utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        if (TokenManager.isLoggedIn) {
            loadUsers()
        }
        loadGroups()
    }

    fun setLogin(value: String) = _state.update { it.copy(login = value, error = null) }
    fun setPassword(value: String) = _state.update { it.copy(password = value, error = null) }
    fun setFirstName(value: String) = _state.update { it.copy(firstName = value) }
    fun setLastName(value: String) = _state.update { it.copy(lastName = value) }
    fun setMiddleName(value: String) = _state.update { it.copy(middleName = value) }
    fun setBirthDate(value: String) = _state.update { it.copy(birthDate = value) }
    fun setGender(value: String) = _state.update { it.copy(gender = value) }
    fun setSelectedGroupId(value: Int?) = _state.update { it.copy(selectedGroupId = value) }
    fun setEmail(value: String) = _state.update { it.copy(email = value) }
    fun setPhoneNumber(value: String) = _state.update { it.copy(phoneNumber = value) }

    fun login() {
        if (!_state.value.isLoginValid) {
            _state.update { it.copy(error = "Заполните логин и пароль (мин. 6 символов)") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.login(_state.value.login, _state.value.password)
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false, currentUser = user, successMessage = "Вход выполнен!") }
                    loadUsers()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message ?: "Ошибка входа") }
                }
        }
    }

    fun register() {
        if (!_state.value.isRegisterValid) {
            _state.update { it.copy(error = "Заполните все обязательные поля") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val person = PersonDto(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                middleName = _state.value.middleName.takeIf { it.isNotBlank() },
                birthDate = _state.value.birthDate,
                gender = _state.value.gender,
                groupId = _state.value.selectedGroupId!!
            )
            val request = RegisterRequest(
                login = _state.value.login,
                password = _state.value.password,
                email = _state.value.email,
                phoneNumber = _state.value.phoneNumber.takeIf { it.isNotBlank() },
                roleId = 1,
                authAllowed = true,
                person = person
            )
            repository.register(request)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, successMessage = "Регистрация успешна! Теперь войдите.") }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message ?: "Ошибка регистрации") }
                }
        }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups -> _state.update { it.copy(groups = groups) } }
                .onFailure { error -> _state.update { it.copy(error = "Не удалось загрузить группы: ${error.message}") } }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .onSuccess { users -> _state.update { it.copy(users = users) } }
                .onFailure { error -> _state.update { it.copy(error = error.message ?: "Ошибка загрузки пользователей") } }
        }
    }

    fun logout() {
        repository.logout()
        _state.update { it.copy(currentUser = null, users = emptyList(), login = "", password = "") }
    }

    fun clearError() = _state.update { it.copy(error = null) }
    fun clearSuccess() = _state.update { it.copy(successMessage = null) }
    fun refreshUsers() { if (TokenManager.isLoggedIn) loadUsers() }
}