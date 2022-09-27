package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.repository.UserRepository
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.utils.MyUtility.resource
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    // get authtoken from preference
    val authToken = userRepository.authToken

    // login or a register user
    fun userLoginRegister(
        user: User, selectedOption: String
    ) {
        viewModelScope.launch {
            if (selectedOption == resource.getString(R.string.login)) {
                userRepository.userLogin(user)
            } else {
                userRepository.userRegister(user)
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
