package network

import data.model.*
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.*

private val data: Any = TODO()

interface ApiService {
    @OptIn(InternalSerializationApi::class)
    @GET("groups")
    suspend fun getGroups(): List<GroupDto>

    @OptIn(InternalSerializationApi::class)
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Unit

    @OptIn(InternalSerializationApi::class)
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @OptIn(InternalSerializationApi::class)
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}