package com.example.mynoteapp.data.local.preference

interface UserPreferenceDataSource {

    fun getUsername(): String?

    fun getPassword(): String?

    fun getSession(): Boolean

    fun setUser(newUsername: String, newPassword: String)

    fun setSession(newSession: Boolean)

}

class UserPreferenceDataSourceImpl(
    private val userPreference: UserPreference
) : UserPreferenceDataSource {

    override fun getUsername(): String? {
        return userPreference.username
    }

    override fun getPassword(): String? {
        return userPreference.password
    }

    override fun getSession(): Boolean {
        return userPreference.session
    }

    override fun setUser(newUsername: String, newPassword: String) {
        userPreference.username = newUsername
        userPreference.password = newPassword

    }

    override fun setSession(newSession: Boolean) {
        userPreference.session = newSession
    }

}