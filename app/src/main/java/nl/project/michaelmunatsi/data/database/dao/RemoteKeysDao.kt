package nl.project.michaelmunatsi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nl.project.michaelmunatsi.model.ArticleRemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM article_remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id: Int): ArticleRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<ArticleRemoteKeys>)

    @Query("DELETE FROM article_remote_keys")
    suspend fun deleteAllRemoteKeys()
}