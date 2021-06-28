package dev.thuatnguyen.currencyconversion.data.mapper

import dev.thuatnguyen.currencyconversion.data.db.ExchangeRateEntity
import dev.thuatnguyen.currencyconversion.data.response.ExchangeRatesResponse

fun ExchangeRatesResponse.toEntity(timestamp: Long) = ExchangeRateEntity(
    timestamp = timestamp,
    quotes = quotes.orEmpty()
)