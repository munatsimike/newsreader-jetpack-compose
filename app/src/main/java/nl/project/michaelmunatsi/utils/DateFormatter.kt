package nl.project.michaelmunatsi.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(localDateTime: String) : String {
    val date = LocalDateTime.parse(localDateTime)
    val pattern = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
    return date.format(pattern)
}