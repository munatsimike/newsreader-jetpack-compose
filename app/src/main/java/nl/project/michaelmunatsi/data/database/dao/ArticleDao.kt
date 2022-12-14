package nl.project.michaelmunatsi.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nl.project.michaelmunatsi.model.NewsArticle

// CRUD operations for the article table
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(article: List<NewsArticle>)

    @Query("SELECT * FROM article ORDER BY Id DESC")
    fun getAllArticles(): PagingSource<Int, NewsArticle>

    @Query("DELETE FROM article")
    suspend fun deleteAllArticles()

    @Query("UPDATE article SET IsLiked = :isLike WHERE Id = :id")
    suspend fun likeDislike(isLike: Int, id: Int)

    @Query("SELECT * FROM article WHERE Id == :id ")
    fun getArticle(id: Int): Flow<NewsArticle>

    @Query("SELECT * FROM article WHERE isLiked == 1 ")
    fun getLikedArticle(): Flow<List<NewsArticle>>
}