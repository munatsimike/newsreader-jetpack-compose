package nl.project.michaelmunatsi.model

data class User(val username: String, val password: String) {

    companion object {
        // validate username on login or registration
        fun validateUsername(text: String): String {
            if (text.length < 8) {
                return "Minimum 8 Character Password"
            } else if (!text.matches("^[0-9a-zA-Z]+$".toRegex())) {
                return "Must contain letters and numbers only"
            }
            return "valid"
        }

        // validate password on login or user registration
        fun validatePassword(password: String) : String{
            if (password.length < 8) {
                return "Minimum 8 Character Password"
            } else if (!password.matches(".*[A-Z].*".toRegex())) {
                return "Must Contain 1 Upper-case Character"
            } else if (!password.matches(".*[a-z].*".toRegex())) {
                return "Must Contain 1 Lower-case Character"
            } else if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
                return "Must Contain 1 Special Character (@#\$%^&+=)"
            }
            return "valid"
        }
    }
}