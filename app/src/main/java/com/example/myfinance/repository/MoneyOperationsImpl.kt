package com.example.myfinance.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.dao.MoneyDao
import com.example.myfinance.model.entity.MoneyEntity

class MoneyOperationsImpl(private val dao: MoneyDao) : MoneyOperations {
    override fun get(): LiveData<List<Money>> = Transformations.map(dao.get()) { list ->
        list.map { moneyEntity ->
            moneyEntity.toMoney()
        }
    }

    override fun operate(money: Money) = dao.save(money = MoneyEntity.fromMoney(money))
}
