package com.example.myfinance.model.dto

data class Money(
    val id: Int,
    val type: OperationType,
    val moneyAmount: Double,
    val currency: String,
    val category: String,
    val subcategory: String,
    val month: String,
    val year: Int
)