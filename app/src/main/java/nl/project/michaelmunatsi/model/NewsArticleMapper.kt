package nl.project.michaelmunatsi.model

import javax.inject.Inject

class NewsArticleMapper @Inject constructor() {
    private fun map(entity: NewsArticleEntity) = with(entity) {
        NewsArticle(
            Id = Id!!,
            Categories = Categories!!,
            Feed = Feed!!,
            Image = Image!!,
            IsLiked = IsLiked!!,
            PublishDate = PublishDate!!,
            Related = Related!!,
            Summary = Summary!!,
            Title = Title!!,
            Url = Url!!
        )
    }

    fun mapList(entity: List<NewsArticleEntity>) = runCatching {
        entity.map(::map)
    }
}