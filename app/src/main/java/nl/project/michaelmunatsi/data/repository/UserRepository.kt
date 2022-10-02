package nl.project.michaelmunatsi.data.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.remote.UserApi
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.utils.Coroutines
import nl.project.michaelmunatsi.utils.MyUtility.resource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userManager: UserManager
) {
    private val _response = MutableStateFlow("")
    val response: StateFlow<String> = _response
    val authToken = userManager.getAuthToken

    suspend fun userLogin(
        user: User
    ) {
        UserApi.retrofitService.userLoginAsync(user.username, user.password)
            .onSuccess {
                Coroutines.io { saveAuthToken(data) }
            }.onError {
                _response.value = message()
            }.onFailure {
                _response.value = resource.getString(R.string.No_internet_access)
            }
    }

    suspend fun userRegister(
        user: User
    ) {
        UserApi.retrofitService.registerUserAsync(user.username, user.password)
            .onSuccess {
                _response.value = data.Message
            }
            .onError {
                _response.value = message()
            }
            .onFailure {
                _response.value = resource.getString(R.string.No_internet_access)
            }
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