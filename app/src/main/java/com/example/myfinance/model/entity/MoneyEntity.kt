package com.example.myfinance.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.dto.OperationType

@Entity
data class MoneyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: OperationType,
    val moneyAmount: Double,
    val currency: String,
    val category: String,
    val subcategory: String,
    val month: String,
    val year: Int
) {
    fun toMoney(): Money = Money(
        id, type, moneyAmount, currency, category, subcategory, month, year
    )

    companion object {
        fun fromMoney(money: Money) = MoneyEntity(
            money.id, money.type, money.moneyAmount, money.currency,
            money.category, money.subcategory, money.month, money.year
        )
    }
}