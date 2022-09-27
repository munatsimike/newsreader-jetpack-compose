package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    // get authtoken from preference
    val authToken = userRepository.authToken

    // login or a register user
    fun userLoginRegister(
        username: String, password: String
    ) {
        viewModelScope.launch {
            if (authToken.equals(null)) {
                userRepository.userRegister(username, password)
            } else {
                userRepository.userLogin(username, password)
            }
        }
    }

    // logout current user
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
