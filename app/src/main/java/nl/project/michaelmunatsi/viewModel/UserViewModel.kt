package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.remote.updateHeaderToken
import nl.project.michaelmunatsi.data.repository.UserRepository
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.model.User.Companion.validatePassword
import nl.project.michaelmunatsi.model.User.Companion.validateUsername
import nl.project.michaelmunatsi.model.state.FormState
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.utils.MyUtility.resource
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val isValidPassword = MutableLiveData(false)
    private val isValidUsername = MutableLiveData(false)

    // get authtoken from preference
    val authToken = userRepository.authToken

    private var _userState = MutableStateFlow<UserState>(UserState.LoggedOut)
    val userState: StateFlow<UserState> = _userState

    private val apiResponse = userRepository.response
    private val _formState = MutableStateFlow<FormState>(FormState.Initial)
    val formState: StateFlow<FormState> = _formState

    init {
        updateUserState()
        handleApiResponsesMessages()
        updateHeaderToken()
    }

    // update token that will be used  to fetch articles
    private fun updateHeaderToken() {
        viewModelScope.launch {
            authToken.collectLatest {
                updateHeaderToken(it)
            }
        }
    }

    private fun updateUserState() {
        viewModelScope.launch {
            authToken.collectLatest { token ->
                if (token == null) {
                    _userState.value = UserState.LoggedOut
                } else {
                    _userState.value = UserState.LoggedIn
                }
            }
        }
    }

    private fun handleApiResponsesMessages() {
        viewModelScope.launch {
            apiResponse.collectLatest { message ->
                if (message == resource.getString(R.string.user_registered)) {
                    _formState.value = FormState.Success(message)
                } else {
                    _formState.value = FormState.Error(message)
                }
            }
        }
    }

    fun updateUserState(token: Token?) {
        if (token == null) {
            _userState.value = UserState.LoggedOut
        } else {
            _userState.value = UserState.LoggedIn
        }
    }

    fun onFormEvent(state: FormState) {
        when (state) {
            is FormState.UserTextChange -> {
                val result = validateUsername(state.newText)
                if (result != resource.getString(R.string.valid)) {
                    _formState.value = FormState.Error(result)
                } else {
                    isValidUsername.value = true
                    _formState.value = FormState.Error("")
                }
            }
            is FormState.PassTextChange -> {
                val result = validatePassword(state.newText)
                if (result != resource.getString(R.string.valid)) {
                    _formState.value = FormState.Error(result)
                } else {
                    isValidPassword.value = true
                    _formState.value = FormState.Error("")
                }
            }
            is FormState.ToggleForm -> {
                _formState.value = FormState.ToggleForm
            }
            else -> {}
        }
    }

    // login or a register user
    fun userLoginRegister(
        user: User, selectedOption: String
    ) {
        if (isValidPassword.value == true && isValidUsername.value == true) {
            viewModelScope.launch {
                if (selectedOption == resource.getString(R.string.login)) {
                    userRepository.userLogin(user)
                } else {
                    userRepository.userRegister(user)
                }
            }
        } else {
            _formState.value =
                FormState.Error(resource.getString(R.string.Invalid_username_password))
        }
    }

    // logout current user
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
