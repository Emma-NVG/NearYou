package com.example.nearyou.model.user

import com.example.nearyou.model.Link
import kotlinx.serialization.Serializable

@Serializable
data class User(val ID: Int,
                val email: String,
                val password: String,
                val url_profile: String,
                val surname: String,
                val first_name: String,
                val age: Int,
                val custom_status: String,
                val is_public: Int,
                val links: Array<Link>,
                val distance: String,
                val created_date: String,
                val token: String) {
    companion object {
        val manager: UserManager = UserManager()
    }
}