package com.aeterna.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.WorkManager
import com.aeterna.core.data.AuthRepositoryImpl
import com.aeterna.core.data.YouTubeMusicRepositoryImpl
import com.aeterna.core.data.cache.CacheManager
import com.aeterna.core.domain.AuthRepository
import com.aeterna.core.domain.YouTubeMusicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindYouTubeMusicRepository(
            youtubeMusicRepositoryImpl: YouTubeMusicRepositoryImpl
    ): YouTubeMusicRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCacheManager(
            @ApplicationContext context: Context,
            workManager: WorkManager
    ): CacheManager {
        return CacheManager(context, workManager)
    }
}
