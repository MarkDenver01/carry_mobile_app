package com.nathaniel.carryapp.di

import com.nathaniel.carryapp.data.local.datasource.AddressDatasourceImpl
import com.nathaniel.carryapp.domain.datasource.AddressDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindAddressLocalDatasource(
        addressDataSourceImpl: AddressDatasourceImpl
    ): AddressDatasource
}