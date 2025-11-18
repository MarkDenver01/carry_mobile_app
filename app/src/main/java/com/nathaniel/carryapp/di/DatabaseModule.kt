package com.nathaniel.carryapp.di

import android.content.Context
import androidx.room.Room
import com.nathaniel.carryapp.data.local.room.CarryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): CarryDatabase =
        Room.databaseBuilder(
            app,
            CarryDatabase::class.java,
            "carry_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideLoginDao(db: CarryDatabase) = db.loginDao()

    @Provides
    fun provideCustomerDao(db: CarryDatabase) = db.customerDao()

    @Provides
    fun provideDriverDao(db: CarryDatabase) = db.driverDao()
}