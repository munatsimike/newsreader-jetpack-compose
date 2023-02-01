package nl.project.michaelmunatsi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.project.michaelmunatsi.data.database.dao.ArticleDao
import nl.project.michaelmunatsi.data.database.dao.RemoteKeyDao
import nl.project.michaelmunatsi.model.NewsArticle
import nl.project.michaelmunatsi.model.RemoteKey

// database has two tables for storing  news articles and remote keys
@Database(
    entities = [NewsArticle::class, RemoteKey::class], version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract val newsDao: ArticleDao
    abstract val remoteKeyDao: RemoteKeyDao
}