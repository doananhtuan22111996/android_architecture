package vn.geekup.app.network

import android.content.Context
import android.net.*
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkChange(private val context: Context) {

  private val networkState: MutableLiveData<NetworkStatus> = MutableLiveData()

  fun networkState(): LiveData<NetworkStatus> {
    return networkState
  }

  init {
    setupNetworkChange()
  }

  @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
  private fun setupNetworkChange() {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
      override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        networkState.postValue(NetworkStatus.CapabilitiesChange(network, networkCapabilities))
      }

      override fun onLost(network: Network) {
        super.onLost(network)
        networkState.postValue(NetworkStatus.Lost(network))
      }

      override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        networkState.postValue(NetworkStatus.LinkPropertiesChange(network, linkProperties))
      }

      override fun onUnavailable() {
        super.onUnavailable()
        networkState.postValue(NetworkStatus.Unavailable)
      }

      override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        networkState.postValue(NetworkStatus.Losing(network, maxMsToLive))
      }

      override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkState.postValue(NetworkStatus.Available(network))
      }
    }
    connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
  }
}