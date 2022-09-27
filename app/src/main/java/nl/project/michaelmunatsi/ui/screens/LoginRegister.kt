package nl.project.michaelmunatsi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.ui.theme.Purple500
import nl.project.michaelmunatsi.utils.LocalDim
import nl.project.michaelmunatsi.utils.MyUtility.resource
import nl.project.michaelmunatsi.viewModel.UserViewModel

object LoginRegister {

    @Composable
    fun Screen(modifier: Modifier = Modifier, userViewModel: UserViewModel = hiltViewModel()) {
        val dimen = LocalDim.current
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val errorLabel by remember { mutableStateOf("") }

        var selectedOption by remember {
            mutableStateOf(resource.getString(R.string.login))
        }
        val onSelectionChange = { text: String ->
            selectedOption = text
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
                            }
                            .background(
                                if (stringResource(id = text) == selectedOption) {
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

                Text(modifier = modifier.padding(dimen.dp_10), text = errorLabel)
                OutlinedTextField(value = username, onValueChange = { newtText ->
                    username = newtText

                }, label = { Text(text = stringResource(id = R.string.username)) })

                Spacer(modifier = Modifier.height(dimen.dp_15))
                OutlinedTextField(value = password, onValueChange = { newtText ->
                    password = newtText

                }, label = { Text(text = stringResource(id = R.string.password)) })
                Spacer(modifier = Modifier.height(dimen.dp_15))
                Row(
                    modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { userViewModel.userLoginRegister(username, password) },
                        modifier = modifier
                            .width(dimen.dp_200)
                    ) {
                        Text(text = selectedOption, fontSize = dimen.sp_20)
                    }
                }
            }
        }
    }
}
