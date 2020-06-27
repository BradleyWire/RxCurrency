package com.bradley.wilson.currency.feed.list

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bradley.wilson.core.extensions.primitives.empty
import com.bradley.wilson.core.extensions.primitives.equalTo
import com.bradley.wilson.core.extensions.primitives.notEqualTo
import com.bradley.wilson.core.extensions.primitives.toCurrencyRate
import com.bradley.wilson.currency.R
import com.bradley.wilson.currency.feed.CurrencyItem
import com.bradley.wilson.currency.utils.CurrencyFlags
import kotlinx.android.synthetic.main.item_view_currency_feed.view.*
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

class CurrencyFeedRecyclerAdapter : RecyclerView.Adapter<CurrencyFeedRecyclerAdapter.CurrencyFeedViewHolder>() {

    private var currencyFeedItems = mutableListOf<CurrencyItem>()

    private val currencyFormatter by lazy {
        NumberFormat.getCurrencyInstance().apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
            roundingMode = RoundingMode.FLOOR
        }
    }

    private lateinit var onItemClicked: (currencyItem: CurrencyItem) -> Unit

    private lateinit var onRateChanged: (currencyItem: CurrencyItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyFeedViewHolder =
        CurrencyFeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_currency_feed, parent, false)
        )

    override fun getItemCount(): Int = currencyFeedItems.size

    override fun onBindViewHolder(holder: CurrencyFeedViewHolder, position: Int) =
        holder.bindAll(currencyFeedItems[position])

    override fun onBindViewHolder(
        holder: CurrencyFeedViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when {
            payloads.isNotEmpty() -> (payloads.first() as CurrencyItem)
                .also { holder.bindRate(it) }
            else -> onBindViewHolder(holder, position)
        }
    }

    fun rateChanged(onRateChanged: (currencyItem: CurrencyItem) -> Unit) {
        this.onRateChanged = onRateChanged
    }

    fun itemClicked(onItemClicked: (currencyItem: CurrencyItem) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    fun updateList(latestRates: List<CurrencyItem>) {
        val difference = DiffUtil.calculateDiff(CurrencyDiffCallback(currencyFeedItems, latestRates))
        difference.dispatchUpdatesTo(this)

        currencyFeedItems.clear()
        currencyFeedItems.addAll(latestRates)

    }

    inner class CurrencyFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currencyAmount = itemView.findViewById<EditText>(R.id.currency_amount)
        private val baseCurrencyTextWatcher = BaseTextWatcher {
            currencyAmount.setSelection(it.length)
            val currencyItem = currencyFeedItems[adapterPosition]
            if (it.isEmpty()) {
                onRateChanged(currencyItem.copy(rate = 0.00))
            } else {
                val newRate = it.toCurrencyRate()
                if (currencyItem.rate.notEqualTo(newRate)) {
                    onRateChanged(currencyItem.copy(rate = newRate))
                }
            }
        }

        fun bindAll(currencyItem: CurrencyItem) {
            with(itemView) {
                val currency = currencyInstance(currencyItem.country)
                currency_title.text = currencyItem.country
                currency_description.text = currency?.displayName ?: String.empty()
                currency_icon.text = CurrencyFlags.getFlagEmojiForCurrency(currency)
                bindRate(currencyItem)
                setOnClickListener { moveItemToTop() }
            }
        }

        fun bindRate(currency: CurrencyItem) {
            with(currencyAmount) {
                removeTextChangedListener(baseCurrencyTextWatcher)
                bindCurrencyData(currency)

                inputType = if (currency.isBateRate) {
                    addTextChangedListener(baseCurrencyTextWatcher)
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                } else {
                    InputType.TYPE_NULL
                }

            }
        }

        private fun moveItemToTop() {
            layoutPosition.takeIf { it > 0 }?.also { position ->
                onItemClicked(currencyFeedItems[position])
            }
        }

        private fun currencyInstance(countryCode: String) =
            try {
                Currency.getInstance(countryCode)
            } catch (exception: IllegalStateException) {
                null
            }

        private fun bindCurrencyData(currencyItem: CurrencyItem) {
            val currency = currencyInstance(currencyItem.country)
            currencyFormatter.currency = currency
            currencyAmount.setText(
                if (currencyItem.rate.equalTo(0.00)) {
                    String.empty()
                } else {
                    currencyFormatter.format(currencyItem.rate)
                        .replace(currency?.symbol ?: String.empty(), String.empty())
                }
            )
        }
    }
}