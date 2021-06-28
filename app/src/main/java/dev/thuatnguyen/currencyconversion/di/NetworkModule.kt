package dev.thuatnguyen.currencyconversion.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.thuatnguyen.currencyconversion.data.api.CurrencyService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideCurrencyService(): CurrencyService {
        return CurrencyService.create()
    }
}