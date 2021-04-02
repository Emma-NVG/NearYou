package com.example.nearyou.model.location

import com.example.nearyou.model.response.ResponseBody
import com.example.nearyou.model.user.UserDAO
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class LocationManager {
    suspend fun addLocation(location: Location): ResponseBody<Location?> {
        val client = HttpClient(Android) { }

        return try {
            val urlString =
                "https://www.nearyou.iut.apokalypt.fr/api/1.0/user/${UserDAO.user!!.ID}/location"
            val data: String = client.post(urlString) {
                header("Authorization", "Bearer ${UserDAO.user!!.token}")

                body = FormDataContent(
                    Parameters.build {
                        append("latitude", location.latitude.toString())
                        append("longitude", location.longitude.toString())
                    }
                )
            }

            Json.decodeFromString(data.replace("{}", Json.encodeToString(location)))
        } catch (e: ResponseException) {
            val data = e.response.content.readUTF8Line(10000).toString().replace("{}", "null");
            Json.decodeFromString(data)
        } catch (e: Exception) {
            return Json.decodeFromString("{\"message\":\"\",\"code\":\"E-UnknownError\",\"data\":null}")
        }
    }
}