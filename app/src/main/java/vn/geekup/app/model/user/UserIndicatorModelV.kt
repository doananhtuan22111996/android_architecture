package vn.geekup.app.model.user

import vn.geekup.app.R
import vn.geekup.app.base.BaseViewItem
import vn.geekup.app.domain.model.user.UserIndicatorModel

data class UserIndicatorModelV(
    val sparrowPoints: Int = 0,
    val currentRank: String? = "",
    val rankingPoints: Int = 0,
    val pointsToNextRank: Int = 0,
    val nextRank: String? = "",
    val aliaPoints: Int = 0
) : BaseViewItem {

    override fun getViewType(): Int = R.layout.item_user_indicator

    override fun getItemId(): String = "user_indicator_id"

    fun model2ModelV(bm: UserIndicatorModel): UserIndicatorModelV {
        return UserIndicatorModelV().copy(
            sparrowPoints = bm.sparrowPoints,
            currentRank = bm.currentRank,
            rankingPoints = bm.rankingPoints,
            pointsToNextRank = bm.pointsToNextRank,
            nextRank = bm.nextRank,
            aliaPoints = bm.aliaPoints
        )
    }
}