package com.example.nearyou.model.user

import android.content.Context
import com.example.nearyou.model.credential.LoginCredential
import com.example.nearyou.model.credential.SignCredential
import com.example.nearyou.model.location.Location
import com.example.nearyou.model.response.ResponseBody
import com.example.nearyou.model.user.member.Member

object UserDAO {
    private val manager: UserManager = UserManager()
    var user: User? = null

    private fun saveUser(user: User?) {
        UserDAO.user = user
    }

    fun removeCredential(ctx: Context) {
        val sharedEditor = ctx.getSharedPreferences("Credential", Context.MODE_PRIVATE).edit()
        sharedEditor.remove("login")
        sharedEditor.remove("login")
        sharedEditor.apply()
    }
    fun getCredential(ctx: Context): LoginCredential? {
        val shared = ctx.getSharedPreferences("Credential", Context.MODE_PRIVATE)

        val login = shared.getString("login", null)
        val password = shared.getString("password", null)
        return if (login != null && password != null) {
            LoginCredential(login, password)
        } else {
            null
        }
    }
    fun saveCredentialCache(credential: LoginCredential, ctx: Context) {
        val sharedEditor = ctx.getSharedPreferences("Credential", Context.MODE_PRIVATE).edit()
        sharedEditor.putString("login", credential.login)
        sharedEditor.putString("password", credential.password)
        sharedEditor.apply()
    }
    fun saveCredentialCache(credential: SignCredential, ctx: Context) {
        this.saveCredentialCache(LoginCredential(credential.email, credential.password), ctx)
    }

    fun logout(ctx: Context) {
        removeCredential(ctx)

        // TODO : stop location service before

        user = null
    }

    suspend fun login(loginCredential: LoginCredential): ResponseBody<User?> {
        val response = this.manager.login(loginCredential)

        saveUser(response.data)

        return response
    }

    suspend fun signUp(signCredential: SignCredential): ResponseBody<User?> {
        val response = this.manager.signup(signCredential)

        saveUser(response.data)

        return response
    }

    suspend fun addLocation(location: Location): ResponseBody<Location?> = Location.manager.addLocation(location)
    suspend fun retrieveAllUserNearMe(): ResponseBody<Array<Member>> = this.manager.retrieveAllUserNearMe()
}