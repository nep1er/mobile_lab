@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package data.repository

import data.model.*
import network.RetrofitClient
import utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AuthRepository {

    suspend fun login(login: String, password: String): Result<UserDto> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.api.login(LoginRequest(login, password))
            TokenManager.token = response.token

            // Сервер не возвращает UserDto, создаём заглушку с логином
            val dummyUser = UserDto(0, login, null, null)
            Result.success(dummyUser)
        } catch (e: HttpException) {
            Result.failure(Exception("Ошибка авторизации: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Нет соединения с сервером"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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