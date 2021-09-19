package vn.geekup.app.domain.model.general

sealed class SortType {
    data class DESC(val sortName: String = "desc") : SortType()
    data class ASC(val sortName: String = "asc") : SortType()
}
