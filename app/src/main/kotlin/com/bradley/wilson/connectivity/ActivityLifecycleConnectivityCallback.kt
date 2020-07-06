package com.bradley.wilson.connectivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import com.bradley.wilson.R
import com.bradley.wilson.currency.feed.ConnectivityListener
import com.google.android.material.snackbar.Snackbar

class ActivityLifecycleConnectivityCallback : Application.ActivityLifecycleCallbacks {
    private lateinit var connectionSnackbar: Snackbar

    private val connectivityListener: ConnectivityListener =
        ConnectivityListener(networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                showConnectivitySnackbar()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                showConnectivitySnackbar()
            }

            @SuppressLint("InlinedApi")
            override fun onAvailable(network: Network) {
                hideConnectivitySnackbar()
            }
        })

    private fun hideConnectivitySnackbar() {
        connectionSnackbar.dismiss()
    }

    private fun showConnectivitySnackbar() {
        connectionSnackbar.show()
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) =
        connectivityListener.registerListener(activity)

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStopped(activity: Activity) =
        connectivityListener.unregisterListener(activity)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.apply {
            connectionSnackbar = Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.no_connection_error_message),
                Snackbar.LENGTH_INDEFINITE
            )
            showConnectivitySnackbar()
        }
    }

    override fun onActivityResumed(activity: Activity) {}
}