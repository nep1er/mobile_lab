package data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class UserDto(
    @SerialName("userId") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String?,
    @SerialName("person") val person: PersonDto?
)