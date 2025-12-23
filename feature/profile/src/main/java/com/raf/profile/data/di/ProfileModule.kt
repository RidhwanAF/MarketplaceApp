package com.raf.profile.data.di

import android.content.Context
import androidx.room.Room
import com.raf.profile.data.local.room.ProfileDatabase
import com.raf.profile.data.remote.ProfileApiService
import com.raf.profile.data.repository.ProfileRepositoryImpl
import com.raf.profile.domain.repository.ProfileRepository
import com.raf.profile.domain.usecase.DeleteUserProfileByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileDatabase(
        @ApplicationContext context: Context,
    ): ProfileDatabase {
        return Room.databaseBuilder(
            context,
            ProfileDatabase::class.java,
            "profile_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        @ApplicationContext context: Context,
        profileDatabase: ProfileDatabase,
        profileApiService: ProfileApiService,
    ): ProfileRepository {
        return ProfileRepositoryImpl(context, profileDatabase, profileApiService)
    }

    @Provides
    @Singleton
    fun provideDeleteUserProfileByIdUseCase(profileRepository: ProfileRepository): DeleteUserProfileByIdUseCase {
        return DeleteUserProfileByIdUseCase(profileRepository)
    }

}