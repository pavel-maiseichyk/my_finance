package com.example.myfinance.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myfinance.model.entity.MoneyEntity

@Dao
interface MoneyDao {

    @Query("SELECT * FROM MoneyEntity ORDER BY id")
    fun get(): LiveData<List<MoneyEntity>>

    @Insert
    fun save(money: MoneyEntity)
}