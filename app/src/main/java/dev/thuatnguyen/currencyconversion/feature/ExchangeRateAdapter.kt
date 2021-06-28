package dev.thuatnguyen.currencyconversion.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.thuatnguyen.currencyconversion.data.model.ExchangeRate
import dev.thuatnguyen.currencyconversion.databinding.ListExchangeRateItemBinding

class ExchangeRateAdapter : RecyclerView.Adapter<ExchangeRateAdapter.ExchangeRateViewHolder>() {

    var exchangeRateList = listOf<ExchangeRate>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        val viewBinding = ListExchangeRateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExchangeRateViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bind(exchangeRateList[position])
    }

    override fun getItemCount() = exchangeRateList.size

    inner class ExchangeRateViewHolder(private val viewBinding: ListExchangeRateItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(exchangeRate: ExchangeRate) {
            viewBinding.tvCurrencyCode.text = exchangeRate.dest
            viewBinding.tvExchangeRate.text = exchangeRate.rate.toString()
        }

    }
}