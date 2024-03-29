package com.example.nearyou.model

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    private val ID: Int,
    val link: String,
    private val created_date: String,
    private val edited_date: String
)