package com.example.nearyou.model.user

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

    fun logout() {
        // TODO : stop location service before

        user = null
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

    suspend fun addLocation(location: Location): ResponseBody<Location?> = Location.manager.addLocation(location)
    suspend fun retrieveAllUserNearMe(): ResponseBody<Array<Member>> = this.manager.retrieveAllUserNearMe()
}