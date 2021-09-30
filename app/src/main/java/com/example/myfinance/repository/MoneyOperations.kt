package com.example.myfinance.repository

import androidx.lifecycle.LiveData
import com.example.myfinance.model.dto.Money

interface MoneyOperations {
    fun get(): LiveData<List<Money>>

    fun operate(money: Money)
}