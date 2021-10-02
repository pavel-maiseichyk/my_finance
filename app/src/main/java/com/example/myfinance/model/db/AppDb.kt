package com.example.myfinance.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfinance.model.dao.MoneyDao
import com.example.myfinance.model.entity.MoneyEntity

@Database(entities = [MoneyEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun moneyDao(): MoneyDao

    companion object {
        private const val DB_NAME = "app.db"
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also { instance = it }
            }
        }

        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()
    }
}