package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.Target
import com.example.dotogether.model.Page
import com.example.dotogether.model.Reels
import com.example.dotogether.model.User
import com.example.dotogether.model.request.*
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import com.example.dotogether.model.response.Response
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
        return generateFlow { (repository.login(loginRequest)) }
    }

    suspend fun register(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>> {
        return generateFlow { (repository.register(registerRequest)) }
    }

    suspend fun updateUser(updateUserRequest: UpdateUserRequest): Flow<Resource<User>> {
        return generateFlow { (repository.updateUser(updateUserRequest)) }
    }

    suspend fun createReels(createReelsRequest: CreateReelsRequest): Flow<Resource<Reels>> {
        return generateFlow { (repository.createReels(createReelsRequest)) }
    }

    suspend fun createTarget(createTargetRequest: CreateTargetRequest): Flow<Resource<Target>> {
        return generateFlow { (repository.createTarget(createTargetRequest)) }
    }

    suspend fun getAllTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getAllTargets()) }
    }

    suspend fun getNextAllTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getNextAllTargets(pageNo)) }
    }

    suspend fun getMyJoinedTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getMyJoinedTargets()) }
    }

    suspend fun getNextMyJoinedTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getNextMyJoinedTargets(pageNo)) }
    }

    suspend fun getMyLikeTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getMyLikeTargets()) }
    }

    suspend fun getNextMyLikeTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getNextMyLikeTargets(pageNo)) }
    }

    suspend fun getMyDoneTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getMyDoneTargets()) }
    }

    suspend fun getNextMyDoneTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getNextMyDoneTargets(pageNo)) }
    }

    suspend fun getTargetsWithUserId(userId: Int): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getTargetsWithUserId(userId)) }
    }

    suspend fun getNextTargetsWithUserId(userId: Int, pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { (repository.getNextTargetsWithUserId(userId, pageNo)) }
    }

    suspend fun joinTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { (repository.joinTarget(targetId)) }
    }

    suspend fun likeTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { (repository.likeTarget(targetId)) }
    }

    suspend fun unJoinTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { (repository.unJoinTarget(targetId)) }
    }

    suspend fun unLikeTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { (repository.unLikeTarget(targetId)) }
    }

    suspend fun getTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { (repository.getTarget(targetId)) }
    }

    suspend fun deleteTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { (repository.deleteTarget(targetId)) }
    }

    suspend fun getUser(userId: Int): Flow<Resource<User>> {
        return generateFlow { (repository.getUser(userId)) }
    }

    suspend fun getFollowers(userId: Int): Flow<Resource<Page<User>>> {
        return generateFlow { (repository.getFollowers(userId)) }
    }

    suspend fun getFollowings(userId: Int): Flow<Resource<Page<User>>> {
        return generateFlow { (repository.getFollowings(userId)) }
    }

    suspend fun getNextFollowers(userId: Int, pageNo: String): Flow<Resource<Page<User>>> {
        return generateFlow { (repository.getNextFollowers(userId, pageNo)) }
    }

    suspend fun getNextFollowings(userId: Int, pageNo: String): Flow<Resource<Page<User>>> {
        return generateFlow { (repository.getNextFollowings(userId, pageNo)) }
    }

    suspend fun follow(userId: Int): Flow<Resource<User>> {
        return generateFlow { (repository.follow(userId)) }
    }

    suspend fun unFollow(userId: Int): Flow<Resource<User>> {
        return generateFlow { repository.unFollow(userId) }
    }

    suspend fun getFollowingsReels(): Flow<Resource<ArrayList<User>>> {
        return generateFlow { repository.getFollowingsReels() }
    }

    private suspend fun <T> generateFlow(function: suspend () -> Response<T>): Flow<Resource<T>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = function.invoke()
                if (response.success) {
                    emit(Resource.Success(Constants.Status.SUCCESS, response.message, response.data))
                } else {
                    emit(Resource.Error(Constants.Status.SUCCESS, response.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
    }
}