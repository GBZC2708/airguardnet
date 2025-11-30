package com.airguardnet.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.airguardnet.mobile.data.local.entity.ReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {
    @Query("SELECT * FROM readings WHERE deviceId = :deviceId ORDER BY recordedAt DESC")
    fun observeByDevice(deviceId: Long): Flow<List<ReadingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<ReadingEntity>)

    @Query("DELETE FROM readings WHERE deviceId = :deviceId")
    suspend fun clearForDevice(deviceId: Long)
}
