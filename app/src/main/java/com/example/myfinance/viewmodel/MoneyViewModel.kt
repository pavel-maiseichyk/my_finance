package com.example.myfinance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.db.AppDb
import com.example.myfinance.model.dto.OperationType
import com.example.myfinance.repository.MoneyOperationsImpl

class MoneyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoneyOperationsImpl(AppDb.getInstance(application).moneyDao())

    fun get(): LiveData<List<Money>> = repository.get()

    fun operate(money: Money) = repository.operate(money)

    fun deleteById(id: Int) = repository.deleteById(id)

    fun getMonthData(month: Int, year: Int, type: OperationType): LiveData<List<Money>> =
        repository.getMonthData(month, year, type)

    fun getAllMonthData(month: Int, year: Int): LiveData<List<Money>> =
        repository.getAllMonthData(month, year)
}