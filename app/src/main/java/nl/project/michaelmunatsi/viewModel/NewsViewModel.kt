package nl.project.michaelmunatsi.viewModel

import androidx.compose.ui.input.key.Key.Companion.I
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.data.NewsArticlePager
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsArticleMapper: NewsArticleMapper,
) : ViewModel() {
    // save list position
    var lazyColumnScrollPosition = 0

    // global variable to save user clicked article
    var selectedArticle: NewsArticle? = null

    // get user liked articles
    var likedArticle = getArticles()

    // get all articles
    var articlePagingData = getArticles()

    // is user navigating to the favourite screen
    var isFavouriteScreen = false

    private fun getArticles(): Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(pageSize = 20), initialKey = 0
        ) {
            NewsArticlePager(
                newsRepository, newsArticleMapper, isFavouriteScreen
            )
        }.flow.cachedIn(viewModelScope)
    }

    fun saveClickedArticle(article: NewsArticle) {
        selectedArticle = article
    }

    //
    fun likeDislike(articleId: Int, isLike: Boolean) {
        viewModelScope.launch {
            newsRepository.likeDislikeAPi(articleId, isLike)

            if (isFavouriteScreen) {
                likedArticle = updateIsLike(articleId, isLike, likedArticle)
            } else {
                articlePagingData = updateIsLike(articleId, isLike, articlePagingData)
            }
        }
    }

    private fun updateIsLike(
        articleId: Int, isLike: Boolean, pagingData: Flow<PagingData<NewsArticle>>
    ): Flow<PagingData<NewsArticle>> {
        return pagingData.map { paging ->
            paging.map {
                if (it.Id == articleId) {
                    it.IsLiked = isLike
                }
                it
            }
        }
    }

    fun refreshLikedArticles() {
        viewModelScope.launch {
            newsRepository.likedArticles()
        }
    }
}