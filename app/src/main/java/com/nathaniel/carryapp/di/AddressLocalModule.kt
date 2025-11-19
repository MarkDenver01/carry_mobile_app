package com.nathaniel.carryapp.di

import com.nathaniel.carryapp.data.local.datasource.AddressLocalDataSourceImpl
import com.nathaniel.carryapp.domain.datasource.AddressLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AddressLocalModule {

    @Binds
    @Singleton
    abstract fun bindAddressLocalDatasource(
        addressLocalDataSourceImpl: AddressLocalDataSourceImpl
    ): AddressLocalDataSource
}