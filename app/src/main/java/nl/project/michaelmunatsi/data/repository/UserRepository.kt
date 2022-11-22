package nl.project.michaelmunatsi.data.repository

import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.project.michaelmunatsi.data.remote.UserApi
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.model.UserRegistrationResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userManager: UserManager
) : BaseRepository(userManager) {

    suspend fun userLogin(
        user: User
    ): ApiResponse<Token> {
        return UserApi.retrofitService.userLogin(user.username, user.password)
    }

    suspend fun userRegister(
        user: User
    ): ApiResponse<UserRegistrationResponse> {
        return UserApi.retrofitService.registerUser(user.username, user.password)
    }

    // save authtoken to dataStore
    suspend fun saveAuthToken(token: Token) {
        withContext(Dispatchers.IO) {
            userManager.saveAuthToken(token)
        }
    }

    suspend fun logout() {
        userManager.deleteAuthToken()
    }
}