package nl.project.michaelmunatsi.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Json(name = "Result")
@Entity(tableName = "article")
open class NewsArticle(
    @PrimaryKey(autoGenerate = false)
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
) : Serializable