package nl.project.michaelmunatsi.model.state

sealed class UserState {
    object LoggedOut : UserState()
    object LoggedIn : UserState()
}