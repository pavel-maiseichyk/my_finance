package com.example.myfinance.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinance.R
import com.example.myfinance.databinding.SpendingCardBinding
import com.example.myfinance.model.dto.Money

class MoneyAdapter : ListAdapter<Money, MoneyAdapter.MoneyViewHolder>(MoneyDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyViewHolder {
        val binding =
            SpendingCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoneyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoneyViewHolder, position: Int) {
        val money = getItem(position)
        holder.bind(money)
    }

    fun getMoneyAt(position: Int): Money = getItem(position)

    inner class MoneyViewHolder(
        private val binding: SpendingCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(money: Money) {
            with(binding) {
                val context = tvSpendingType.context

                tvDate.text = makeDatePretty(money.day, money.month, money.year)

                if (money.type.toString() == "SPENT") {
                    tvSpendingType.text = "Потрачено"
                    tvSpendingType.setTextColor(ContextCompat.getColor(context, R.color.dark_red))
                } else {
                    tvSpendingType.text = "Получено"
                    tvSpendingType.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
                }
                tvSpent.text = (money.moneyAmount.toString() + " " + money.currency)
                tvCategory.text = money.category.trim()
                tvSubcategory.text = money.subcategory.trim()
            }
        }

        private fun makeDatePretty(day: Int, month: Int, year: Int): String {
            val formatYear: String = year.toString()
            val formatMonth: String = if (month <= 9) ("0$month") else month.toString()
            val formatDay: String = if (day <= 9) ("0$day") else day.toString()
            return "$formatDay.$formatMonth.$formatYear"
        }
    }
}

class MoneyDiffCallBack : DiffUtil.ItemCallback<Money>() {
    override fun areItemsTheSame(oldItem: Money, newItem: Money): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Money, newItem: Money): Boolean {
        return oldItem == newItem
    }
}