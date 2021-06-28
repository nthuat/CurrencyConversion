package dev.thuatnguyen.currencyconversion.domain.repository

import dev.thuatnguyen.currencyconversion.data.api.CurrencyService
import dev.thuatnguyen.currencyconversion.data.db.ExchangeRateDao
import dev.thuatnguyen.currencyconversion.data.db.ExchangeRateEntity
import dev.thuatnguyen.currencyconversion.data.mapper.toEntity
import dev.thuatnguyen.currencyconversion.data.model.Currency
import dev.thuatnguyen.currencyconversion.data.model.ExchangeRate
import io.reactivex.rxjava3.core.Single
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Provider

interface CurrencyRepository {
    fun getCurrencies(): Single<List<Currency>>

    fun getExchangeRates(): Single<List<ExchangeRate>>
}

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrencyService,
    private val exchangeRateDao: ExchangeRateDao,
    private val currentTime: Provider<Long>
) : CurrencyRepository {

    override fun getCurrencies(): Single<List<Currency>> {
        return apiService.getCurrencyList().map { response ->
            if (!response.success) {
                throw ApiException(response.error?.code ?: 0, response.error?.info)
            } else if (response.currencies.isNullOrEmpty()) {
                throw ApiException(-1, "No currencies info from API")
            }
            response.currencies.toList().map {
                Currency(it.first, it.second)
            }
        }
    }

    override fun getExchangeRates(): Single<List<ExchangeRate>> {
        return getExchangeRatesFromLocal()
            .flatMap { entity ->
                if (shouldFetch(entity.timestamp)) {
                    getExchangeRatesFromRemote()
                } else {
                    Single.just(entity.quotes.toList())
                }
            }
            .map { quotes ->
                quotes.map {
                    ExchangeRate(dest = it.first.replaceFirst("USD", ""), rate = it.second)
                }
            }
    }

    private fun shouldFetch(lastTime: Long): Boolean {
        return currentTime.get() - lastTime * 1000 > VALID_CACHE_TIME_INTERVAL
    }

    private fun getExchangeRatesFromLocal(): Single<ExchangeRateEntity> {
        return exchangeRateDao.getExchangeRates()
            .onErrorReturn {
                ExchangeRateEntity(0, emptyMap())
            }
    }

    private fun getExchangeRatesFromRemote(): Single<List<Pair<String, Double>>> {
        return apiService.getExchangeRateList()
            .map { response ->
                if (!response.success) {
                    throw ApiException(response.error?.code ?: 0, response.error?.info)
                } else if (response.quotes.isNullOrEmpty()) {
                    throw ApiException(-1, "No exchange rate info from API")
                } else {
                    exchangeRateDao.insert(response.toEntity(currentTime.get()))
                    response.quotes.toList()
                }
            }
    }

    companion object {
        private const val VALID_CACHE_TIME_INTERVAL = 30 * 60 * 1000
    }
}

data class ApiException(val code: Int, val info: String?) : Exception(info)
