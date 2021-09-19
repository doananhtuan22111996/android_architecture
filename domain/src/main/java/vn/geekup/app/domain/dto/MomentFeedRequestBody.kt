package vn.geekup.app.domain.dto

sealed class MomentSort {
    data class DESC(val sortName: String = "desc") : MomentSort()
    data class ASC(val sortName: String = "asc") : MomentSort()
}

data class MomentFeedRequestBody(
    val dates: ArrayList<String>? = null,
    val cursor: String? = null,
    val limit: Int = 0,
    val sort: MomentSort = MomentSort.DESC()
)