package nl.project.michaelmunatsi.viewModel

import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nl.project.michaelmunatsi.data.NewsArticlePager
import nl.project.michaelmunatsi.data.repository.NewsRepository
import nl.project.michaelmunatsi.model.Category
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.NewsArticleMapper
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsArticleMapper: NewsArticleMapper,
    private val newsArticlePager: NewsArticlePager
) : ViewModel() {
    private val initKey = 0
    var selectedArticle: NewsArticle? = null
    val pagingData = Pager(
        config = PagingConfig(pageSize = 20), initialKey = initKey
    ) { newsArticlePager }.flow.cachedIn(viewModelScope)

    fun saveClickedArticle(article: NewsArticle) {
        selectedArticle = article
    }

    //
    fun likeDislike(articleId: Int, isLike: Boolean) {
        viewModelScope.launch {
            newsRepository.likeDislikeAPi(articleId, isLike)
        }
        update(articleId, isLike)
    }

    fun refreshLikedArticles() {
        viewModelScope.launch {
            newsRepository.likedArticles()
        }
    }

    private fun update(articleId: Int, isLike: Boolean) {
        pagingData.map { pagingData ->
            pagingData.filter {
                it.Id == articleId
            }
        }
    }

}
