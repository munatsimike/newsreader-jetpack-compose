package nl.project.michaelmunatsi.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Json(name = "Result")
@Entity(tableName = "article")
open class NewsArticle(
    @PrimaryKey(autoGenerate = false)
    var Id: Int = 0,
    var Categories: List<Category> = emptyList(),
    var Feed: Int = 0,
    var Image: String = "",
    var IsLiked: Boolean = false,
    var PublishDate: String = "",
    var Related: List<String> = emptyList(),
    var Summary: String = "",
    var Title: String = "",
    var Url: String = ""
) : Serializable