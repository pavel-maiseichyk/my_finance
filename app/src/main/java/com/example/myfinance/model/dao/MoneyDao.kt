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

    @Query("SELECT * FROM MoneyEntity WHERE (month = :month) AND (year = :year) AND (type = :type)")
    fun getMonthData(month: Int, year: Int, type: String): LiveData<List<MoneyEntity>>

    @Query("SELECT * FROM MoneyEntity WHERE (month = :month) AND (year = :year)")
    fun getAllMonthData(month: Int, year: Int): LiveData<List<MoneyEntity>>

    @Insert
    fun save(money: MoneyEntity)

    @Query("DELETE FROM MoneyEntity WHERE id = :id")
    fun deleteById(id: Int)
}