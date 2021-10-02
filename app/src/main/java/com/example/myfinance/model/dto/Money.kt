package com.example.myfinance.model.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class Money(
    val id: Int,
    val type: OperationType,
    val moneyAmount: BigDecimal,
    val currency: String,
    val category: String,
    val subcategory: String,
    val year: Int,
    val month: Int,
    val day: Int,
)