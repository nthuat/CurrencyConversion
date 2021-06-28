package dev.thuatnguyen.currencyconversion.data.response


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("info")
    val info: String = ""
)