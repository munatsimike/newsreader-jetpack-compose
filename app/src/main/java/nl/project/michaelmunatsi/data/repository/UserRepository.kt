package nl.project.michaelmunatsi.data.repository

import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.project.michaelmunatsi.data.network.UserApi
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.utils.Coroutines
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userManager: UserManager
) {
    val authToken = userManager.getAuthToken

    suspend fun userLogin(
        user: User
    ) {
        UserApi.retrofitService.userLoginAsync(user.username, user.password).onSuccess {
            Coroutines.io { saveAuthToken(data) }
        }.onError {}.onFailure { }
    }

    suspend fun userRegister(
      user: User
    ) {
        UserApi.retrofitService.registerUserAsync(user.username, user.password).onSuccess { }.onError { }
            .onFailure { }
    }

    // save authtoken to dataStore
    private suspend fun saveAuthToken(token: Token) {
        withContext(Dispatchers.IO) {
            userManager.saveAuthToken(token)
        }
    }

    suspend fun logout() {
        userManager.deleteAuthToken()
    }
}