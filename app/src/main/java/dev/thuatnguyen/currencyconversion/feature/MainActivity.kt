package dev.thuatnguyen.currencyconversion.feature

import android.R
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.thuatnguyen.currencyconversion.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val currencyViewModel: CurrencyViewModel by viewModels()

    private val exchangeRateAdapter by lazy { ExchangeRateAdapter() }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupObservers()
        currencyViewModel.getCurrencies()
    }

    private fun setupObservers() {
        currencyViewModel.state.observe(this) {
            when (it) {
                is CurrencyState.CurrenciesLoaded -> {
                    val items = it.currencies.map { "${it.name} (${it.code})" }
                    val adapter = ArrayAdapter(
                        this,
                        R.layout.simple_spinner_dropdown_item,
                        items
                    )
                    binding.spinnerCurrency.adapter = adapter
                }
                is CurrencyState.ExchangeRateLoaded -> {
                    exchangeRateAdapter.exchangeRateList = it.exchangeRates
                }
            }
        }

        currencyViewModel.event.observe(this) {
            when (it) {
                CurrencyEvent.ShowLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                CurrencyEvent.HideLoading -> {
                    binding.progressBar.visibility = View.GONE
                }
                is CurrencyEvent.ShowError -> {
                    showMessage(it.message)
                }
            }
        }
    }

    private fun setupViews() {
        binding.recyclerView.run {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = exchangeRateAdapter
        }

        binding.edtAmount.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.hideKeyboard()
                calculateRatesIfAny()
            }
            true
        }

        binding.edtAmount.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                v.hideKeyboard()
                calculateRatesIfAny()
            }
        }
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.edtAmount.text.toString().toDoubleOrNull().let { amount ->
                        if (amount != null) {
                            currencyViewModel.getExchangeRates(amount, position)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if (currentFocus != null) {
//            calculateRatesIfAny()
//            val imm: InputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    private fun calculateRatesIfAny() {
        binding.edtAmount.text.toString().toDoubleOrNull().let { amount ->
            if (amount != null) {
                currencyViewModel.getExchangeRates(
                    amount,
                    binding.spinnerCurrency.selectedItemPosition
                )
            } else {
                if (binding.edtAmount.text.toString().isBlank()) {
                    showMessage("Please enter amount")
                } else {
                    showMessage("Please enter a valid number")
                }
            }
        }
    }


    private fun showMessage(message: String?) {
        Toast.makeText(this, message ?: "Something went wrong", Toast.LENGTH_LONG)
            .show()
    }

    private fun View.hideKeyboard() = ViewCompat.getWindowInsetsController(this)
        ?.hide(WindowInsetsCompat.Type.ime())
}