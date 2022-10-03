package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.NewsArticle
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {
    // save list position
    var lazyColumnScrollPosition = 0

    // global variable to save user clicked article
    var selectedArticle: NewsArticle? = null

    // is user navigating to the favourite screen
    var isFavouriteScreen = false
    val getAllArticles = newsRepository.getAllArticles()

    private val _likedArticles = MutableStateFlow<PagingData<NewsArticle>>(PagingData.empty())
    val likedArticle = _likedArticles

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            newsRepository.likedArticles().cachedIn(viewModelScope).collect {
                _likedArticles.emit(it)

            }
        }
    }

    fun saveClickedArticle(article: NewsArticle) {
        selectedArticle = article
    }

    fun likeDislike(articleId: Int, isLike: Boolean) {
        viewModelScope.launch {
            newsRepository.likeDislikeAPi(articleId, isLike)
            getArticles()
        }
    }
}