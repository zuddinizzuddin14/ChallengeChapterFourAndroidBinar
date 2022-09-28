package com.example.mynoteapp.data.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreference(context: Context) {
    private val preference: SharedPreferences = context.getSharedPreferences(NAME, MODE)

    companion object {
        private const val NAME = "appNote"
        private const val MODE = Context.MODE_PRIVATE
    }

    var session: Boolean
        get() = preference.getBoolean(PreferenceKey.PREF_USER_SESSION, false)
    set(value) = preference.edit() {
        this.putBoolean(PreferenceKey.PREF_USER_SESSION, value)
    }

    var username: String?
        get() = preference.getString(PreferenceKey.PREF_USER_USERNAME, null)
        set(value) = preference.edit {
            this.putString(PreferenceKey.PREF_USER_USERNAME, value)
        }

    var password: String?
        get() = preference.getString(PreferenceKey.PREF_USER_PASSWORD, null)
        set(value) = preference.edit {
            this.putString(PreferenceKey.PREF_USER_PASSWORD, value)
        }

}

object PreferenceKey {
    val PREF_USER_SESSION = "PREF_USER_SESSION"
    val PREF_USER_USERNAME = "PREF_USER_USERNAME"
    val PREF_USER_PASSWORD = "PREF_USER_PASSWORD"
}