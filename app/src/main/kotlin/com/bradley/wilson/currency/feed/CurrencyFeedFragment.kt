package com.bradley.wilson.currency.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.bradley.wilson.R
import com.bradley.wilson.core.extensions.android.scrollToTop
import com.bradley.wilson.currency.feed.list.CurrencyFeedRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_currency_feed.*
import org.koin.android.viewmodel.ext.android.viewModel

class CurrencyFeedFragment : Fragment(R.layout.fragment_currency_feed) {

    private val currencyFeedViewModel: CurrencyFeedViewModel by viewModel()

    private val currencyFeedAdapter by lazy {
        CurrencyFeedRecyclerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCurrencyFeed()
        observeFeed()
    }

    private fun initCurrencyFeed() {
        currency_feed_recycler_view.adapter = currencyFeedAdapter
        with(currencyFeedAdapter) {
            itemClicked {
                currencyFeedViewModel.onCurrencyItemClicked(it)
            }
            rateChanged {
                currencyFeedViewModel.updateFeed(it)
            }
        }
    }

    private fun observeFeed() {
        with(currencyFeedViewModel) {
            recyclerScrollerLiveData.observe(viewLifecycleOwner) {
                currency_feed_recycler_view.scrollToTop()
            }
            currencyFeedLiveData.observe(viewLifecycleOwner) {
                currencyFeedAdapter.updateList(it)
            }
        }
    }

    companion object {
        fun newInstance() = CurrencyFeedFragment()
    }
}