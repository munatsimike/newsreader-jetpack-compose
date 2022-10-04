package nl.project.michaelmunatsi.model

import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.utils.MyUtility.resource

// contains coding for validating username and password
data class User(val username: String, val password: String) {

    companion object {
        // validate username on login or registration
        fun validateUsername(text: String): String {
            if (text.length < 8) {
                return resource.getString(R.string.username_must_contain_at_least_8_Character)
            } else if (!text.matches("^[0-9a-zA-Z]+$".toRegex())) {
                return resource.getString(R.string.username_must_contain_letters_numbers_only)
            }
            return resource.getString(R.string.valid)
        }

        // validate password on login or user registration
        fun validatePassword(password: String): String {
            if (password.length < 8) {
                return resource.getString(R.string.password_must_contain_at_least_8_Character)
            } else if (!password.matches(".*[A-Z].*".toRegex())) {
                return resource.getString(R.string.must_contain_1_upper_case_character)
            } else if (!password.matches(".*[a-z].*".toRegex())) {
                return resource.getString(R.string.must_contain_1_lower_case_character)
            } else if (!password.matches(".*[@#*\$%^&+=].*".toRegex())) {
                return resource.getString(R.string.must_contain_1_special_character)+ " [@#*\$%^&+=]"
            }
            return resource.getString(R.string.valid)
        }
    }
}