package com.raf.auth.data.di

import android.content.Context
import androidx.room.Room
import com.raf.auth.data.local.AuthDataStore
import com.raf.auth.data.local.db.AuthDatabase
import com.raf.auth.data.repository.AuthRepositoryImpl
import com.raf.auth.domain.repository.AuthRepository
import com.raf.auth.domain.usecase.LoginUseCase
import com.raf.auth.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthDatabase(@ApplicationContext context: Context): AuthDatabase {
        return Room.databaseBuilder(
            context,
            AuthDatabase::class.java,
            "auth_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): AuthDataStore {
        return AuthDataStore(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authDatabase: AuthDatabase, authDataStore: AuthDataStore): AuthRepository {
        return AuthRepositoryImpl(authDatabase, authDataStore)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }
}