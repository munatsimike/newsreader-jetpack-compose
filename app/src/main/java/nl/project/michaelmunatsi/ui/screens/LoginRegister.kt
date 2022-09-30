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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.model.User
import nl.project.michaelmunatsi.model.state.FormState
import nl.project.michaelmunatsi.ui.theme.Purple500
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.UserViewModel

object LoginRegister {
    private lateinit var successErrorMessageLabel: MutableState<String>
    private lateinit var labelTextColor: MutableState<Color>
    private lateinit var password: MutableState<String>
    private lateinit var username: MutableState<String>
    private lateinit var selectedOption: MutableState<String>

    @Composable
    fun Screen(sharedUserViewModel: UserViewModel,modifier: Modifier = Modifier) {
        selectedOption = remember { mutableStateOf(resource.getString(R.string.login)) }
        val onSelectionChange = { text: String ->
            selectedOption.value = text
        }

        val options = listOf(
            R.string.login, R.string.register
        )

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.14f)
                        .fillMaxHeight()
                ) {
                    var length = 0.5f
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
                            .background(
                                if (stringResource(id = text) == selectedOption.value) {
                                    Purple500
                                } else {
                                    Color.Gray
                                }
                            ), contentAlignment = Center) {
                            Text(
                                text = stringResource(id = text),
                                fontSize = dimen.sp_20,
                                color = Color.White,
                                modifier = modifier
                            )
                        }
                    }
                }
                LoginRegisterForm(sharedUserViewModel, selectedOption.value)
            }
        }
    }

    @Composable
    fun LoginRegisterForm(
        userViewModel: UserViewModel, selectedOption: String, modifier: Modifier = Modifier
    ) {
        successErrorMessageLabel = remember { mutableStateOf("") }
        username = remember { mutableStateOf("") }
        password = remember { mutableStateOf("") }
        labelTextColor = remember { mutableStateOf(Color.Red) }
        var passwordVisibility by remember { mutableStateOf(false) }

        HandleFormStateChanges(userViewModel)

        val icon = if (passwordVisibility) {
            R.drawable.ic_baseline_visibility_24
        } else {
            R.drawable.ic_baseline_visibility_off_24
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = modifier.padding(dimen.dp_15),
                text = successErrorMessageLabel.value,
                color = labelTextColor.value
            )
            OutlinedTextField(value = username.value, onValueChange = { newtText ->
                username.value = newtText
                userViewModel.onFormEvent(FormState.UserTextChange(newtText))
            }, label = { Text(text = stringResource(id = R.string.username)) })

            Spacer(modifier = Modifier.height(dimen.dp_15))
            OutlinedTextField(
                value = password.value,
                onValueChange = { newtText ->
                    password.value = newtText
                    userViewModel.onFormEvent(FormState.PassTextChange(newtText))
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
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(dimen.dp_15))
            Row(
                modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        userViewModel.userLoginRegister(
                            User(username.value, password.value), selectedOption
                        )
                    }, modifier = modifier.width(dimen.dp_200)
                ) {
                    Text(text = selectedOption, fontSize = dimen.sp_20)
                }
            }
        }
    }

    @Composable
    private fun HandleFormStateChanges(userViewModel: UserViewModel) {
        val state by userViewModel.formState.collectAsState()
        when (state) {
            is FormState.Error -> {
                successErrorMessageLabel.value = (state as FormState.Error).message
            }
            is FormState.ToggleForm -> {
                clearFromFields()
            }
            is FormState.Success -> {
                clearFromFields()
                selectedOption.value = resource.getString(R.string.login)
                labelTextColor.value = Color.Green
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
}
