package com.example.nearyou.model.user

import com.example.nearyou.model.credential.LoginCredential
import com.example.nearyou.model.credential.SignCredential
import com.example.nearyou.model.response.ResponseBody
import com.example.nearyou.model.user.member.Member
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class UserManager {
    suspend fun login(credential: LoginCredential): ResponseBody<User?> {
        val client = HttpClient(Android) { }

        return try {
            val data: String = client.post("https://www.nearyou.iut.apokalypt.fr/api/1.0/user/login") {
                body = FormDataContent(
                        Parameters.build {
                            append("email", credential.login)
                            append("password", credential.password)
                        }
                )
            }

            Json.decodeFromString(data)
        } catch (e: ResponseException) {
            val data = e.response.content.readUTF8Line(10000).toString().replace("{}", "null");
            Json.decodeFromString(data)
        } catch (e: Exception) {
            return Json.decodeFromString("{\"message\":\"\",\"code\":\"E-UnknownError\",\"data\":null}")
        }
    }

    suspend fun signup(credential: SignCredential): ResponseBody<User?> {
        val client = HttpClient(Android) { }

        return try {
            val data: String = client.post("https://www.nearyou.iut.apokalypt.fr/api/1.0/user") {
                body = FormDataContent(
                        Parameters.build {
                            append("email", credential.email)
                            append("password", credential.password)
                            append("surname", credential.surname)
                            append("first_name", credential.first_name)
                            append("age", credential.age.toString())
                        }
                )
            }

            Json.decodeFromString(data)
        } catch (e: ResponseException) {
            val data = e.response.content.readUTF8Line(10000).toString().replace("{}", "null");
            Json.decodeFromString(data)
        } catch (e: Exception) {
            return Json.decodeFromString("{\"message\":\"\",\"code\":\"E-UnknownError\",\"data\":null}")
        }
    }

    suspend fun retrieveAllUserNearMe(): ResponseBody<Array<Member>> {
        val client = HttpClient(Android) { }

        return try {
            val urlString = "https://www.nearyou.iut.apokalypt.fr/api/1.0/user/${UserDAO.user!!.ID}/near"
            val data: String = client.get(urlString) {
                header("Authorization", "Bearer ${UserDAO.user!!.token}")
            }

            Json.decodeFromString(data)
        } catch (e: ResponseException) {
            val data = e.response.content.readUTF8Line(10000).toString()
            Json.decodeFromString(data)
        } catch (e: Exception) {
            return Json.decodeFromString("{\"message\":\"\",\"code\":\"E-UnknownError\",\"data\":[]}")
        }
    }
}