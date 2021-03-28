package com.example.nearyou.model.user.member

import com.example.nearyou.model.Link
import com.example.nearyou.model.user.UserManager
import kotlinx.serialization.Serializable

@Serializable
data class Member(val ID: Int,
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
                  val created_date: String) {
    companion object {
        val manager: MemberManager = MemberManager()
    }
}