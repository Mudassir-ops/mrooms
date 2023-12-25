package com.example.janberktaskmudassirsatti.di

import android.content.Context
import com.example.janberktaskmudassirsatti.di.repository.ScreenshotRepository
import com.example.janberktaskmudassirsatti.di.repository.ScreenshotRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideImageRepository(
        @ApplicationContext context: Context
    ): ScreenshotRepository {
        return ScreenshotRepositoryImpl(context)
    }

}

