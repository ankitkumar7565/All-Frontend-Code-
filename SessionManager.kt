package com.example.managementtask.Data



import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun setLogin(isLogin: Boolean) {
        prefs.edit().putBoolean("IS_LOGIN", isLogin).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("IS_LOGIN", false)
    }

    fun saveUser(email: String, email1: String) {
        prefs.edit().putString("EMAIL", email).apply()
    }

    fun getUser(): String? {
        return prefs.getString("EMAIL", null)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
