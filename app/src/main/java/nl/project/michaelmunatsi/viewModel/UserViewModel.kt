package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.repository.UserRepository
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.model.User.Companion.validatePassword
import nl.project.michaelmunatsi.model.User.Companion.validateUsername
import nl.project.michaelmunatsi.model.state.FormState
import nl.project.michaelmunatsi.model.state.UserState
import nl.project.michaelmunatsi.utils.Coroutines
import nl.project.michaelmunatsi.utils.MyUtility.onErrorMessage
import nl.project.michaelmunatsi.utils.MyUtility.onFailureMessage
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

    private val _formState = MutableStateFlow<FormState>(FormState.Initial)
    val formState: StateFlow<FormState> = _formState

    init {
        updateUserState()
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
            FormState.Initial -> {
                _formState.value = FormState.Initial
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
                    loginUser(user)
                } else {
                    registerUser(user)
                }
            }
        } else {
            _formState.value =
                FormState.Error(resource.getString(R.string.Invalid_username_password))
        }
    }

    private fun loginUser(user: User) {
        viewModelScope.launch {
            userRepository.userLogin(user)
                .onSuccess {
                    Coroutines.io {
                        userRepository.saveAuthToken(data)
                    }
                }.onError {
                    try {
                        onErrorMessage(statusCode.code, message())
                    } catch (e: Exception) {
                        _formState.value = FormState.Error(e.message as String)
                    }
                }.onFailure {
                    try {
                        if (message().contains("code=401")) {
                            _formState.value =
                                FormState.Error(resource.getString(R.string.invalid_credentials))
                        } else {
                            onFailureMessage()
                        }
                    } catch (e: Exception) {
                        _formState.value = FormState.Error(e.message as String)
                    }
                }
        }
    }

    private fun registerUser(user: User) {
        viewModelScope.launch {
            userRepository.userRegister(user)
                .onSuccess {
                    _formState.value = FormState.Success(data.Message)
                }.onError {
                    try {
                        onErrorMessage(statusCode.code, message())
                    } catch (e: Exception) {
                        _formState.value = FormState.Error(e.message as String)
                    }
                }.onFailure {
                    try {
                        onFailureMessage()
                    } catch (e: Exception) {
                        _formState.value = FormState.Error(e.message as String)
                    }
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
