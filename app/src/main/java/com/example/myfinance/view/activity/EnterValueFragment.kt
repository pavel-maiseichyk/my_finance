package com.example.myfinance.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myfinance.R
import com.example.myfinance.databinding.FragmentEnterValueBinding
import com.example.myfinance.model.dto.Money
import com.example.myfinance.model.dto.OperationType
import com.example.myfinance.viewmodel.MoneyViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class EnterValueFragment : Fragment() {
    private val viewModel: MoneyViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEnterValueBinding.inflate(
            inflater, container, false
        )

        with(binding) {
            ArrayAdapter.createFromResource(
                requireContext(), R.array.currency_array,
                R.layout.support_simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spCurrency.adapter = adapter
            }

            btnSave.setOnClickListener {
                val calendar = Calendar.getInstance()
                val monthDate = SimpleDateFormat("MMMM")
                val month: String = monthDate.format(calendar.time)
                val year = calendar.get(Calendar.YEAR)

                val category = etCategory.text.toString()
                val spentAmount = etSpent.text.toString()
                val subcategory = etSubcategory.text.toString()

                if (category == "" || subcategory == "" || spentAmount == "") {
                    Snackbar.make(it, "Can't be empty!", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val type = OperationType.SPENT

                val money = Money(
                    id = 0,
                    type = type,
                    moneyAmount = spentAmount.toDouble(),
                    currency = spCurrency.selectedItem.toString(),
                    category = category,
                    subcategory = subcategory,
                    month = month,
                    year = year
                )

                etSpent.setText("")
                etCategory.setText("")
                etSubcategory.setText("")

                Snackbar.make(it, "Save successful!", Snackbar.LENGTH_LONG)
                    .show()

                viewModel.operate(money)
            }
            btnMoveToMonth.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_enter_value_to_fragment_month)
            }
        }
        return binding.root
    }
}