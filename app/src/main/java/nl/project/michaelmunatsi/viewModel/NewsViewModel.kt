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
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.Token
import nl.project.michaelmunatsi.model.state.NetworkState
import nl.project.michaelmunatsi.utils.MyUtility.onErrorMessage
import nl.project.michaelmunatsi.utils.MyUtility.onFailureMessage
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val token = MutableStateFlow(Token(""))
    private var _refresh = MutableLiveData(false)
    private var _networkState = MutableStateFlow<NetworkState>(NetworkState.NotLoading)
    private val _article = MutableStateFlow<NewsArticle?>(null)

    val refresh: LiveData<Boolean> = _refresh
    val networkState: StateFlow<NetworkState> = _networkState
    val allArticles = newsRepository.getAllArticles().cachedIn(viewModelScope)
    val article: StateFlow<NewsArticle?> = _article

    init {
        getToken()
    }

    private fun getToken() {
        viewModelScope.launch {
            newsRepository.authToken.collectLatest {
                if (it != null) {
                    token.value = it
                }
            }
        }
    }

    fun fetchLikedArticles() {
        _networkState.value = NetworkState.Loading
        viewModelScope.launch {
            newsRepository.likedArticles().collectLatest {
                _networkState.value = NetworkState.Success(it)
            }
        }
    }

    fun likeDislike(articleId: Int, isLike: Boolean) {
        viewModelScope.launch {
            newsRepository.likeDislikeAPi(articleId, isLike, token = token.value).onSuccess {
                viewModelScope.launch {
                    newsRepository.likeDislikeRoomDB(isLike, articleId)
                }
            }.onError {
                try {
                    onErrorMessage(statusCode = statusCode.code, message())
                } catch (e: Exception) {
                    _networkState.value = NetworkState.Error(e.message)
                }
            }.onFailure {
                try {
                    onFailureMessage()
                } catch (e: Exception) {
                    _networkState.value = NetworkState.Error(e.message)
                }
            }
        }
    }

    suspend fun fetchArticleById(id: Int) {
        newsRepository.getArticle(id).collectLatest {
            _article.value = it
        }
    }

    fun refresh(refresh: Boolean) {
        _refresh.value = refresh
    }
}