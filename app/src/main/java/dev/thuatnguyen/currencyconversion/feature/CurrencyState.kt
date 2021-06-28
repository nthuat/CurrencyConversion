package dev.thuatnguyen.currencyconversion.feature

import dev.thuatnguyen.currencyconversion.data.model.Currency
import dev.thuatnguyen.currencyconversion.data.model.ExchangeRate

sealed class CurrencyState {
    object NotLoaded : CurrencyState()

    data class CurrenciesLoaded(val currencies: List<Currency>) : CurrencyState()

    data class ExchangeRateLoaded(val exchangeRates: List<ExchangeRate>) : CurrencyState()
}

sealed class CurrencyEvent {
    object ShowLoading : CurrencyEvent()

    object HideLoading : CurrencyEvent()

    data class ShowError(val message: String?) : CurrencyEvent()
}
