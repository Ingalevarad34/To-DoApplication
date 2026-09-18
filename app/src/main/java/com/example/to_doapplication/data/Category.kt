package com.example.to_doapplication.data

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val colorHex: Long = 0xFFFFFFFF,
    val iconName: String = "",
    val userId: String = ""
) {
    // Empty constructor for Firebase
    constructor() : this("", "", 0xFFFFFFFF, "", "")
}
