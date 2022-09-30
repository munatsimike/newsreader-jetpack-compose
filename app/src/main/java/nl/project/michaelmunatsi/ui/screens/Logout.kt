package nl.project.michaelmunatsi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.UserViewModel

object Logout {
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        modifier: Modifier = Modifier,

        ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                ) {
                    Text(text = stringResource(id = R.string.welcome), fontSize = dimen.sp_20)
                    Spacer(modifier = modifier.width(dimen.dp_10))
                    Text(text = stringResource(id = R.string.welcome), fontSize = dimen.sp_20)
                }
                Spacer(modifier = modifier.height(dimen.dp_10))
                Button(
                    onClick = { sharedUserViewModel.logout() },
                    modifier = modifier.width(dimen.dp_200)
                ) {
                    Text(text = stringResource(id = R.string.logout), fontSize = dimen.sp_20)
                }
            }
        }
    }
}