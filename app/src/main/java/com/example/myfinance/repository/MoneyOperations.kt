package com.example.myfinance.repository

import androidx.lifecycle.LiveData
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.dto.OperationType


interface MoneyOperations {
    fun get(): LiveData<List<Money>>

    fun getMonthData(month: Int, year: Int, type: OperationType): LiveData<List<Money>>

    fun getAllMonthData(month: Int, year: Int): LiveData<List<Money>>

    fun operate(money: Money)

    fun deleteById(id: Int)
}