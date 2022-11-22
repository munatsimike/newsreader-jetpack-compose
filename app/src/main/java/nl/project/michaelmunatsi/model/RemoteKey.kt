package nl.project.michaelmunatsi.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val nextId: Int?,
) {
    constructor(nextId: Int?) : this(1, nextId)
}