package com.example.nearyou.model.user

import com.example.nearyou.model.credential.LoginCredential
import com.example.nearyou.model.credential.SignCredential
import com.example.nearyou.model.response.ResponseBody

object UserDAO {
    private val manager: UserManager = UserManager()
    var user: User? = null

    private fun saveUser(user: User?) {
        UserDAO.user = user
    }

    suspend fun login(loginCredential: LoginCredential): ResponseBody<User?> {
        val response = this.manager.login(loginCredential)

        saveUser(response.data)

        return response
    }

    suspend fun signup(signCredential: SignCredential): ResponseBody<User?> {
        val response = this.manager.signup(signCredential)

        saveUser(response.data)

        return response
    }
}