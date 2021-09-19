package vn.geekup.app.data.model.user

import vn.geekup.app.data.model.general.BaseVO
import vn.geekup.app.domain.model.user.UserIndicatorModel

data class UserIndicatorVO(
    val sparrowPoints: Int = 0,
    val currentRank: String? = "",
    val rankingPoints: Int = 0,
    val pointsToNextRank: Int = 0,
    val nextRank: String? = "",
    val aliaPoints: Int = 0
) : BaseVO<UserIndicatorModel>() {

    override fun vo2Model(): UserIndicatorModel = UserIndicatorModel(
        sparrowPoints = sparrowPoints,
        currentRank = currentRank,
        rankingPoints = rankingPoints,
        pointsToNextRank = pointsToNextRank,
        nextRank = nextRank,
        aliaPoints = aliaPoints
    )
}