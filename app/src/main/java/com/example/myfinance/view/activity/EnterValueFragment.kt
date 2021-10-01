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
import com.example.myfinance.viewmodel.MoneyViewModel
import com.google.android.material.snackbar.Snackbar
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
            toolbar.toolbarText.text = "My Finance"

            ArrayAdapter.createFromResource(
                requireContext(), R.array.currency_array,
                R.layout.support_simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spCurrency.adapter = adapter
            }

            btnSave.setOnClickListener {
                val category = etCategory.text.toString()
                val spentAmount = etSpent.text.toString()
                val subcategory = etSubcategory.text.toString()
                if (category == "" || subcategory == "" || spentAmount == "") {
                    Snackbar.make(it, "Can't be empty!", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val calendar = Calendar.getInstance()
                val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
                val month: Int = calendar.get(Calendar.MONTH) + 1
                val year: Int = calendar.get(Calendar.YEAR)
                val type = if (chipSpent.isChecked) "SPENT" else "RECEIVED"

                val money = Money(
                    id = 0,
                    type = type,
                    moneyAmount = spentAmount.toDouble(),
                    currency = spCurrency.selectedItem.toString(),
                    category = category,
                    subcategory = subcategory,
                    year = year,
                    month = month,
                    day = day
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