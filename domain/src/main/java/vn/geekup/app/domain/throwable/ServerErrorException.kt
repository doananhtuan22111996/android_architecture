package vn.geekup.app.domain.throwable

 class ServerErrorException(val code: Int? = 0, override val message: String? = "") : Throwable(message)