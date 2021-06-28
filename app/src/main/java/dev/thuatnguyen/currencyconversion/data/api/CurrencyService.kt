package dev.thuatnguyen.currencyconversion.data.api

import dev.thuatnguyen.currencyconversion.BuildConfig
import dev.thuatnguyen.currencyconversion.data.response.CurrenciesResponse
import dev.thuatnguyen.currencyconversion.data.response.ExchangeRatesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("list")
    fun getCurrencyList(
        @Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY
    ): Single<CurrenciesResponse>

    @GET("live")
    fun getExchangeRateList(
        @Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY
    ): Single<ExchangeRatesResponse>

    companion object {
        private const val BASE_URL = "http://api.currencylayer.com/"

        fun create(): CurrencyService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(CurrencyService::class.java)
        }
    }
}