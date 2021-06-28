package dev.thuatnguyen.currencyconversion.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import dev.thuatnguyen.currencyconversion.domain.repository.CurrencyRepository
import dev.thuatnguyen.currencyconversion.domain.repository.CurrencyRepositoryImpl
import dev.thuatnguyen.currencyconversion.usecase.GetCurrenciesUseCase
import dev.thuatnguyen.currencyconversion.usecase.GetCurrenciesUseCaseImpl
import dev.thuatnguyen.currencyconversion.usecase.GetExchangeRateUseCase
import dev.thuatnguyen.currencyconversion.usecase.GetExchangeRateUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrencyModule {

    @Binds
    abstract fun bindGetCurrenciesUseCase(useCase: GetCurrenciesUseCaseImpl): GetCurrenciesUseCase

    @Binds
    abstract fun bindGetExchangeRateUseCase(useCase: GetExchangeRateUseCaseImpl): GetExchangeRateUseCase

    @Binds
    abstract fun bindCurrencyRepository(useCase: CurrencyRepositoryImpl): CurrencyRepository


}