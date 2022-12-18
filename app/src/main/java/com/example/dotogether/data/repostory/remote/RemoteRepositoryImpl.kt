package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.request.LoginRequest
import com.example.dotogether.model.request.RegisterRequest
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityScoped
class RemoteRepositoryImpl @Inject constructor(private val repository: RemoteRepository) {

    suspend fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.login(loginRequest)
                if (result.success) {
                    emit(Resource.Success(result.message, result.data))
                } else {
                    emit(Resource.Error(result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun regitser(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.register(registerRequest)
                if (result.success) {
                    emit(Resource.Success(result.message, result.data))
                } else {
                    emit(Resource.Error(result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }
}