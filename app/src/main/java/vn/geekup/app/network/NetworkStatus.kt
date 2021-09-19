package vn.geekup.app.network

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

sealed class NetworkStatus {
  data class Available(val network: Network?) : NetworkStatus()
  data class Losing(val network: Network?, val maxMsToLive: Int) : NetworkStatus()
  data class Lost(val network: Network?) : NetworkStatus()
  object Unavailable : NetworkStatus()
  data class CapabilitiesChange(val network: Network?, val networkCapabilities: NetworkCapabilities?) : NetworkStatus()
  data class LinkPropertiesChange(val network: Network?, val linkProperties: LinkProperties?) : NetworkStatus()
}