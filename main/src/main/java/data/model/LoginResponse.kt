package data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class LoginResponse(
    @SerialName("token") val token: String,
    @SerialName("user") val user: UserDto
)