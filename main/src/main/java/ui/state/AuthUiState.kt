package ui.state

import data.model.GroupDto
import data.model.UserDto
import kotlinx.serialization.InternalSerializationApi

data class AuthUiState @OptIn(InternalSerializationApi::class) constructor(
    // Экран входа
    val login: String = "",
    val password: String = "",

    // Экран регистрации
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",  // "YYYY-MM-DD"
    val gender: String = "MALE", // "MALE" или "FEMALE"
    val selectedGroupId: Int? = null,
    val email: String = "",
    val phoneNumber: String = "",

    // Общие
    val groups: List<GroupDto> = emptyList(),
    val users: List<UserDto> = emptyList(),
    val currentUser: UserDto? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
) {
    // Валидация формы входа
    val isLoginValid: Boolean
        get() = login.isNotBlank() && password.length >= 6

    // Валидация формы регистрации
    val isRegisterValid: Boolean
        get() = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) &&
                email.contains("@") &&
                selectedGroupId != null &&
                login.isNotBlank() &&
                password.length >= 6
}