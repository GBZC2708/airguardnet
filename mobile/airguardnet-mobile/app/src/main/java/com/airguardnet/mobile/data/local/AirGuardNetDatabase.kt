package com.airguardnet.mobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.airguardnet.mobile.data.local.dao.AlertDao
import com.airguardnet.mobile.data.local.dao.DeviceDao
import com.airguardnet.mobile.data.local.dao.HotspotDao
import com.airguardnet.mobile.data.local.dao.ReadingDao
import com.airguardnet.mobile.data.local.dao.UserSessionDao
import com.airguardnet.mobile.data.local.entity.AlertEntity
import com.airguardnet.mobile.data.local.entity.DeviceEntity
import com.airguardnet.mobile.data.local.entity.HotspotEntity
import com.airguardnet.mobile.data.local.entity.ReadingEntity
import com.airguardnet.mobile.data.local.entity.UserSessionEntity

@Database(
    entities = [UserSessionEntity::class, DeviceEntity::class, ReadingEntity::class, AlertEntity::class, HotspotEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AirGuardNetDatabase : RoomDatabase() {
    abstract fun userSessionDao(): UserSessionDao
    abstract fun deviceDao(): DeviceDao
    abstract fun readingDao(): ReadingDao
    abstract fun alertDao(): AlertDao
    abstract fun hotspotDao(): HotspotDao
}
