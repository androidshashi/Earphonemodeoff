package com.shash.earphonemodeoff.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.shash.earphonemodeoff.utils.PREFERENCE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


//top level
val Context.userDatastore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

/**
 *Singleton class
 *
 */
object MyPreferences {

    //Constants
    private val KEY_HAS_RATED= booleanPreferencesKey("KEY_HAS_RATED")

    /**set whether user has accepted the condition or not*/
    suspend fun setRated(context: Context, accepted: Boolean) {
        context.userDatastore.apply {
            edit { it[KEY_HAS_RATED] = accepted }
        }
    }

    /**
     * Get whether user has accepted TNC or not
     */
    fun hasRated(context: Context):Flow<Boolean> = context.userDatastore.data.map {
        if (it[KEY_HAS_RATED] != null) it[KEY_HAS_RATED]!! else false
    }

}