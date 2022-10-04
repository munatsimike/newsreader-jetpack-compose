package nl.project.michaelmunatsi.ui.screens

import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.utils.MyUtility.resource
import retrofit2.HttpException
import java.io.IOException

// This class contains common code for the main and favorite screen
// e.g Displays lazy list of articles and liked articles,
// displays errors from network and paging source
abstract class BaseScreen {

    fun errorMsg(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> {
                resource.getString(R.string.No_internet_access)
            }
            is HttpException -> {
                resource.getString(R.string.user_not_logged_in)
            }
            else -> {
                throwable.message.toString()
            }
        }
    }
}