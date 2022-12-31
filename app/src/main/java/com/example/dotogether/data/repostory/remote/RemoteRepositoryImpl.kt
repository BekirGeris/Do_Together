package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.Connection
import com.example.dotogether.model.Target
import com.example.dotogether.model.request.CreateTargetRequest
import com.example.dotogether.model.request.LoginRequest
import com.example.dotogether.model.request.RegisterRequest
import com.example.dotogether.model.Page
import com.example.dotogether.model.User
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import com.example.dotogether.util.Constants
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
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED,"Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun register(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.register(registerRequest)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createTarget(createTargetRequest: CreateTargetRequest): Flow<Resource<Target>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.createTarget(createTargetRequest)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllTargets(): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getAllTargets()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNextAllTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getNextAllTargets(pageNo)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMyJoinedTargets(): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getMyJoinedTargets()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNextMyJoinedTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getNextMyJoinedTargets(pageNo)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMyLikeTargets(): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getMyLikeTargets()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNextMyLikeTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getNextMyLikeTargets(pageNo)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMyDoneTargets(): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getMyDoneTargets()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNextMyDoneTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getNextMyDoneTargets(pageNo)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMyTargets(): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getMyTargets()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNextMyTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getNextMyTargets(pageNo)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun joinTarget(targetId: Int): Flow<Resource<Target>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.joinTarget(targetId)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun likeTarget(targetId: Int): Flow<Resource<Target>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.likeTarget(targetId)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun unJoinTarget(targetId: Int): Flow<Resource<Target>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.unJoinTarget(targetId)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun unLikeTarget(targetId: Int): Flow<Resource<Target>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.unLikeTarget(targetId)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTarget(targetId: Int): Flow<Resource<Target>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getTarget(targetId)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUser(userId: Int): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getUser(userId)
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowers(): Flow<Resource<Page<Connection>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getFollowers()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowings(): Flow<Resource<Page<Connection>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = repository.getFollowings()
                if (result.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, result.message, result.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, result.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }
}