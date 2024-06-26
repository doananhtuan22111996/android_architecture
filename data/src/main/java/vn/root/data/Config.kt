package vn.root.data

internal object Config {
	val isDebug: Boolean
		get() = BuildConfig.DEBUG
	
	val mainDomain: String
		get() = BuildConfig.MAIN_DOMAIN
	
	object Cache {
		const val CACHE_FILE_NAME = "HttpCache"
		const val CACHE_FILE_SIZE = 1025 * 1024L * 5
	}
	
	object TimeOut {
		const val TIMEOUT_READ_SECOND = 15L
		const val TIMEOUT_CONNECT_SECOND = 15L
		const val TIMEOUT_WRITE_SECOND = 15L
	}
	
	object SharePreference {
		const val KEY_AUTH_TOKEN = "key_auth_token"
		const val KEY_AUTH_REFRESH_TOKEN = "key_auth_refresh_token"
	}
}