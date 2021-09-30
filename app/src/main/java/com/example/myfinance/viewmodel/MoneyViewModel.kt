package com.example.myfinance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.db.AppDb
import com.example.myfinance.repository.MoneyOperationsImpl

class MoneyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MoneyOperationsImpl(AppDb.getInstance(application).moneyDao())

    fun get() = repository.get()

    fun operate(money: Money) = repository.operate(money)
}