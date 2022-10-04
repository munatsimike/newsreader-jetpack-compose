package nl.project.michaelmunatsi.data.repository

abstract class BaseRepository constructor(
    userManager: UserManager
) {
    val authToken = userManager.getAuthToken
}