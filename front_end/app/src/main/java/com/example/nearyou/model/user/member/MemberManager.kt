package com.example.nearyou.model.user.member

import com.example.nearyou.model.response.ResponseBody
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.UnknownHostException

class MemberManager {
    suspend fun retrieveData(id: String, token: String): ResponseBody<Member?> {
        val client = HttpClient(Android) { }

        return try {
            val data: String = client.get("https://www.nearyou.iut.apokalypt.fr/api/1.0/user/${id}") {
                header("Authorization", "Bearer $token")
            }

            Json.decodeFromString(data)
        } catch (e: ResponseException) {
            val data = e.response.content.readUTF8Line(10000).toString().replace("{}", "null");
            Json.decodeFromString(data)
        } catch (e: UnknownHostException) {
            return Json.decodeFromString("{\"message\":\"No internet !\",\"code\":\"E-NoInternet\",\"data\":null}")
        } catch (e: Exception) {
            return Json.decodeFromString("{\"message\":\"\",\"code\":\"E-UnknownError\",\"data\":null}")
        }
    }
}