package dev.thuatnguyen.currencyconversion.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rate ORDER BY timestamp DESC LIMIT 1")
    fun getExchangeRates(): Single<ExchangeRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ExchangeRateEntity)

}