package com.example.nearyou.model.user

import com.example.nearyou.model.Link
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val ID: Int,
    val url_profile: String,
    var surname: String,
    var first_name: String,
    var age: Int,
    val custom_status: String,
    val is_public: Int,
    var links: Array<Link>,
    val distance: Int,
    val created_date: String,
    val edited_date: String,
    val token: String
)