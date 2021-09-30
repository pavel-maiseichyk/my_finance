package com.example.myfinance.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinance.R
import com.example.myfinance.databinding.FragmentMonthBinding
import com.example.myfinance.model.dto.Money
import com.example.myfinance.view.adapter.MoneyAdapter
import com.example.myfinance.viewmodel.MoneyViewModel

class MonthFragment : Fragment() {
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

        val adapter = MoneyAdapter()
        binding.rvSpending.layoutManager = LinearLayoutManager(activity)
        binding.rvSpending.adapter = adapter
        viewModel.get().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        return binding.root
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}