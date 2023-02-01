package nl.project.michaelmunatsi.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ArticleDB =
        Room.databaseBuilder(
            context.applicationContext,
            ArticleDB::class.java,
            "article_db.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideArticleDao(articleDB: ArticleDB) = articleDB.newsDao

    @Provides
    @Singleton
    fun provideRemoteKeyDao(articleDB: ArticleDB) = articleDB.remoteKeyDao
}