package dev.thuatnguyen.currencyconversion.usecase

import dev.thuatnguyen.currencyconversion.data.model.ExchangeRate
import dev.thuatnguyen.currencyconversion.domain.repository.CurrencyRepository
import io.reactivex.rxjava3.core.Single
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

interface GetExchangeRateUseCase {
    fun execute(amount: Double, source: String): Single<List<ExchangeRate>>
}

class GetExchangeRateUseCaseImpl @Inject constructor(
    private val repository: CurrencyRepository
) : GetExchangeRateUseCase {

    override fun execute(amount: Double, source: String): Single<List<ExchangeRate>> {
        return repository.getExchangeRates()
            .map { exchangeRates ->
                val usdSourceRate = exchangeRates.find { it.dest.contains(source) }?.rate ?: 1.0
                exchangeRates
                    .map { exchangeRate ->
                        exchangeRate.copy(
                            source = source,
                            dest = "$source -> ${exchangeRate.dest}",
                            rate = BigDecimal((exchangeRate.rate / usdSourceRate) * amount)
                                .setScale(6, RoundingMode.HALF_EVEN).toDouble()
                        )
                    }
            }
    }
}