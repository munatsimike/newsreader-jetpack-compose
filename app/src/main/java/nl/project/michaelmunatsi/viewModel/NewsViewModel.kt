package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.state.NetworkState
import nl.project.michaelmunatsi.utils.MyUtility.getMapperResult
import nl.project.michaelmunatsi.utils.MyUtility.onErrorMessage
import nl.project.michaelmunatsi.utils.MyUtility.onFailureMessage
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsArticleMapper: NewsArticleMapper
) : ViewModel() {

    private val token = MutableStateFlow(Token(""))

    // save list position
    var lazyColumnScrollPosition = 0

    // global variable to save user clicked article
    var selectedArticle: NewsArticle? = null
    val networkState = MutableStateFlow<NetworkState>(NetworkState.NotLoading)

    val allArticles = newsRepository.getAllArticles().cachedIn(viewModelScope)

    init {
        getToken()
    }

    private fun getToken() {
        viewModelScope.launch {
            newsRepository.authToken.collect {
                if (it != null) {
                    token.value = it
                    getLikedArticles()
                }
            }
        }
    }

    private fun getLikedArticles() {
        viewModelScope.launch {
            newsRepository.likedArticles(token.value)
                .onSuccess {
                    networkState.value =
                        NetworkState.Success(data = getMapperResult(newsArticleMapper.mapList(data.Results)))
                }.onError {
                    try {
                        onErrorMessage(statusCode = statusCode.code, message())
                    } catch (e: Exception) {
                        networkState.value = NetworkState.Error(e.message)
                    }
                }.onFailure {
                    networkState.value = NetworkState.Error(message())
                }
        }
    }

    fun saveClickedArticle(article: NewsArticle) {
        selectedArticle = article
    }

    fun likeDislike(articleId: Int, isLike: Boolean) {
        viewModelScope.launch {
            newsRepository.likeDislikeAPi(articleId, isLike, token = token.value).onSuccess {
                viewModelScope.launch {
                    newsRepository.likeDislikeRoomDB(isLike, articleId)
                    getLikedArticles()
                }
            }.onError {
                try {
                    onErrorMessage(statusCode = statusCode.code, message())
                } catch (e: Exception) {
                    networkState.value = NetworkState.Error(e.message)
                }
            }.onFailure {
                try {
                    onFailureMessage()
                } catch (e: Exception) {
                    networkState.value = NetworkState.Error(e.message)
                }
            }
        }
    }

    fun loadFavorites() {
        networkState.value = NetworkState.Loading
        getLikedArticles()
    }
}