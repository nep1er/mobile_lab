package data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class PersonDto(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("middleName") val middleName: String?,
    @SerialName("birthDate") val birthDate: String,  // формат: "YYYY-MM-DD"
    @SerialName("gender") val gender: String,         // "MALE" или "FEMALE"
    @SerialName("groupId") val groupId: Int
)