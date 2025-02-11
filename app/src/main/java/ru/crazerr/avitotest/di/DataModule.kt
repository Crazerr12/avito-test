package ru.crazerr.avitotest.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.crazerr.avitotest.data.local.TrackLocalDataSource
import ru.crazerr.avitotest.data.repository.TrackRepositoryImpl
import ru.crazerr.avitotest.domain.repository.TrackRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    companion object {
        @Singleton
        @Provides
        fun provideTracksLocalDataSource(
            @ApplicationContext context: Context
        ): TrackLocalDataSource {
            return TrackLocalDataSource(context = context)
        }
    }

    @Singleton
    @Binds
    abstract fun bindTrackRepository(
        tracksRepositoryImpl: TrackRepositoryImpl
    ): TrackRepository
}