package vn.geekup.app.domain.dto

import vn.geekup.app.domain.model.general.SortType

data class UserEngagementRequestBody(
    val cursor: String? = null,
    val limit: Int = 20,
    val sort: SortType = SortType.DESC()
)