package vn.geekup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vn.geekup.domain.model.general.BaseModel
import vn.geekup.domain.model.general.RemoteKey

@Dao
interface BaseDao<T : BaseModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(obj: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)
}

@Dao
interface RemoteKeyDao : BaseDao<RemoteKey> {

    @Query("SELECT * FROM RemoteKey WHERE repoId = :id")
    fun remoteKeysId(id: String): RemoteKey?

    @Query("SELECT * FROM RemoteKey")
    fun remoteKeysIdAll(): List<RemoteKey>

    @Query("DELETE FROM RemoteKey")
    suspend fun delete()
}