package nl.project.michaelmunatsi.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.utils.MyUtility.dimen
import nl.project.michaelmunatsi.viewModel.UserViewModel

// contains code for the logout screen
object Logout {
    @Composable
    fun Screen(
        sharedUserViewModel: UserViewModel,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.20f),
            contentAlignment = Alignment.Center
        ) {
            // logout button
            Button(
                onClick = { sharedUserViewModel.logout() }, modifier = modifier.width(dimen.dp_200)
            ) {
                Text(text = stringResource(id = R.string.logout), fontSize = dimen.sp_20)
            }
        }
    }
}
