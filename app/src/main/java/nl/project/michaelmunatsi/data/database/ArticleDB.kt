package nl.project.michaelmunatsi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.project.michaelmunatsi.data.database.dao.ArticleDao
import nl.project.michaelmunatsi.data.database.dao.RemoteKeysDao
import nl.project.michaelmunatsi.model.ArticleRemoteKeys
import nl.project.michaelmunatsi.model.NewsArticle

@Database(
    entities = [NewsArticle::class, ArticleRemoteKeys::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract val newsDao: ArticleDao
    abstract val RemoteKeysDao: RemoteKeysDao
}

private lateinit var instance: ArticleDB

fun createDB(context: Context): ArticleDB {
    synchronized(ArticleDB::class.java) {
        if (!::instance.isInitialized) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                ArticleDB::class.java,
                "article_db.db"
            ).build()
        }
    }
    return instance
}
