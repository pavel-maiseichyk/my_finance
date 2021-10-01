package com.example.myfinance.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfinance.model.dto.Money

@Entity
data class MoneyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String,
    val moneyAmount: Double,
    val currency: String,
    val category: String,
    val subcategory: String,
    val year: Int,
    val month: Int,
    val day: Int
) {
    fun toMoney(): Money = Money(
        id, type, moneyAmount, currency, category, subcategory, year, month, day
    )

    companion object {
        fun fromMoney(money: Money) = MoneyEntity(
            money.id, money.type, money.moneyAmount, money.currency,
            money.category, money.subcategory, money.year, money.month, money.day
        )
    }
}