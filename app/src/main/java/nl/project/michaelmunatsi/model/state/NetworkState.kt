package nl.project.michaelmunatsi.model.state

import nl.project.michaelmunatsi.model.NewsArticle

sealed class NetworkState {
    class Error(val error: String?) : NetworkState()
    class Success(val data: List<NewsArticle>) : NetworkState()
    object NotLoading : NetworkState()
    object Loading : NetworkState()
}