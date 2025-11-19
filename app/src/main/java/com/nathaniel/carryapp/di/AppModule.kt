package com.nathaniel.carryapp.di

import androidx.room.Update
import com.nathaniel.carryapp.data.local.datasource.LoginLocalDataSourceImpl
import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.data.remote.api.PsgcApiService
import com.nathaniel.carryapp.data.remote.datasource.AuthImplRemoteDataSource
import com.nathaniel.carryapp.data.repository.ApiRepository
import com.nathaniel.carryapp.data.repository.GeocodingRepository
import com.nathaniel.carryapp.data.repository.LocalRepository
import com.nathaniel.carryapp.domain.datasource.AddressLocalDataSource
import com.nathaniel.carryapp.domain.datasource.AuthRemoteDatasource
import com.nathaniel.carryapp.domain.datasource.LoginLocalDataSource
import com.nathaniel.carryapp.domain.usecase.ForwardGeocodeUseCase
import com.nathaniel.carryapp.domain.usecase.GetAddressUseCase
import com.nathaniel.carryapp.domain.usecase.GetAllProductsUseCase
import com.nathaniel.carryapp.domain.usecase.GetBarangaysByCityUseCase
import com.nathaniel.carryapp.domain.usecase.GetCitiesByProvinceUseCase
import com.nathaniel.carryapp.domain.usecase.GetCurrentLocationUseCase
import com.nathaniel.carryapp.domain.usecase.GetCustomerDetailsUseCase
import com.nathaniel.carryapp.domain.usecase.GetMobileOrEmailUseCase
import com.nathaniel.carryapp.domain.usecase.GetProvincesByRegionUseCase
import com.nathaniel.carryapp.domain.usecase.ReverseGeocodeUseCase
import com.nathaniel.carryapp.domain.usecase.SaveAddressUseCase
import com.nathaniel.carryapp.domain.usecase.SaveCustomerDetailsUseCase
import com.nathaniel.carryapp.domain.usecase.SaveMobileOrEmailUseCase
import com.nathaniel.carryapp.domain.usecase.UpdateAddressUseCase
import com.nathaniel.carryapp.domain.usecase.VerifyOtpUseCase
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
    fun provideAuthRemoteDataSource(
        apiService: ApiService,
        psgcApiService: PsgcApiService
    ): AuthRemoteDatasource {
        return AuthImplRemoteDataSource(apiService, psgcApiService)
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
    ): ApiRepository {
        return ApiRepository(remote, local, tokenManager)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(
        addressLocalDataSource: AddressLocalDataSource,
        loginLocalDataSource: LoginLocalDataSource,
        tokenManager: TokenManager
    ): LocalRepository = LocalRepository(addressLocalDataSource, loginLocalDataSource, tokenManager)

    @Provides
    @Singleton
    fun provideVerifyOtpUseCase(
        apiRepository: ApiRepository
    ): VerifyOtpUseCase = VerifyOtpUseCase(apiRepository)

    @Provides
    @Singleton
    fun provideGetAllProductsUseCase(
        apiRepository: ApiRepository
    ): GetAllProductsUseCase = GetAllProductsUseCase(apiRepository)

    @Provides
    @Singleton
    fun provideGetProvincesByRegionUseCase(
        apiRepository: ApiRepository
    ): GetProvincesByRegionUseCase = GetProvincesByRegionUseCase(apiRepository)

    @Provides
    @Singleton
    fun provideCitiesByProvinceUseCase(
        apiRepository: ApiRepository
    ): GetCitiesByProvinceUseCase = GetCitiesByProvinceUseCase(apiRepository)

    @Provides
    @Singleton
    fun provideBarangaysByCityUseCase(
        apiRepository: ApiRepository
    ): GetBarangaysByCityUseCase = GetBarangaysByCityUseCase(apiRepository)

    @Provides
    @Singleton
    fun provideSavedAddressUseCase(
        localRepository: LocalRepository
    ): SaveAddressUseCase = SaveAddressUseCase(localRepository)

    @Provides
    @Singleton
    fun provideForwardGeocodeUseCase(
        geocodingRepository: GeocodingRepository
    ): ForwardGeocodeUseCase = ForwardGeocodeUseCase(geocodingRepository)

    @Provides
    @Singleton
    fun provideReverseGeocodeUseCase(
        geocingRepository: GeocodingRepository
    ): ReverseGeocodeUseCase = ReverseGeocodeUseCase(geocingRepository)

    @Provides
    @Singleton
    fun provideGetAddressUseCase(
        localRepository: LocalRepository
    ): GetAddressUseCase = GetAddressUseCase(localRepository)

    @Provides
    @Singleton
    fun provideGetCurrentLocationUseCase(
        repository: GeocodingRepository
    ): GetCurrentLocationUseCase = GetCurrentLocationUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateAddressUseCase(
        repository: LocalRepository
    ): UpdateAddressUseCase = UpdateAddressUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveMobileOrEmailUseCase(
        repository: LocalRepository
    ): SaveMobileOrEmailUseCase = SaveMobileOrEmailUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMobileOrEmailUseCase(
        repository: LocalRepository
    ): GetMobileOrEmailUseCase = GetMobileOrEmailUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveCustomerDetailUseCase(
        repository: LocalRepository
    ): SaveCustomerDetailsUseCase = SaveCustomerDetailsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCustomerDetailUseCase(
        repository: LocalRepository
    ): GetCustomerDetailsUseCase = GetCustomerDetailsUseCase(repository)
}
