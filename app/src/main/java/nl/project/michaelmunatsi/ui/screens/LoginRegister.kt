package nl.project.michaelmunatsi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.model.state.FormState
import nl.project.michaelmunatsi.ui.theme.Purple00
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.UserViewModel

// contains code for the login and register screen
// the form is divided into two sections. First section is the menu with two menu items: login and register
// the second section is the body which contains text fields and buttons
object LoginRegister {
    private lateinit var successErrorMessageLabel: MutableState<String>
    private lateinit var labelTextColor: MutableState<Color>
    private lateinit var password: MutableState<String>
    private lateinit var username: MutableState<String>
    private lateinit var selectedOption: MutableState<String>
    private lateinit var passwordFieldIsFocused: MutableState<Boolean>

    @Composable
    fun Screen(sharedUserViewModel: UserViewModel, modifier: Modifier = Modifier) {
        selectedOption = remember { mutableStateOf(resource.getString(R.string.login)) }
        val onSelectionChange = { text: String ->
            selectedOption.value = text
        }
        // form menu options
        val options = listOf(
            R.string.login, R.string.register
        )

        // form header with menu that contains login and register menu items
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.14f)
                        .background(Purple00)
                ) {
                    // menu item length: to each to cover 50% of the form width
                    var length = 0.5f
                    // display menu item text: login and register
                    options.forEach { text ->
                        if (options.indexOf(text) == 1) {
                            length = 1f
                        }
                        Box(modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth(length)
                            .clickable {
                                onSelectionChange(resource.getString(text))
                                sharedUserViewModel.onFormEvent(FormState.ToggleForm)
                            }
                            // menu item background colors
                            .background(
                                getMenuBackgroundColor(selected = stringResource(id = text) == selectedOption.value)
                            ), contentAlignment = Center) {
                            Text(
                                text = stringResource(id = text),
                                fontSize = dimen.sp_20,
                                color = getMenuTextColor(stringResource(id = text) == selectedOption.value),
                                modifier = modifier
                            )
                        }
                    }
                }
                LoginRegisterForm(sharedUserViewModel, selectedOption.value)
            }
        }
    }

    // body of the form with text fields and buttons
    @Composable
    fun LoginRegisterForm(
        userViewModel: UserViewModel, selectedOption: String, modifier: Modifier = Modifier
    ) {
        val maxChar = 20
        successErrorMessageLabel = remember { mutableStateOf("") }
        username = remember { mutableStateOf("") }
        password = remember { mutableStateOf("") }
        labelTextColor = remember { mutableStateOf(Color.Transparent) }
        var passwordVisibility by remember { mutableStateOf(false) }
        passwordFieldIsFocused = remember {
            mutableStateOf(false)
        }

        HandleFormStateChanges(userViewModel)
        // icons to show or hide password
        val icon = if (passwordVisibility) {
            R.drawable.ic_baseline_visibility_24
        } else {
            R.drawable.ic_baseline_visibility_off_24
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // label to display success or error messages
            Text(
                modifier = modifier.padding(dimen.dp_15),
                text = successErrorMessageLabel.value,
                color = labelTextColor.value
            )
            // username input field
            OutlinedTextField(
                value = username.value,
                onValueChange = { newtText ->
                    if (newtText.trim().length <= maxChar) username.value = newtText.trim()
                    // validate input
                    if (selectedOption == resource.getString(R.string.register)) {
                        userViewModel.onFormEvent(FormState.UserTextChange(newtText))
                    }
                },
                label = { Text(text = stringResource(id = R.string.username)) },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface, fontSize = 18.sp
                ),
            )

            Spacer(modifier = Modifier.height(dimen.dp_15))
            // password input field
            OutlinedTextField(
                modifier = modifier.onFocusChanged {
                    passwordFieldIsFocused.value = it.isFocused
                },
                value = password.value,
                onValueChange = { newtText ->
                    if (newtText.trim().length <= maxChar) password.value = newtText.trim()
                    // validate input
                    if (selectedOption == resource.getString(R.string.register)) {
                        userViewModel.onFormEvent(FormState.PassTextChange(newtText))
                    }
                },
                label = { Text(text = stringResource(id = R.string.password)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = stringResource(
                                id = R.string.password_icon
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                ),
                singleLine = true,
                // hide and show password
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface, fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(dimen.dp_15))
            // login or register button
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (selectedOption == resource.getString(R.string.login)) {
                            userViewModel.onFormEvent(FormState.UserTextChange(username.value))
                            userViewModel.onFormEvent(FormState.PassTextChange(password.value))
                        }

                        if (username.value.length >= 4 && password.value.length >= 8) {
                            userViewModel.userLoginRegister(
                                // submit username and password to the remote saver
                                User(username.value, password.value), selectedOption
                            )
                        }
                    }, modifier = modifier.width(dimen.dp_200)

                ) {
                    Text(text = selectedOption, fontSize = dimen.sp_20)
                }
            }
            Spacer(modifier = Modifier.height(dimen.dp_10))
            if (selectedOption == stringResource(id = R.string.register)) {
                if (passwordFieldIsFocused.value) {
                    ShowPasswordRequirements()
                } else {
                    ShowUsernameRequirements()
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }


    // handle state changes
    @Composable
    private fun HandleFormStateChanges(userViewModel: UserViewModel) {
        val state by userViewModel.formState.collectAsState()
        when (state) {
            is FormState.Error -> {
                // display error
                labelTextColor.value = MaterialTheme.colors.onError
                successErrorMessageLabel.value = (state as FormState.Error).message
            }
            is FormState.ToggleForm -> {
                // clear form when toggling between login and register
                clearFromFields()
                userViewModel.onFormEvent(FormState.Initial)
            }
            is FormState.Success -> {
                clearFromFields()
                userViewModel.onFormEvent(FormState.Initial)
                selectedOption.value = resource.getString(R.string.login)
                labelTextColor.value = MaterialTheme.colors.primaryVariant
                successErrorMessageLabel.value = (state as FormState.Success).message
            }
            else -> {}
        }
    }

    private fun clearFromFields() {
        if (successErrorMessageLabel.value.isNotEmpty()) successErrorMessageLabel.value = ""
        if (password.value.isNotEmpty()) password.value = ""
        if (username.value.isNotEmpty()) username.value = ""
    }

    @Composable
    private fun getMenuBackgroundColor(selected: Boolean): Color {
        if (selected) {
            return MaterialTheme.colors.primary
        }
        return MaterialTheme.colors.surface
    }

    @Composable
    private fun getMenuTextColor(selected: Boolean): Color {
        if (selected) {
            return MaterialTheme.colors.onPrimary
        }
        return MaterialTheme.colors.onSurface
    }

    @Composable
    private fun ShowPasswordRequirements(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.padding(dimen.dp_15)
        ) {
            Text(
                text = stringResource(id = R.string.password_requirements),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.must_contain_1_lower_case_character),
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.must_contain_1_special_character),
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.must_contain_1_upper_case_character),
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.password_must_contain_at_least_8_Character),
                color = MaterialTheme.colors.onSurface
            )
        }
    }

    @Composable
    private fun ShowUsernameRequirements(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.padding(dimen.dp_15)
        ) {
            Text(
                text = stringResource(id = R.string.username_requirements),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )

            Text(
                text = stringResource(id = R.string.username_must_contain_letters_numbers_only).replace(
                    stringResource(id = R.string.username), ""
                ), color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string.username_must_contain_at_least_4_Character).replace(
                    stringResource(id = R.string.username), ""
                ), color = MaterialTheme.colors.onSurface
            )
        }
    }
}
