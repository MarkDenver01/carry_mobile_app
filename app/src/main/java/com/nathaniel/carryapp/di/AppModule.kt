package com.nathaniel.carryapp.di

import com.nathaniel.carryapp.MainApplication
import com.nathaniel.carryapp.data.local.datasource.LoginLocalDataSourceImpl
import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.data.remote.datasource.AuthImplRemoteDataSource
import com.nathaniel.carryapp.data.repository.AuthRepository
import com.nathaniel.carryapp.domain.datasource.AuthRemoteDatasource
import com.nathaniel.carryapp.domain.datasource.LoginLocalDataSource
import com.nathaniel.carryapp.domain.usecase.VerifyOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.math.log

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(apiService: ApiService): AuthRemoteDatasource {
        return AuthImplRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideLoginLocalDataSource(
        impl: LoginLocalDataSourceImpl
    ): LoginLocalDataSource = impl

    @Provides
    @Singleton
    fun provideAuthRepository(
        remote: AuthRemoteDatasource,
        local: LoginLocalDataSource,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepository(remote, local, tokenManager)
    }

    @Provides
    @Singleton
    fun provideVerifyOtpUseCase(
        authRepository: AuthRepository
    ): VerifyOtpUseCase = VerifyOtpUseCase(authRepository)
}
