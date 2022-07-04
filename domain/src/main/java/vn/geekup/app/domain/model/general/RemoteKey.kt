package vn.geekup.app.domain.model.general

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class RemoteKey(@PrimaryKey val repoId: String, val nextKey: String) : BaseModel()