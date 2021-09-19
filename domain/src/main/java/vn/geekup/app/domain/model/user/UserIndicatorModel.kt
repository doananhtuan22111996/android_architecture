package vn.geekup.app.domain.model.user

import vn.geekup.app.domain.model.general.BaseModel

data class UserIndicatorModel(
    val sparrowPoints: Int = 0,
    val currentRank: String? = "",
    val rankingPoints: Int = 0,
    val pointsToNextRank: Int = 0,
    val nextRank: String? = "",
    val aliaPoints: Int = 0
) : BaseModel()