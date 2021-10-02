package com.example.myfinance.view.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinance.databinding.FragmentMonthBinding
import com.example.myfinance.view.adapter.MoneyAdapter
import com.example.myfinance.viewmodel.MoneyViewModel
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.example.myfinance.model.dto.Money

import com.example.myfinance.R
import com.example.myfinance.model.dto.OperationType
import java.math.BigDecimal


class MonthFragment : Fragment() {

    companion object {
        const val PLN_TO_EUR = "0.22"
        const val EUR_TO_PLN = "4.58"
        const val USD_TO_PLN = "3.95"
        const val BYN_TO_PLN = "1.57"
    }

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

            toolbar.toolbarText.text = (createMonthName(month) + ", " + year)
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
                                if (list.isEmpty()) tvNothingFound.visibility =
                                    View.VISIBLE else tvNothingFound.visibility = View.GONE
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
                    if (list.isEmpty()) tvNothingFound.visibility =
                        View.VISIBLE else tvNothingFound.visibility = View.GONE
                    moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                    tvSpentAmount.text = moneyAmountTextPLN
                }

            chipAll.setOnClickListener {
                viewModel.getAllMonthData(month, year).observe(viewLifecycleOwner) { list ->
                    adapter.submitList(list)
                    if (list.isEmpty()) tvNothingFound.visibility =
                        View.VISIBLE else tvNothingFound.visibility = View.GONE
                    layoutSpent.visibility = View.GONE
                }
            }

            chipSpent.setOnClickListener {
                viewModel.getMonthData(month, year, OperationType.SPENT)
                    .observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                        if (list.isEmpty()) tvNothingFound.visibility =
                            View.VISIBLE else tvNothingFound.visibility = View.GONE
                        layoutSpent.visibility = View.VISIBLE
                        moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                        tvSpentAmount.text = moneyAmountTextPLN
                        tvSpentText.text = resources.getString(R.string.summary)
                        tvSpentText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_red
                            )
                        )
                    }
            }

            chipGot.setOnClickListener {
                viewModel.getMonthData(month, year, OperationType.RECEIVED)
                    .observe(viewLifecycleOwner) { list ->
                        adapter.submitList(list)
                        if (list.isEmpty()) tvNothingFound.visibility =
                            View.VISIBLE else tvNothingFound.visibility = View.GONE
                        layoutSpent.visibility = View.VISIBLE
                        moneyAmountTextPLN = (countMoneyInPLN(list).toString() + " PLN")
                        tvSpentAmount.text = moneyAmountTextPLN
                        tvSpentText.text = resources.getString(R.string.summary)
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
                    val moneyPLN = tvSpentAmount.text.split(" ")[0].toBigDecimal()
                    tvSpentAmount.text = (convertPLNToEUR(moneyPLN).toString() + " EUR")
                    currencyIsPLN = false
                } else {
                    tvSpentAmount.text = moneyAmountTextPLN
                    currencyIsPLN = true
                }
            }

            btnMoveToEnter.setOnClickListener {
                findNavController().navigateUp()
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

    private fun countMoneyInPLN(list: List<Money>): BigDecimal {
        var moneyAmountPLN: BigDecimal = BigDecimal("0.0")
        var moneyAmountEUR: BigDecimal = BigDecimal("0.0")
        var moneyAmountUSD: BigDecimal = BigDecimal("0.0")
        var moneyAmountBYN: BigDecimal = BigDecimal("0.0")
        list.forEach {
            when (it.currency) {
                "PLN" -> moneyAmountPLN = moneyAmountPLN.add(it.moneyAmount)
                "EUR" -> moneyAmountEUR = moneyAmountEUR.add(it.moneyAmount)
                "USD" -> moneyAmountUSD = moneyAmountUSD.add(it.moneyAmount)
                "BYN" -> moneyAmountBYN = moneyAmountBYN.add(it.moneyAmount)
            }
        }
        return moneyAmountPLN +
                moneyAmountEUR.multiply(BigDecimal(EUR_TO_PLN)) +
                moneyAmountUSD.multiply(BigDecimal(USD_TO_PLN)) +
                moneyAmountBYN.multiply(BigDecimal(BYN_TO_PLN))
    }

    private fun convertPLNToEUR(moneyAmountPlN: BigDecimal) =
        moneyAmountPlN.multiply(BigDecimal(PLN_TO_EUR))

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
            1 -> resources.getStringArray(R.array.month_array)[0]
            2 -> resources.getStringArray(R.array.month_array)[1]
            3 -> resources.getStringArray(R.array.month_array)[2]
            4 -> resources.getStringArray(R.array.month_array)[3]
            5 -> resources.getStringArray(R.array.month_array)[4]
            6 -> resources.getStringArray(R.array.month_array)[5]
            7 -> resources.getStringArray(R.array.month_array)[6]
            8 -> resources.getStringArray(R.array.month_array)[7]
            9 -> resources.getStringArray(R.array.month_array)[8]
            10 -> resources.getStringArray(R.array.month_array)[9]
            11 -> resources.getStringArray(R.array.month_array)[10]
            12 -> resources.getStringArray(R.array.month_array)[10]
            else -> ""
        }
}