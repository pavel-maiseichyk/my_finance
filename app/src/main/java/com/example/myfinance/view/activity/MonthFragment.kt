package com.example.myfinance.view.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinance.R
import com.example.myfinance.databinding.FragmentMonthBinding
import com.example.myfinance.view.adapter.MoneyAdapter
import com.example.myfinance.viewmodel.MoneyViewModel
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.example.myfinance.model.dto.Money
import android.widget.Toast

import android.graphics.Movie




class MonthFragment : Fragment() {
    var month = Calendar.getInstance().get(Calendar.MONTH) + 1
    val year = Calendar.getInstance().get(Calendar.YEAR)
    private var currencyIsPLN = true
    var moneyAmountTextPLN = ""

    private val viewModel: MoneyViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onBackPressed()
        val binding = FragmentMonthBinding.inflate(
            inflater, container, false
        )

        with(binding) {
            val adapter = MoneyAdapter()
            rvSpending.layoutManager = LinearLayoutManager(activity)

            rvSpending.adapter = adapter

            toolbar.toolbarText.text = (createMonthName(month) + " " + year + " г.")
            toolbar.toolbarText.setOnClickListener {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val mMonth = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        month = monthOfYear + 1
                        toolbar.toolbarText.text = (createMonthName(month) + " " + year + " г.")
                        viewModel.getAllMonthData(month, year)
                            .observe(viewLifecycleOwner) { list ->
                                adapter.submitList(list)
                                moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                                tvSpentAmount.text = moneyAmountTextPLN
                            }
                    },
                    year,
                    mMonth,
                    day
                )
                dpd.show()
            }

            viewModel.getAllMonthData(month, year)
                .observe(viewLifecycleOwner) { list ->
                    adapter.submitList(list)
                    moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                    tvSpentAmount.text = moneyAmountTextPLN
                }

            chipAll.setOnClickListener {
                viewModel.getAllMonthData(month, year).observe(viewLifecycleOwner) { list ->
                    adapter.submitList(list)
                    layoutSpent.visibility = View.GONE
                }
            }

            chipSpent.setOnClickListener {
                viewModel.getMonthData(month, year, "SPENT").observe(viewLifecycleOwner) { list ->
                    adapter.submitList(list)
                    layoutSpent.visibility = View.VISIBLE
                    moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                    tvSpentAmount.text = moneyAmountTextPLN
                    tvSpentText.text = "Итого:"
                    tvSpentText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_red
                        )
                    )
                }
            }

            chipGot.setOnClickListener {
                viewModel.getMonthData(month, year, "RECEIVED")
                    .observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                        layoutSpent.visibility = View.VISIBLE
                        moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                        tvSpentAmount.text = moneyAmountTextPLN
                        tvSpentText.text = "Итого:"
                        tvSpentText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_green
                            )
                        )
                    }
            }

            tvSpentAmount.setOnClickListener {
                if (currencyIsPLN) {
                    val moneyPLN = tvSpentAmount.text.split(" ")[0].toDouble()
                    tvSpentAmount.text = (convertPLNToEUR(moneyPLN).toString() + " EUR")
                    currencyIsPLN = false
                } else {
                    tvSpentAmount.text = moneyAmountTextPLN
                    currencyIsPLN = true
                }
            }

            ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(
                    0, ItemTouchHelper.LEFT
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: ViewHolder, target: ViewHolder
                    ): Boolean = false

                    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                        viewModel.deleteById(adapter.getMoneyAt(viewHolder.adapterPosition).id)
                    }
                }).attachToRecyclerView(rvSpending)

            return root
        }
    }

    private fun countMoneyInPLN(list: List<Money>): Double {
        var moneyAmountPLN = 0.0
        var moneyAmountEUR = 0.0
        var moneyAmountUSD = 0.0
        var moneyAmountBYN = 0.0
        list.forEach {
            when (it.currency) {
                "PLN" -> moneyAmountPLN = moneyAmountPLN.plus(it.moneyAmount)
                "EUR" -> moneyAmountEUR = moneyAmountEUR.plus(it.moneyAmount)
                "USD" -> moneyAmountUSD = moneyAmountUSD.plus(it.moneyAmount)
                "BYN" -> moneyAmountBYN = moneyAmountBYN.plus(it.moneyAmount)
            }
        }
        return moneyAmountPLN + moneyAmountEUR * 4.58 + moneyAmountUSD * 3.95 + moneyAmountBYN * 1.57
    }

    private fun convertPLNToEUR(moneyAmountPlN: Double) = moneyAmountPlN * 0.22

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun createMonthName(month: Int): String =
        when (month) {
            1 -> "Январь"
            2 -> "Февраль"
            3 -> "Март"
            4 -> "Апрель"
            5 -> "Май"
            6 -> "Июнь"
            7 -> "Июль"
            8 -> "Август"
            9 -> "Сентябрь"
            10 -> "Октябрь"
            11 -> "Ноябрь"
            12 -> "Декабрь"
            else -> ""
        }
}