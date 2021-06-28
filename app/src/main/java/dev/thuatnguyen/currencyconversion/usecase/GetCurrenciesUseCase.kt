package dev.thuatnguyen.currencyconversion.usecase

import dev.thuatnguyen.currencyconversion.data.model.Currency
import dev.thuatnguyen.currencyconversion.domain.repository.CurrencyRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface GetCurrenciesUseCase {
    fun execute(): Single<List<Currency>>
}

class GetCurrenciesUseCaseImpl @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : GetCurrenciesUseCase {
    override fun execute(): Single<List<Currency>> {
        return currencyRepository.getCurrencies()
    }
}