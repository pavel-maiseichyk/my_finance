package com.example.myfinance.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.dto.OperationType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
data class MoneyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: OperationType,
    val moneyAmount: String,
    val currency: String,
    val category: String,
    val subcategory: String,
    val year: Int,
    val month: Int,
    val day: Int
) {
    fun toMoney(): Money = Money(
        id, type, moneyAmount.toBigDecimal(), currency,
        category, subcategory, year, month, day
    )

    companion object {
        fun fromMoney(money: Money) = MoneyEntity(
            money.id, money.type, money.moneyAmount.toEngineeringString(), money.currency,
            money.category, money.subcategory,
            money.year, money.month, money.day
        )
    }
}