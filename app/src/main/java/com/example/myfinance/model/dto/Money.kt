package com.example.myfinance.model.dto

data class Money(
    val id: Int,
    val type: String,
    val moneyAmount: Double,
    val currency: String,
    val category: String,
    val subcategory: String,
    val year: Int,
    val month: Int,
    val day: Int
)