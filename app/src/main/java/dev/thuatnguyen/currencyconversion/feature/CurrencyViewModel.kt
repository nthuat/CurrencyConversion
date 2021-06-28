package dev.thuatnguyen.currencyconversion.feature

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.thuatnguyen.currencyconversion.base.BaseViewModel
import dev.thuatnguyen.currencyconversion.data.model.Currency
import dev.thuatnguyen.currencyconversion.usecase.GetCurrenciesUseCase
import dev.thuatnguyen.currencyconversion.usecase.GetExchangeRateUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getExchangeRateUseCase: GetExchangeRateUseCase
) : BaseViewModel<CurrencyState, CurrencyEvent>() {

    override val initialState: CurrencyState
        get() = CurrencyState.NotLoaded

    private var currencyList = emptyList<Currency>()

    fun getCurrencies() {
        if (currencyList.isNotEmpty()) {
            setState(CurrencyState.CurrenciesLoaded(currencyList))
        } else {
            getCurrenciesUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { event(CurrencyEvent.ShowLoading) }
                .doFinally { event(CurrencyEvent.HideLoading) }
                .subscribe({ currencies ->
                    currencyList = currencies
                    setState(CurrencyState.CurrenciesLoaded(currencies))
                }, {
                    event(CurrencyEvent.ShowError(it.message))
                })
                .addToDisposables()
        }
    }

    fun getExchangeRates(amount: Double, position: Int) {
        val code = currencyList[position].code
        getExchangeRateUseCase.execute(amount, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { event(CurrencyEvent.ShowLoading) }
            .doFinally { event(CurrencyEvent.HideLoading) }
            .subscribe({
                setState(CurrencyState.ExchangeRateLoaded(it))
            }, {
                event(CurrencyEvent.ShowError(it.message))
            })
            .addToDisposables()
    }
}