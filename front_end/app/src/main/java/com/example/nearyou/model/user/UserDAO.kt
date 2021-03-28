package com.example.nearyou.model.user

import com.example.nearyou.model.Credential
import com.example.nearyou.model.response.ResponseBody

object UserDAO {
    var user: User? = null

    private fun saveUser(user: User?) {
        UserDAO.user = user
    }

    suspend fun login(credential: Credential): ResponseBody<User?> {
        val response = User.manager.login(credential)

        saveUser(response.data)

        return response
    }
}