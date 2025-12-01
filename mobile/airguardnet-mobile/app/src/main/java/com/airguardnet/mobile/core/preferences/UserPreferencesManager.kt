package com.airguardnet.mobile.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "airguardnet_prefs")

data class UserPreferences(
    val criticalNotificationsEnabled: Boolean = true,
    val primaryDeviceId: Long? = null,
    val lastEmail: String? = null,
    val lastNotifiedCriticalAlertAt: Long? = null
)

@Singleton
class UserPreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val store = context.dataStore
    private val criticalKey = booleanPreferencesKey("critical_notifications")
    private val primaryDeviceKey = longPreferencesKey("primary_device")
    private val lastEmailKey = stringPreferencesKey("last_email")
    private val lastCriticalAlertAtKey = longPreferencesKey("last_critical_alert_at")

    val preferences: Flow<UserPreferences> = store.data.map { prefs ->
        UserPreferences(
            criticalNotificationsEnabled = prefs[criticalKey] ?: true,
            primaryDeviceId = prefs[primaryDeviceKey]?.takeIf { it > 0 },
            lastEmail = prefs[lastEmailKey],
            lastNotifiedCriticalAlertAt = prefs[lastCriticalAlertAtKey]?.takeIf { it > 0 }
        )
    }

    suspend fun setCriticalNotifications(enabled: Boolean) {
        store.edit { prefs ->
            prefs[criticalKey] = enabled
        }
    }

    suspend fun setPrimaryDevice(id: Long?) {
        store.edit { prefs ->
            if (id != null) {
                prefs[primaryDeviceKey] = id
            } else {
                prefs.remove(primaryDeviceKey)
            }
        }
    }

    suspend fun setLastEmail(email: String) {
        store.edit { prefs ->
            if (email.isNotBlank()) {
                prefs[lastEmailKey] = email
            } else {
                prefs.remove(lastEmailKey)
            }
        }
    }

    suspend fun setLastNotifiedCriticalAlertAt(timestamp: Long?) {
        store.edit { prefs ->
            if (timestamp != null && timestamp > 0) {
                prefs[lastCriticalAlertAtKey] = timestamp
            } else {
                prefs.remove(lastCriticalAlertAtKey)
            }
        }
    }
}
