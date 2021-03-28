package com.example.nearyou.model.user

import android.util.Log
import com.example.nearyou.model.Credential
import com.example.nearyou.model.response.ResponseBody
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class UserManager {
    suspend fun login(credential: Credential): ResponseBody<User?> {
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

            Log.e("Ok", data)
            Json.decodeFromString(data)
        } catch (e: ResponseException) {
            val data = e.response.content.readUTF8Line(10000).toString().replace("{}", "null");
            Json.decodeFromString(data)
        } catch (e: Exception) {
            Log.e("Erreur", e.localizedMessage)
            return Json.decodeFromString("{\"message\":\"Unknwon error !\",\"code\":\"E-UnknownError\",\"data\":null}")
        }
    }
}