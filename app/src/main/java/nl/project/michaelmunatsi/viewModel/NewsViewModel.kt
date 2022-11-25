package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.R
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.state.NetworkState
import nl.project.michaelmunatsi.utils.MyUtility.getMapperResult
import nl.project.michaelmunatsi.utils.MyUtility.onErrorMessage
import nl.project.michaelmunatsi.utils.MyUtility.onFailureMessage
import nl.project.michaelmunatsi.utils.MyUtility.resource
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository, private val newsArticleMapper: NewsArticleMapper
) : ViewModel() {

    private val token = MutableStateFlow(Token(""))
    private var _refresh = MutableLiveData(false)
    val refresh: LiveData<Boolean> = _refresh
    val networkState = MutableStateFlow<NetworkState>(NetworkState.NotLoading)
    val allArticles = newsRepository.getAllArticles().cachedIn(viewModelScope)

    private val _article = MutableStateFlow(NewsArticle())
    val article: StateFlow<NewsArticle> = _article

    init {
        getToken()
    }

    private fun getToken() {
        viewModelScope.launch {
            newsRepository.authToken.collectLatest {
                if (it != null) {
                    token.value = it
                    getLikedArticles()
                }
            }
        }
    }

    private fun getLikedArticles() {
        viewModelScope.launch {
            newsRepository.likedArticles(token.value).onSuccess {
                networkState.value =
                    NetworkState.Success(data = getMapperResult(newsArticleMapper.mapList(data.Results)))
            }.onError {
                if (statusCode.code == 401) {
                    networkState.value =
                        NetworkState.Error(resource.getString(R.string.user_not_logged_in))
                } else {
                    networkState.value = NetworkState.Error(message())
                }
            }.onFailure {
                networkState.value =
                    NetworkState.Error(resource.getString(R.string.No_internet_access))
            }
        }
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

    suspend fun fetchArticle(id: Int) {
        newsRepository.getArticle(id).collectLatest {
            _article.value = it
        }
    }

    fun refresh(refresh: Boolean) {
        _refresh.value = refresh
    }
}