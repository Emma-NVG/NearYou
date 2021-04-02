package com.example.nearyou.model.user

import com.example.nearyou.model.Link
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val ID: Int,
    val url_profile: String,
    val surname: String,
    val first_name: String,
    val age: Int,
    val custom_status: String,
    val is_public: Int,
    val links: Array<Link>,
    val distance: Int,
    val created_date: String,
    val edited_date: String,
    val token: String
)