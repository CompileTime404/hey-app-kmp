package com.dorianbanic.chat.data.notification

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dorianbanic.chat.domain.notification.CurrentDeviceTokenStore
import kotlinx.coroutines.flow.first

class DataStoreCurrentDeviceTokenStore(
    private val dataStore: DataStore<Preferences>
) : CurrentDeviceTokenStore {

    private val deviceTokenKey = stringPreferencesKey("KEY_DEVICE_TOKEN")

    override suspend fun get(): String? {
        return dataStore.data.first()[deviceTokenKey]
    }

    override suspend fun set(token: String?) {
        dataStore.edit { prefs ->
            if (token == null) {
                prefs.remove(deviceTokenKey)
            } else {
                prefs[deviceTokenKey] = token
            }
        }
    }
}