package nl.project.michaelmunatsi.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Article_remote_keys")
data class ArticleRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)