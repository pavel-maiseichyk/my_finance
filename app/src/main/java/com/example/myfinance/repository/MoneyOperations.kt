package com.example.myfinance.repository

import androidx.lifecycle.LiveData
import com.example.myfinance.model.dto.Money


interface MoneyOperations {
    fun get(): LiveData<List<Money>>

    fun getMonthData(month: Int, year: Int, type: String): LiveData<List<Money>>

    fun getAllMonthData(month: Int, year: Int): LiveData<List<Money>>

    fun operate(money: Money)

    fun deleteById(id: Int)
}