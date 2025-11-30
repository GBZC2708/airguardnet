package com.airguardnet.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.airguardnet.mobile.data.local.entity.HotspotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HotspotDao {
    @Query("SELECT * FROM hotspots ORDER BY recordedAt DESC")
    fun observeHotspots(): Flow<List<HotspotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hotspot: HotspotEntity)

    @Query("DELETE FROM hotspots")
    suspend fun clear()
}
