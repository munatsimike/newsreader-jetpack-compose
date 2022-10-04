package nl.project.michaelmunatsi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nl.project.michaelmunatsi.model.RemoteKey
// CRUD operations for the remote keys table
@Dao
interface RemoteKeyDao {
    @Query("SELECT * FROM remote_keys")
    suspend fun getRemoteKey(): RemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeys: RemoteKey)

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}