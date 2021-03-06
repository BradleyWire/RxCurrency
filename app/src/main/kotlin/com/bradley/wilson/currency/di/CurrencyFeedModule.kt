package com.bradley.wilson.currency.di

import com.bradley.wilson.core.ui.CoroutineDispatcherProvider
import com.bradley.wilson.currency.data.CurrencyDataSource
import com.bradley.wilson.currency.data.CurrencyRepository
import com.bradley.wilson.currency.data.local.source.CurrencyDatabaseService
import com.bradley.wilson.currency.data.local.source.CurrencyLocalDataSource
import com.bradley.wilson.currency.data.remote.CurrencyApi
import com.bradley.wilson.currency.data.remote.CurrencyApiService
import com.bradley.wilson.currency.data.remote.CurrencyRemoteDataSource
import com.bradley.wilson.currency.feed.CurrencyFeedViewModel
import com.bradley.wilson.currency.usecase.ConvertRatesUseCase
import com.bradley.wilson.currency.usecase.GetLatestRatesUseCase
import com.bradley.wilson.core.network.NetworkClient
import com.bradley.wilson.currency.CurrencyMapper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val currencyFeedModule: Module = module {
    viewModel { CurrencyFeedViewModel(get(), get(), get(), get()) }

    factory { CoroutineDispatcherProvider() }
    factory { ConvertRatesUseCase() }
    factory { GetLatestRatesUseCase(get()) }
    single<CurrencyRepository> { CurrencyDataSource(get(), get(), get()) }
    factory { CurrencyMapper() }
    factory { CurrencyRemoteDataSource(get()) }
    factory { CurrencyApiService(get()) }
    factory { get<NetworkClient>().create(CurrencyApi::class.java) }
    factory { CurrencyLocalDataSource(get()) }
    factory { CurrencyDatabaseService(get()) }
}
