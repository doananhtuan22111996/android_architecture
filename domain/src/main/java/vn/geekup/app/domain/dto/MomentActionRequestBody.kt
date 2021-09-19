package vn.geekup.app.domain.dto

data class MomentLikeRequestBody(val type: Int = 1)

data class MomentPostCommentRequestBody(
    val content: String? = "",
)

data class MomentCommentRequestBody(
    val momentId: Int? = 0,
    val cursor: String? = null,
    val limit: Int = 10,
    val sort: MomentSort = MomentSort.DESC()
)