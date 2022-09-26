package nl.project.michaelmunatsi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import nl.project.michaelmunatsi.data.NewsArticlePager
import nl.project.michaelmunatsi.model.Category
import nl.project.michaelmunatsi.model.NewsArticle
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val pager: NewsArticlePager) : ViewModel() {
    private val initKey = 0
    var articles = Pager(config = PagingConfig(pageSize = 20), initialKey = initKey) {
        pager
    }.flow.cachedIn(viewModelScope)

    fun getArticle(articleId: Int): NewsArticle {
//        articles.collectLatest { pagingData ->
//            pagingData
//        }
        return article()
    }

    private fun article(): NewsArticle {
        return NewsArticle(
            134069,
            listOf(Category(4270, "Sport"), Category(4271, "Wielrennen")),
            3,
            "https://media.nu.nl/m/k3zx972ap9ap_sqr256.jpg/van-der-breggen-en-blaak-kondigen-afscheid-aan-als-wielrenster.jpg",
            false,
            "2020-05-10T15:14:07",
            listOf(),
            "Anna van der Breggen en Chantal Blaak hebben zondag hun afscheid aangekondigd als wielrenster. De wereldtoppers stoppen over respectievelijk anderhalf en twee jaar en gaan daarna verder als ploegleidster.",
            "Van der Breggen en Blaak kondigen afscheid aan als wielrenster",
            "https://www.nu.nl/wielrennen/6050336/van-der-breggen-en-blaak-kondigen-afscheid-aan-als-wielrenster.html"
        )
    }
}