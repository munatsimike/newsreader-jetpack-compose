package nl.project.michaelmunatsi.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {
    companion object {
        @Provides
        fun provideContext(@ApplicationContext context: Context): Context {
            return context
        }
    }
}
