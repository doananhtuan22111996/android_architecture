package vn.geekup.app.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import vn.geekup.app.domain.model.moment.MomentModel

@Dao
interface TravelDao : BaseDao<MomentModel> {

    @Query("SELECT * FROM MomentModel")
    fun getTravelFeeds(): List<MomentModel>

    @Query("SELECT * FROM MomentModel ORDER BY `id` DESC")
    fun getPagingTravelFeeds(): PagingSource<Int, MomentModel>

    @Query("DELETE FROM MomentModel")
    suspend fun delete()

}