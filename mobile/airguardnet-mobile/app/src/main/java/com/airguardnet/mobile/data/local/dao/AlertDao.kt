package com.airguardnet.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.airguardnet.mobile.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts WHERE deviceId = :deviceId ORDER BY createdAt DESC")
    fun observeForDevice(deviceId: Long): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(alerts: List<AlertEntity>)

    @Query("DELETE FROM alerts WHERE deviceId = :deviceId")
    suspend fun clearForDevice(deviceId: Long)
}
