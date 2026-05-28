package data.repository

import data.model.*
import network.RetrofitClient
import utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import retrofit2.HttpException
import java.io.IOException

class AuthRepository {

    @OptIn(InternalSerializationApi::class)
    suspend fun login(login: String, password: String): Result<UserDto> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.api.login(LoginRequest(login, password))
            TokenManager.token = response.token
            Result.success(response.user)
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка авторизации: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Нет соединения с сервером"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun register(request: RegisterRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            RetrofitClient.api.register(request)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка регистрации: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Нет соединения с сервером"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        try {
            val users = RetrofitClient.api.getUsers()
            Result.success(users)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                TokenManager.clear()
                Result.failure(Exception("Сессия истекла"))
            } else {
                Result.failure(Exception("Ошибка загрузки: ${e.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Нет соединения с сервером"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getGroups(): Result<List<GroupDto>> = withContext(Dispatchers.IO) {
        try {
            val groups = RetrofitClient.api.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}