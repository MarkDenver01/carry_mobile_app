package com.nathaniel.carryapp.di

import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.data.remote.datasource.AuthRemoteDataSource
import com.nathaniel.carryapp.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(apiService: ApiService) =
        AuthRemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDatasource: AuthRemoteDataSource,
        tokenManager: TokenManager
    ) = AuthRepository(authRemoteDatasource, tokenManager)
}