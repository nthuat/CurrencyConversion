package dev.thuatnguyen.currencyconversion.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(
    @ColumnInfo(name = "timestamp")
    @PrimaryKey
    val timestamp: Long,

    @ColumnInfo(name = "quotes")
    val quotes: Map<String, Double>

)