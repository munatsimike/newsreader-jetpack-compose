package nl.project.michaelmunatsi.model

data class NewsArticle(
    val Id: Int,
    val Categories: List<Category>,
    val Feed: Int,
    val Image: String,
    val IsLiked: Boolean,
    val PublishDate: String,
    val Related: List<String>,
    val Summary: String,
    val Title: String,
    val Url: String
)
