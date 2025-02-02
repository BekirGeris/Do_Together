package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.*
import com.example.dotogether.model.Target
import com.example.dotogether.model.request.*
import com.example.dotogether.model.response.*
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScoped
class RemoteRepositoryImpl @Inject constructor(private val repository: RemoteRepository) {

    suspend fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return generateFlow { repository.login(loginRequest) }
    }

    suspend fun register(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>> {
        return generateFlow { repository.register(registerRequest) }
    }

    suspend fun updateUser(updateUserRequest: UpdateUserRequest): Flow<Resource<User>> {
        return generateFlow { repository.updateUser(updateUserRequest) }
    }

    suspend fun createReels(createReelsRequest: CreateReelsRequest): Flow<Resource<Reels>> {
        return generateFlow { repository.createReels(createReelsRequest) }
    }

    suspend fun createTarget(createTargetRequest: CreateTargetRequest): Flow<Resource<Target>> {
        return generateFlow { repository.createTarget(createTargetRequest) }
    }

    suspend fun updateTarget(targetId: Int, updateTargetRequest: UpdateTargetRequest): Flow<Resource<Target>> {
        return generateFlow { repository.updateTarget(targetId, updateTargetRequest) }
    }

    suspend fun updatePassword(updatePasswordRequest: UpdatePasswordRequest): Flow<Resource<User>> {
        return generateFlow { repository.updatePassword(updatePasswordRequest) }
    }

    suspend fun forgotPassword(forgetPasswordRequest: ForgetPasswordRequest): Flow<Resource<String>> {
        return generateFlow { repository.forgotPassword(forgetPasswordRequest) }
    }

    suspend fun forgotPasswordVerify(forgetPasswordVerifyRequest: ForgetPasswordVerifyRequest): Flow<Resource<String>> {
        return generateFlow { repository.forgotPasswordVerify(forgetPasswordVerifyRequest) }
    }

    suspend fun newChat(newChatRequest: NewChatRequest): Flow<Resource<ChatResponse>> {
        return generateFlow { repository.newChat(newChatRequest) }
    }

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): Flow<Resource<SendMessageResponse>> {
        return generateFlow { repository.sendMessage(sendMessageRequest) }
    }

    suspend fun getAllTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getAllTargets() }
    }

    suspend fun getNextAllTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getNextAllTargets(pageNo) }
    }

    suspend fun getMyJoinedTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getMyJoinedTargets() }
    }

    suspend fun getNextMyJoinedTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getNextMyJoinedTargets(pageNo) }
    }

    suspend fun getMyLikeTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getMyLikeTargets() }
    }

    suspend fun getNextMyLikeTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getNextMyLikeTargets(pageNo) }
    }

    suspend fun getMyDoneTargets(): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getMyDoneTargets() }
    }

    suspend fun getNextMyDoneTargets(pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getNextMyDoneTargets(pageNo) }
    }

    suspend fun getTargetsWithUserId(userId: Int): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getTargetsWithUserId(userId) }
    }

    suspend fun getNextTargetsWithUserId(userId: Int, pageNo: String): Flow<Resource<Page<Target>>> {
        return generateFlow { repository.getNextTargetsWithUserId(userId, pageNo) }
    }

    suspend fun joinTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.joinTarget(targetId) }
    }

    suspend fun likeTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.likeTarget(targetId) }
    }

    suspend fun unJoinTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.unJoinTarget(targetId) }
    }

    suspend fun unLikeTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.unLikeTarget(targetId) }
    }

    suspend fun getTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.getTarget(targetId) }
    }

    suspend fun deleteTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.deleteTarget(targetId) }
    }

    suspend fun getUser(userId: Int): Flow<Resource<User>> {
        return generateFlow { repository.getUser(userId) }
    }

    suspend fun getFollowers(userId: Int): Flow<Resource<Page<User>>> {
        return generateFlow { repository.getFollowers(userId) }
    }

    suspend fun getFollowings(userId: Int): Flow<Resource<Page<User>>> {
        return generateFlow { repository.getFollowings(userId) }
    }

    suspend fun getNextFollowers(userId: Int, pageNo: String): Flow<Resource<Page<User>>> {
        return generateFlow { repository.getNextFollowers(userId, pageNo) }
    }

    suspend fun getNextFollowings(userId: Int, pageNo: String): Flow<Resource<Page<User>>> {
        return generateFlow { repository.getNextFollowings(userId, pageNo) }
    }

    suspend fun follow(userId: Int): Flow<Resource<User>> {
        return generateFlow { repository.follow(userId) }
    }

    suspend fun unFollow(userId: Int): Flow<Resource<User>> {
        return generateFlow { repository.unFollow(userId) }
    }

    suspend fun getFollowingsReels(): Flow<Resource<ArrayList<User>>> {
        return generateFlow { repository.getFollowingsReels() }
    }

    suspend fun removeReels(reelsId: Int): Flow<Resource<Reels>> {
        return generateFlow { repository.removeReels(reelsId) }
    }

    suspend fun searchUser(searchRequest: SearchRequest): Flow<Resource<ArrayList<User>>> {
        return generateFlow { repository.searchUser(searchRequest) }
    }

    suspend fun searchTarget(searchRequest: SearchRequest): Flow<Resource<ArrayList<Target>>> {
        return generateFlow { repository.searchTarget(searchRequest) }
    }

    suspend fun searchTag(searchRequest: SearchRequest): Flow<Resource<ArrayList<Tag>>> {
        return generateFlow { repository.searchTag(searchRequest) }
    }

    suspend fun searchFollowings(searchRequest: SearchRequest, userId: Int): Flow<Resource<ArrayList<User>>> {
        return generateFlow { repository.searchFollowings(searchRequest, userId) }
    }

    suspend fun searchFollowers(searchRequest: SearchRequest, userId: Int): Flow<Resource<ArrayList<User>>> {
        return generateFlow { repository.searchFollowers(searchRequest, userId) }
    }

    suspend fun searchMyChats(searchRequest: SearchRequest): Flow<Resource<List<ChatResponse>>> {
        return generateFlow { repository.searchMyChats(searchRequest) }
    }

    suspend fun myChats(): Flow<Resource<List<ChatResponse>>> {
        return generateFlow { repository.myChats() }
    }

    suspend fun getChat(chatId: String): Flow<Resource<ChatResponse>> {
        return generateFlow { repository.getChat(chatId) }
    }

    suspend fun changeNotifyChat(chatId: String): Flow<Resource<ChatResponse>> {
        return generateFlow { repository.changeNotifyChat(chatId) }
    }

    suspend fun resetUnreadCountChat(chatId: String) {
        repository.resetUnreadCountChat(chatId)
    }

    suspend fun doneTarget(targetId: Int): Flow<Resource<Target>> {
        return generateFlow { repository.doneTarget(targetId) }
    }

    suspend fun getMyUserFromRemote(): Flow<Resource<User>> {
        return generateFlow { repository.getMyUserFromRemote() }
    }

    suspend fun deleteMyAccount(): Flow<Resource<User>> {
        return generateFlow { repository.deleteMyAccount() }
    }

    suspend fun getAllNotifications(): Flow<Resource<Page<Notification>>> {
        return generateFlow { repository.getAllNotifications() }
    }

    suspend fun getNextAllNotifications(pageNo: String): Flow<Resource<Page<Notification>>> {
        return generateFlow { repository.getNextAllNotifications(pageNo) }
    }

    suspend fun notificationsReadAll(): Flow<Resource<Any>> {
        return generateFlow { repository.notificationsReadAll() }
    }

    suspend fun getActions(targetId: Int): Flow<Resource<ArrayList<Action>>> {
        return generateFlow { repository.getActions(targetId) }
    }

    suspend fun getMembers(targetId: Int): Flow<Resource<Page<User>>> {
        return generateFlow { repository.getMembers(targetId) }
    }

    suspend fun getNextMembers(targetId: Int, pageNo: String): Flow<Resource<Page<User>>> {
        return generateFlow { repository.getNextMembers(targetId, pageNo) }
    }

    suspend fun searchMembers(searchRequest: SearchRequest, targetId: Int): Flow<Resource<ArrayList<User>>> {
        return generateFlow { repository.searchMembers(searchRequest, targetId) }
    }

    suspend fun removeUserFromTarget(targetId: Int, userId: Int): Flow<Resource<User>> {
        return generateFlow { repository.removeUserFromTarget(targetId, userId) }
    }

    suspend fun getAllDiscover(): Flow<Resource<ArrayList<Discover>>> {
        return generateFlow { repository.getAllDiscover() }
    }

    suspend fun updateTargetSettings(targetId: Int, updateTargetSettingsRequest: UpdateTargetSettingsRequest) : Flow<Resource<Target>> {
        return generateFlow { repository.updateTargetSettings(targetId, updateTargetSettingsRequest) }
    }

    suspend fun sendReport(targetId: Int, reportRequest: ReportRequest): Flow<Resource<ReportResponse>> {
        return generateFlow { repository.sendReport(targetId, reportRequest) }
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
                if (e is HttpException) {
                    val response = e.response()
                    val errorBody = response?.errorBody()?.string() ?: "Unknown error occurred"
                    val errorMessage = try {
                        val errorJson = JSONObject(errorBody)
                        errorJson.getString("message")
                    } catch (ex: Exception) {
                        "Unknown error occurred"
                    }
                    emit(Resource.Error(Constants.Status.FAILED, errorMessage))
                } else {
                    emit(Resource.Error(Constants.Status.FAILED, "Error: ${e.localizedMessage}"))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}