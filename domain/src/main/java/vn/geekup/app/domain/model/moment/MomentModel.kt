package vn.geekup.app.domain.model.moment

import androidx.room.Entity
import androidx.room.PrimaryKey
import vn.geekup.app.domain.model.general.BaseModel

@Entity
data class MomentModel(
    @PrimaryKey
    val id: Int,
    val channelName: String,
    val posterName: String,
) : BaseModel()
