package nl.project.michaelmunatsi.model.state

sealed class FormState: State {
    object Initial: FormState()
    object ToggleForm: FormState()
    data class PassTextChange(val newText: String): FormState()
    data class UserTextChange(val newText: String): FormState()
    data class Error(val message: String): FormState()
    data class Success(val message: String): FormState()
}