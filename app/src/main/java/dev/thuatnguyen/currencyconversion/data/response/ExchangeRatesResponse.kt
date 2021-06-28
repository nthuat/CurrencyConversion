package dev.thuatnguyen.currencyconversion.data.response


import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("quotes")
    val quotes: Map<String, Double>?,

    @SerializedName("timestamp")
    val timestamp: Long?,

    @SerializedName("error")
    val error: ErrorResponse?
)