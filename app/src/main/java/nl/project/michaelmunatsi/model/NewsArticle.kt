package nl.project.michaelmunatsi.model

class NewsArticle(
    var Id: Int,
    var Categories: List<Category>,
    var Feed: Int,
    var Image: String,
    var IsLiked: Boolean,
    var PublishDate: String,
    var Related: List<String>,
    var Summary: String,
    var Title: String,
    var Url: String
)
